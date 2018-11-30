package com.mechjacktv.mechjackbot.command.core;

import javax.inject.Inject;

import com.mechjacktv.mechjackbot.*;
import com.mechjacktv.mechjackbot.command.BaseCommand;
import com.mechjacktv.mechjackbot.command.CommandConfigurationBuilder;

public final class PingCommand extends BaseCommand {

  public static final String DESCRIPTION_DEFAULT = "A simple check to see if the chat bot is running.";
  public static final String MESSAGE_FORMAT_DEFAULT = "Don't worry, @%s. I'm here.";
  public static final String TRIGGER_DEFAULT = "!ping";

  @Inject
  protected PingCommand(final CommandConfigurationBuilder commandConfigurationBuilder) {
    super(commandConfigurationBuilder.setTrigger(TRIGGER_DEFAULT)
        .setDescription(DESCRIPTION_DEFAULT)
        .setMessageFormat(MESSAGE_FORMAT_DEFAULT));
  }

  @Override
  @RestrictToAccessLevel(AccessLevel.MODERATOR)
  public void handleMessageEvent(MessageEvent messageEvent) {
    this.sendResponse(messageEvent);
  }

}
