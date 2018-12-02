package com.mechjacktv.mechjackbot.command.core;

import javax.inject.Inject;

import com.mechjacktv.mechjackbot.AccessLevel;
import com.mechjacktv.mechjackbot.MessageEvent;
import com.mechjacktv.mechjackbot.RestrictToAccessLevel;
import com.mechjacktv.mechjackbot.command.BaseCommand;
import com.mechjacktv.mechjackbot.command.CommandConfigurationBuilder;

public final class TestCommand extends BaseCommand {

  public static final String DEFAULT_DESCRIPTION = "This is a test command.";
  public static final String DEFAULT_MESSAGE_FORMAT = "%2$s";
  public static final String DEFAULT_TRIGGER = "!test";

  @Inject
  protected TestCommand(final CommandConfigurationBuilder commandConfigurationBuilder) {
    super(commandConfigurationBuilder.setDescription(DEFAULT_DESCRIPTION)
        .setMessageFormat(DEFAULT_MESSAGE_FORMAT)
        .setTrigger(DEFAULT_TRIGGER));
  }

  @Override
  @RestrictToAccessLevel(AccessLevel.MODERATOR)
  public void handleMessageEvent(final MessageEvent messageEvent) {
    this.sendResponse(messageEvent, this.getTrigger());
  }

}
