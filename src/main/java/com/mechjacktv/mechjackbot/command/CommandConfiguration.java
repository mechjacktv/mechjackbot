package com.mechjacktv.mechjackbot.command;

import com.mechjacktv.configuration.Configuration;
import com.mechjacktv.mechjackbot.CommandDescription;
import com.mechjacktv.mechjackbot.CommandTrigger;
import com.mechjacktv.mechjackbot.CommandUsage;
import com.mechjacktv.mechjackbot.CommandUtils;
import com.mechjacktv.util.ExecutionUtils;

public interface CommandConfiguration {

  CommandUtils getCommandUtils();

  Configuration getConfiguration();

  ExecutionUtils getExecutionUtils();

  CommandDescription getDescription();

  CommandMessageFormat getMessageFormat();

  CommandTrigger getTrigger();

  boolean isTriggerable();

  CommandUsage getUsage();

}
