package tv.mechjack.mechjackbot.command;

import tv.mechjack.configuration.Configuration;
import tv.mechjack.mechjackbot.ChatCommandDescription;
import tv.mechjack.mechjackbot.ChatCommandTrigger;
import tv.mechjack.mechjackbot.ChatCommandUsage;
import tv.mechjack.mechjackbot.ChatCommandUtils;
import tv.mechjack.util.ExecutionUtils;

public interface CommandConfiguration {

  ChatCommandUtils getChatCommandUtils();

  Configuration getConfiguration();

  ExecutionUtils getExecutionUtils();

  ChatCommandDescription getDescription();

  CommandMessageFormat getMessageFormat();

  ChatCommandTrigger getTrigger();

  boolean isTriggerable();

  ChatCommandUsage getUsage();

}
