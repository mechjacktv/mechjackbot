package com.mechjacktv.mechjackbot.command;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mechjacktv.mechjackbot.*;

@SuppressWarnings("CanBeFinal")
class InspectConfigCommand extends AbstractCommand {

    private static final Logger log = LoggerFactory.getLogger(InspectConfigCommand.class);

    private static final String COMMAND_SYNTAX_REGEX = "\\s+([\\w\\d.-_]+)\\s*";

    private final AppConfiguration appConfiguration;
    private final Pattern commandSyntaxPattern;
    private final CommandUtils commandUtils;

    @Inject
    InspectConfigCommand(final AppConfiguration appConfiguration, final CommandUtils commandUtils) {
        super(CommandTrigger.of("!inspectconfig"), commandUtils);
        this.appConfiguration = appConfiguration;
        this.commandSyntaxPattern = Pattern.compile(this.getTrigger() + COMMAND_SYNTAX_REGEX);
        this.commandUtils = commandUtils;
    }

    @Override
    public CommandDescription getDescription() {
        return CommandDescription.of("Reports the value for the configuration key specified.");
    }

    @Override
    @RestrictToOwner
    @GlobalCoolDown
    public void handleMessage(MessageEvent messageEvent) {
        final Message message = messageEvent.getMessage();
        final Matcher messageMatcher = this.commandSyntaxPattern.matcher(message.value);

        if (messageMatcher.matches()) {
            final String key = messageMatcher.group(1).trim().toLowerCase();

            if ("all".equals(key)) {
                this.logConfigurationValues();
                messageEvent.sendResponse(Message.of("Dumped all configuration values to the log"));
            } else {
                final Optional<String> value = this.appConfiguration.get(key);

                if (value.isPresent()) {
                    messageEvent.sendResponse(Message.of(this.formatMessage(key, value.get())));
                } else {
                    messageEvent.sendResponse(Message.of(String.format("No value was found for %s", key)));
                }
            }
        } else {
            this.commandUtils.sendUsage(messageEvent, CommandUsage.of(String.format("%s <key> = <value>", this.getTrigger())));
        }
    }

    private void logConfigurationValues() {
        for (final String key : this.appConfiguration.getKeys()) {
            final Optional<String> value = this.appConfiguration.get(key);

            log.info(this.formatMessage(key, value.orElse("<<missing>>")));
        }
    }

    private String formatMessage(final String key, final String value) {
        return String.format("%s: %s", key, value);
    }

}
