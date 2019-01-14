package tv.mechjack.mechjackbot.command.shoutout;

import javax.inject.Inject;

import com.google.common.base.Strings;

import tv.mechjack.mechjackbot.api.BaseChatCommand;
import tv.mechjack.mechjackbot.api.ChatCommandUtils;
import tv.mechjack.mechjackbot.api.ChatMessage;
import tv.mechjack.mechjackbot.api.ChatMessageEvent;
import tv.mechjack.mechjackbot.api.CommandConfigurationBuilder;
import tv.mechjack.twitchclient.TwitchLogin;

public final class ShoutOutChatCommand extends BaseChatCommand {

  public static final String DEFAULT_DESCRIPTION = "Shout out the specified caster.";
  public static final String DEFAULT_MESSAGE_FORMAT = "Fellow streamer in chat! Everyone, please give a warm welcome "
      + "to %1$s. It would be great if you checked them out and gave them a follow. https://twitch.tv/%1$s";
  public static final String DEFAULT_TRIGGER = "!so";
  public static final String USAGE = "<casterName>";

  private final ChatCommandUtils chatCommandUtils;

  @Inject
  ShoutOutChatCommand(final CommandConfigurationBuilder commandConfigurationBuilder,
      final ChatCommandUtils chatCommandUtils) {
    super(commandConfigurationBuilder.setTrigger(DEFAULT_TRIGGER)
        .setDescription(DEFAULT_DESCRIPTION)
        .setMessageFormat(DEFAULT_MESSAGE_FORMAT)
        .setUsage(USAGE));
    this.chatCommandUtils = chatCommandUtils;
  }

  @Override
  public void handleMessageEvent(final ChatMessageEvent chatMessageEvent) {
    final ChatMessage chatMessage = this.chatCommandUtils.stripTriggerFromMessage(this, chatMessageEvent);

    if (!Strings.isNullOrEmpty(chatMessage.value)) {
      this.sendResponse(chatMessageEvent, TwitchLogin.of(chatMessage.value));
    } else {
      this.sendUsage(chatMessageEvent);
    }
  }

}