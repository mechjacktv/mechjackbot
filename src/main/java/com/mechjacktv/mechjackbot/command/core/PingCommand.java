package com.mechjacktv.mechjackbot.command.core;

import javax.inject.Inject;

import com.mechjacktv.mechjackbot.*;
import com.mechjacktv.mechjackbot.command.BaseCommand;
import com.mechjacktv.mechjackbot.command.CommandConfigurationBuilder;

public final class PingCommand extends BaseCommand {

  public static final String DEFAULT_DESCRIPTION = "A simple check to see if the chat bot is running.";
  public static final String DEFAULT_MESSAGE_FORMAT = "Don't worry, @%s. I'm here.";
  public static final String DEFAULT_TRIGGER = "!ping";

  @Inject
  PingCommand(final CommandConfigurationBuilder commandConfigurationBuilder) {
    super(commandConfigurationBuilder.setTrigger(DEFAULT_TRIGGER)
        .setDescription(DEFAULT_DESCRIPTION)
        .setMessageFormat(DEFAULT_MESSAGE_FORMAT));
  }

  @Override
  @RequiresAccessLevel(AccessLevel.MODERATOR)
  public void handleMessageEvent(MessageEvent messageEvent) {
    this.sendResponse(messageEvent);
  }

}
