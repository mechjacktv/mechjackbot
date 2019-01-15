package tv.mechjack.mechjackbot.api;

import tv.mechjack.platform.configuration.Configuration;
import tv.mechjack.platform.util.ExecutionUtils;

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
