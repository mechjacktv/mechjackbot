package com.mechjacktv.mechjackbot.command;

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

public abstract class SimpleCommand implements Command {

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
    if (!this.createCommandsFile()) {
      try (final FileInputStream castersFile = new FileInputStream(COMMANDS_LOCATION)) {
        this.commands.load(castersFile);
      }
    }
  }

  private boolean createCommandsFile() throws IOException {
    return new File(COMMANDS_LOCATION).createNewFile();
  }

  @Override
  public final String getTrigger() {
    return "!simple";
  }

  @Override
  public final boolean isHandledMessage(MessageEvent messageEvent) {
    final String message = messageEvent.getMessage();
    final Matcher messageMatcher = this.newCommandPattern.matcher(message);

    return messageMatcher.matches() && this.commandUtils.isPrivilegedViewer(messageEvent);
  }

  @Override
  public final void handleMessage(MessageEvent messageEvent) {
    final String message = messageEvent.getMessage();
    final Matcher messageMatcher = this.newCommandPattern.matcher(message);

    if (messageMatcher.matches() && this.commandUtils.isPrivilegedViewer(messageEvent)) {
      final String commandTrigger = messageMatcher.group(1);
      final String commandBody = messageMatcher.group(2);

      if (this.commandUtils.isGloballyCooledDown("!addcommand")) {
        this.setCommand(commandTrigger, commandBody);
        messageEvent.sendResponse(String.format("Added %s command", commandTrigger));
        try {
          this.commands.store(System.out, "");
        } catch (final IOException e) {
          e.printStackTrace();
        }
      }
    } else if (message.startsWith("!delcommand") && this.commandUtils.isPrivilegedViewer(messageEvent)) {
      if (this.commandUtils.isGloballyCooledDown("!delcommand")) {
        final String[] messageParts = message.split(" ");

        if (messageParts.length > 1 && this.commands.containsKey(messageParts[1])) {
          this.commands.remove(messageParts[1]);
          this.saveCommands();
          messageEvent.sendResponse(String.format("Removed %s command", messageParts[1]));
        }
      }
    } else if (message.startsWith("!comtest") && this.commandUtils.isPrivilegedViewer(messageEvent)) {
      final StringBuilder messageBuilder = new StringBuilder(String.format("Commands (%d): ", this.commands.size()));

      for (final Object key : this.commands.keySet()) {
        messageBuilder.append(key.toString()).append(" ");
      }
      messageEvent.sendResponse(messageBuilder.toString());
    } else {
      final String commandTrigger = this.parseCommandTrigger(message);

      if (this.commands.containsKey(commandTrigger)) {
        if (this.commandUtils.isGloballyCooledDown(commandTrigger)) {
          messageEvent.sendResponse(this.commands.getProperty(commandTrigger));
        }
      }
    }
  }

  private void setCommand(final String commandTrigger, final String commandBody) {
    this.commands.setProperty(commandTrigger, commandBody);
    this.saveCommands();
  }


  private void saveCommands() {
    try (final FileOutputStream castersFile = new FileOutputStream(COMMANDS_LOCATION)) {
      this.commands.store(castersFile, "");
    } catch (final IOException e) {
      e.printStackTrace();
    }
  }

  private String parseCommandTrigger(final String message) {
    if (!message.contains(" ")) {
      return message;
    }
    return message.substring(0, message.indexOf(" "));
  }

}
