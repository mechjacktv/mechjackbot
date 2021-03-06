package tv.mechjack.mechjackbot.chatbot.kicl;

import java.util.Objects;

import org.kitteh.irc.client.library.event.channel.ChannelMessageEvent;

import tv.mechjack.mechjackbot.api.ChatBot;
import tv.mechjack.mechjackbot.api.ChatMessage;
import tv.mechjack.mechjackbot.api.ChatMessageEvent;
import tv.mechjack.mechjackbot.api.ChatUser;
import tv.mechjack.platform.configuration.Configuration;
import tv.mechjack.platform.utils.ExecutionUtils;

public class KiclChatMessageEvent implements ChatMessageEvent {

  public static final String DEFAULT_RESPONSE_MESSAGE_FORMAT = KiclChatBot.DEFAULT_CHAT_BOT_MESSAGE_FORMAT;
  public static final String KEY_RESPONSE_MESSAGE_FORMAT = KiclChatBot.KEY_CHAT_BOT_MESSAGE_FORMAT;

  private final ChannelMessageEvent channelMessageEvent;
  private final ChatBot chatBot;
  private final KiclChatUserFactory chatUserFactory;
  private final Configuration configuration;
  private final ExecutionUtils executionUtils;

  KiclChatMessageEvent(final ChannelMessageEvent channelMessageEvent, final ChatBot chatBot,
      final KiclChatUserFactory chatUserFactory, final Configuration configuration,
      final ExecutionUtils executionUtils) {
    this.channelMessageEvent = channelMessageEvent;
    this.chatBot = chatBot;
    this.chatUserFactory = chatUserFactory;
    this.configuration = configuration;
    this.executionUtils = executionUtils;
  }

  @Override
  public ChatBot getChatBot() {
    return this.chatBot;
  }

  @Override
  public ChatUser getChatUser() {
    return this.chatUserFactory.create(this.channelMessageEvent);
  }

  @Override
  public ChatMessage getChatMessage() {
    return ChatMessage.of(this.channelMessageEvent.getMessage());
  }

  @Override
  public void sendResponse(final ChatMessage chatMessage) {
    Objects.requireNonNull(chatMessage, this.executionUtils.nullMessageForName("chatMessage"));
    this.channelMessageEvent.sendReply(String.format(
        this.configuration.get(KEY_RESPONSE_MESSAGE_FORMAT, DEFAULT_RESPONSE_MESSAGE_FORMAT), chatMessage));
  }

  @Override
  public void sendRawResponse(final ChatMessage chatMessage) {
    Objects.requireNonNull(chatMessage, this.executionUtils.nullMessageForName("chatMessage"));
    this.channelMessageEvent.sendReply(chatMessage.value);
  }

}
