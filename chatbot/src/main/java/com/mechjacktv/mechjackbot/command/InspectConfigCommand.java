package com.mechjacktv.mechjackbot.command;

import com.mechjacktv.mechjackbot.AppConfiguration;
import com.mechjacktv.mechjackbot.GlobalCoolDown;
import com.mechjacktv.mechjackbot.MessageEvent;
import com.mechjacktv.mechjackbot.RestrictToOwner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings("CanBeFinal")
class InspectConfigCommand extends AbstractCommand {

    private static final Logger log = LoggerFactory.getLogger(InspectConfigCommand.class);

    private static final String COMMAND_SYNTAX_REGEX = "\\s+([\\w\\d.-_]+)\\s*";

    private final AppConfiguration appConfiguration;
    private final Pattern commandSyntaxPattern;
    private final CommandUtils commandUtils;

    @Inject
    InspectConfigCommand(final AppConfiguration appConfiguration, final CommandUtils commandUtils) {
        super("!inspectconfig", commandUtils);
        this.appConfiguration = appConfiguration;
        this.commandSyntaxPattern = Pattern.compile(getTrigger() + COMMAND_SYNTAX_REGEX);
        this.commandUtils = commandUtils;
    }

    @Override
    public String getDescription() { return "Reports the value for the configuration key specified."; }

    @Override
    @RestrictToOwner
    @GlobalCoolDown
    public void handleMessage(MessageEvent messageEvent) {
        final String message = messageEvent.getMessage();
        final Matcher messageMatcher = this.commandSyntaxPattern.matcher(message);

        if (messageMatcher.matches()) {
            final String key = messageMatcher.group(1).trim().toLowerCase();

            if("all".equals(key)) {
                logConfigurationValues();
                messageEvent.sendResponse(String.format("Dumped all configuration values to the log"));
            } else {
                final Optional<String> value = this.appConfiguration.get(key);

                if(value.isPresent()) {
                    messageEvent.sendResponse(formatMessage(key, value.get()));
                } else {
                    messageEvent.sendResponse(String.format("No value was found for %s", key));
                }
            }
        } else {
            this.commandUtils.sendUsage(messageEvent, String.format("%s <key> = <value>", getTrigger()));
        }
    }

    private void logConfigurationValues() {
        for(final String key : this.appConfiguration.getKeys()) {
            final Optional<String> value = this.appConfiguration.get(key);

            log.info(formatMessage(key, value.get()));
        }
    }

    private String formatMessage(final String key, final String value) {
        return String.format("%s: %s", key, value);
    }

}
