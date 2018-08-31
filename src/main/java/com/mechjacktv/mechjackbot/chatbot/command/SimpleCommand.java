package com.mechjacktv.mechjackbot.chatbot.command;

import com.mechjacktv.mechjackbot.Command;
import com.mechjacktv.mechjackbot.MessageEvent;

import javax.inject.Inject;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class SimpleCommand implements Command {

    private static final String COMMANDS_LOCATION = System.getProperty("user.home") + "/.mechjackbot_commands.config";
    private static final String NEW_COMMAND_PATTERN = "\\!addcommand\\s+(\\S+)\\s+(.+)";

    private final CommandUtils commandUtils;
    private final Pattern newCommandPattern;
    private final Properties commands;

    @Inject
    public SimpleCommand(final CommandUtils commandUtils) throws IOException {
        this.commandUtils = commandUtils;
        this.newCommandPattern = Pattern.compile(NEW_COMMAND_PATTERN);
        this.commands = new Properties();
        if (!createCastersFile()) {
            try (final FileInputStream castersFile = new FileInputStream(COMMANDS_LOCATION)) {
                this.commands.load(castersFile);
            }
        }
    }

    private final boolean createCastersFile() throws IOException {
        return new File(COMMANDS_LOCATION).createNewFile();
    }

    @Override
    public final boolean handleMessage(MessageEvent messageEvent) {
        final String message = messageEvent.getMessage();
        final Matcher messageMatcher = this.newCommandPattern.matcher(message);

        if (messageMatcher.matches() && commandUtils.privilegedUser(messageEvent)) {
            final String commandTrigger = messageMatcher.group(1);
            final String commandBody = messageMatcher.group(2);

            if (commandUtils.isCooleddown("!addcommand")) {
                setCommand(commandTrigger, commandBody);
                messageEvent.respond(String.format("Added %s command", commandTrigger));
                try {
                    commands.store(System.out, "");
                } catch (final IOException e) {
                    e.printStackTrace();
                }
            }
            return true;
        } else if (message.startsWith("!delcommand") && commandUtils.privilegedUser(messageEvent)) {
            if (commandUtils.isCooleddown("!delcommand")) {
                final String[] messageParts = message.split(" ");

                if (messageParts.length > 1 && commands.containsKey(messageParts[1])) {
                    commands.remove(messageParts[1]);
                    saveCommands();
                    messageEvent.respond(String.format("Removed %s command", messageParts[1]));
                }
            }
            return true;
        } else if (message.startsWith("!comtest") && commandUtils.privilegedUser(messageEvent)) {
            final StringBuilder messageBuilder = new StringBuilder(String.format("Commands (%d): ", commands.size()));

            for(final Object key : commands.keySet()) {
                messageBuilder.append(key.toString()).append(" ");
            }
            messageEvent.respond(messageBuilder.toString());
            return true;
        } else {
            final String commandTrigger = parseCommandTrigger(message);

            if (commands.containsKey(commandTrigger)) {
                if (commandUtils.isCooleddown(commandTrigger)) {
                    messageEvent.respond(commands.getProperty(commandTrigger));
                }
                return true;
            }
        }
        return false;
    }

    private final void setCommand(final String commandTrigger, final String commandBody) {
        commands.setProperty(commandTrigger, commandBody);
        saveCommands();
    }


    private final void saveCommands() {
        try (final FileOutputStream castersFile = new FileOutputStream(COMMANDS_LOCATION)) {
            commands.store(castersFile, "");
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    private final String parseCommandTrigger(final String message) {
        if (message.indexOf(" ") == -1) {
            return message;
        }
        return message.substring(0, message.indexOf(" "));
    }

}
