package com.mechjacktv.mechjackbot.command.core;

import javax.inject.Inject;

import com.mechjacktv.mechjackbot.*;
import com.mechjacktv.mechjackbot.command.BaseChatCommand;
import com.mechjacktv.mechjackbot.command.CommandConfigurationBuilder;

public final class PingChatCommand extends BaseChatCommand {

  public static final String DEFAULT_DESCRIPTION = "A simple check to see if the chat bot is running.";
  public static final String DEFAULT_MESSAGE_FORMAT = "Don't worry, @%s. I'm here.";
  public static final String DEFAULT_TRIGGER = "!ping";

  @Inject
  PingChatCommand(final CommandConfigurationBuilder commandConfigurationBuilder) {
    super(commandConfigurationBuilder.setTrigger(DEFAULT_TRIGGER)
        .setDescription(DEFAULT_DESCRIPTION)
        .setMessageFormat(DEFAULT_MESSAGE_FORMAT));
  }

  @Override
  @RequiresUserRole(UserRole.MODERATOR)
  public void handleMessageEvent(ChatMessageEvent chatMessageEvent) {
    this.sendResponse(chatMessageEvent);
  }

}
