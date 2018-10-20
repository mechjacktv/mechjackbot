package com.mechjacktv.mechjackbot.command;

import com.mechjacktv.mechjackbot.AppConfiguration;
import com.mechjacktv.mechjackbot.GlobalCoolDown;
import com.mechjacktv.mechjackbot.MessageEvent;
import com.mechjacktv.mechjackbot.RestrictToOwner;

import javax.inject.Inject;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings("CanBeFinal")
class DelConfigCommand extends AbstractCommand {

    private static final String COMMAND_SYNTAX_REGEX = "\\s+([\\w\\d.-_]+)\\s*";

    private final AppConfiguration appConfiguration;
    private final Pattern commandSyntaxPattern;
    private final CommandUtils commandUtils;

    @Inject
    DelConfigCommand(final AppConfiguration appConfiguration, final CommandUtils commandUtils) {
        super("!delconfig", commandUtils);
        this.appConfiguration = appConfiguration;
        this.commandSyntaxPattern = Pattern.compile(getTrigger() + COMMAND_SYNTAX_REGEX);
        this.commandUtils = commandUtils;
    }

    @Override
    public String getDescription() {
        return "Delets the value for the configuration key specified.";
    }

    @Override
    @RestrictToOwner
    @GlobalCoolDown
    public void handleMessage(MessageEvent messageEvent) {
        final String message = messageEvent.getMessage();
        final Matcher messageMatcher = this.commandSyntaxPattern.matcher(message);

        if (messageMatcher.matches()) {
            final String key = messageMatcher.group(1).trim().toLowerCase();

            this.appConfiguration.remove(key);
            messageEvent.sendResponse(String.format("%s deleted", key));
        } else {
            this.commandUtils.sendUsage(messageEvent, String.format("%s <key> = <value>", getTrigger()));
        }
    }

}
