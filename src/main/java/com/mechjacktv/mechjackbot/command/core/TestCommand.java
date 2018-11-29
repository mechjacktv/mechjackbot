package com.mechjacktv.mechjackbot.command.core;

import javax.inject.Inject;

import com.mechjacktv.mechjackbot.AccessLevel;
import com.mechjacktv.mechjackbot.MessageEvent;
import com.mechjacktv.mechjackbot.RestrictToAccessLevel;
import com.mechjacktv.mechjackbot.command.BaseCommand;
import com.mechjacktv.mechjackbot.command.CommandConfigurationBuilder;

public final class TestCommand extends BaseCommand {

  @Inject
  protected TestCommand(final CommandConfigurationBuilder commandConfigurationBuilder) {
    super(commandConfigurationBuilder.setTrigger("!test")
        .setDescription("This is a test command.")
        .setMessageFormat("key: %2$s, default: %3$s, value: %4$s"));
  }

  @Override
  @RestrictToAccessLevel(AccessLevel.OWNER)
  public void handleMessageEvent(final MessageEvent messageEvent) {
    this.sendResponse(messageEvent, this.getTriggerKey(), this.getTriggerDefault(), this.getTrigger());
  }

}
