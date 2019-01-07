package tv.mechjack.mechjackbot.command.core;

import javax.inject.Inject;

import tv.mechjack.mechjackbot.ChatMessageEvent;
import tv.mechjack.mechjackbot.RequiresUserRole;
import tv.mechjack.mechjackbot.UserRole;
import tv.mechjack.mechjackbot.command.BaseChatCommand;
import tv.mechjack.mechjackbot.command.CommandConfigurationBuilder;

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
  @RequiresUserRole(UserRole.MODERATOR)
  public void handleMessageEvent(ChatMessageEvent chatMessageEvent) {
    this.sendResponse(chatMessageEvent);
  }

}
