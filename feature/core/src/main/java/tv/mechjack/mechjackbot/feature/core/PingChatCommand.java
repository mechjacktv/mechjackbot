package tv.mechjack.mechjackbot.feature.core;

import javax.inject.Inject;

import tv.mechjack.mechjackbot.api.BaseChatCommand;
import tv.mechjack.mechjackbot.api.ChatMessageEvent;
import tv.mechjack.mechjackbot.api.CommandConfigurationBuilder;
import tv.mechjack.mechjackbot.api.RequiresAccessLevel;
import tv.mechjack.mechjackbot.api.UserRole;

public final class PingChatCommand extends BaseChatCommand {

  public static final String DEFAULT_DESCRIPTION = "A simple check to see if the chat bot is running.";
  public static final String DEFAULT_MESSAGE_FORMAT = "Don't worry, @$(user). I'm here.";
  public static final String DEFAULT_TRIGGER = "!ping";

  @Inject
  PingChatCommand(final CommandConfigurationBuilder commandConfigurationBuilder) {
    super(commandConfigurationBuilder.setTrigger(DEFAULT_TRIGGER)
        .setDescription(DEFAULT_DESCRIPTION)
        .setMessageFormat(DEFAULT_MESSAGE_FORMAT));
  }

  @Override
  @RequiresAccessLevel(UserRole.MODERATOR)
  public void handleMessageEvent(ChatMessageEvent chatMessageEvent) {
    this.sendResponse(chatMessageEvent);
  }

}
