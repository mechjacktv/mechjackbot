package com.mechjacktv.mechjackbot.chatbot.kicl;

import java.util.Objects;

import com.mechjacktv.configuration.Configuration;
import com.mechjacktv.mechjackbot.ChatBot;
import com.mechjacktv.mechjackbot.ChatMessage;
import com.mechjacktv.mechjackbot.ChatMessageEvent;
import com.mechjacktv.mechjackbot.ChatUser;
import com.mechjacktv.mechjackbot.chatbot.ChatUserFactory;
import com.mechjacktv.util.ExecutionUtils;

import org.kitteh.irc.client.library.element.User;
import org.kitteh.irc.client.library.event.channel.ChannelMessageEvent;

public class KiclChatMessageEvent implements ChatMessageEvent {

  public static final String DEFAULT_RESPONSE_MESSAGE_FORMAT = KiclChatBot.DEFAULT_CHAT_BOT_MESSAGE_FORMAT;
  public static final String KEY_RESPONSE_MESSAGE_FORMAT = KiclChatBot.KEY_CHAT_BOT_MESSAGE_FORMAT;

  private final ChannelMessageEvent channelMessageEvent;
  private final ChatBot chatBot;
  private final ChatUserFactory<User> chatUserFactory;
  private final Configuration configuration;
  private final ExecutionUtils executionUtils;

  KiclChatMessageEvent(final ChannelMessageEvent channelMessageEvent, final ChatBot chatBot,
      final ChatUserFactory<User> chatUserFactory, final Configuration configuration,
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
    return this.chatUserFactory.create(this.channelMessageEvent.getActor());
  }

  @Override
  public ChatMessage getChatMessage() {
    return ChatMessage.of(this.channelMessageEvent.getMessage());
  }

  @Override
  public void sendResponse(final ChatMessage chatMessage) {
    Objects.requireNonNull(chatMessage, this.executionUtils.nullMessageForName("chatMessage"));
    this.channelMessageEvent.sendReply(String.format(
        this.configuration.get(KEY_RESPONSE_MESSAGE_FORMAT, DEFAULT_RESPONSE_MESSAGE_FORMAT), chatMessage.value));
  }

}
