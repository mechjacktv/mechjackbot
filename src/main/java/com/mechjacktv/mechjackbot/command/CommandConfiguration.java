package com.mechjacktv.mechjackbot.command;

import com.mechjacktv.configuration.Configuration;
import com.mechjacktv.mechjackbot.ChatCommandDescription;
import com.mechjacktv.mechjackbot.ChatCommandTrigger;
import com.mechjacktv.mechjackbot.ChatCommandUsage;
import com.mechjacktv.mechjackbot.ChatCommandUtils;
import com.mechjacktv.util.ExecutionUtils;

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
