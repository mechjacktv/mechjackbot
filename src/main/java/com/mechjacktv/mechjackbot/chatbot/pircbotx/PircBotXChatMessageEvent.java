package com.mechjacktv.mechjackbot.chatbot.pircbotx;

import java.util.Objects;

import org.pircbotx.PircBotX;
import org.pircbotx.User;
import org.pircbotx.hooks.types.GenericMessageEvent;

import com.mechjacktv.configuration.Configuration;
import com.mechjacktv.mechjackbot.ChatBot;
import com.mechjacktv.mechjackbot.ChatMessage;
import com.mechjacktv.mechjackbot.ChatMessageEvent;
import com.mechjacktv.mechjackbot.ChatUser;
import com.mechjacktv.mechjackbot.chatbot.ChatBotFactory;
import com.mechjacktv.mechjackbot.chatbot.ChatUserFactory;
import com.mechjacktv.util.ExecutionUtils;

public final class PircBotXChatMessageEvent implements ChatMessageEvent {

  static final String RESPONSE_MESSAGE_FORMAT_KEY = PircBotXChatBot.CHAT_BOT_MESSAGE_FORMAT_KEY;
  static final String RESPONSE_MESSAGE_FORMAT_DEFAULT = PircBotXChatBot.CHAT_BOT_MESSAGE_FORMAT_DEFAULT;

  private final Configuration configuration;
  private final ChatBotFactory<PircBotX> chatBotFactory;
  private final ChatUserFactory<User> chatUserFactory;
  private final ExecutionUtils executionUtils;
  private final GenericMessageEvent genericMessageEvent;

  PircBotXChatMessageEvent(final Configuration configuration, final ChatBotFactory<PircBotX> chatBotFactory,
      final ChatUserFactory<User> chatUserFactory, final ExecutionUtils executionUtils,
      final GenericMessageEvent genericMessageEvent) {
    this.configuration = configuration;
    this.chatBotFactory = chatBotFactory;
    this.chatUserFactory = chatUserFactory;
    this.executionUtils = executionUtils;
    this.genericMessageEvent = genericMessageEvent;
  }

  @Override
  public ChatBot getChatBot() {
    return this.chatBotFactory.create(this.genericMessageEvent.getBot());
  }

  @Override
  public ChatUser getChatUser() {
    return this.chatUserFactory.create(this.genericMessageEvent.getUser());
  }

  @Override
  public ChatMessage getChatMessage() {
    return ChatMessage.of(this.genericMessageEvent.getMessage());
  }

  @Override
  public void sendResponse(final ChatMessage chatMessage) {
    Objects.requireNonNull(chatMessage, this.executionUtils.nullMessageForName("chatMessage"));
    this.genericMessageEvent.respondWith(String.format(
        this.configuration.get(RESPONSE_MESSAGE_FORMAT_KEY, RESPONSE_MESSAGE_FORMAT_DEFAULT), chatMessage.value));
  }

}
