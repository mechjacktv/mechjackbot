package com.mechjacktv.mechjackbot.chatbot;

import java.util.Objects;

import org.pircbotx.PircBotX;
import org.pircbotx.User;
import org.pircbotx.hooks.types.GenericMessageEvent;

import com.mechjacktv.configuration.Configuration;
import com.mechjacktv.mechjackbot.ChatBot;
import com.mechjacktv.mechjackbot.ChatBotConfiguration;
import com.mechjacktv.mechjackbot.ChatUser;
import com.mechjacktv.mechjackbot.CommandUtils;
import com.mechjacktv.mechjackbot.Message;
import com.mechjacktv.mechjackbot.MessageEvent;
import com.mechjacktv.util.ExecutionUtils;

public final class PircBotXMessageEvent implements MessageEvent {

  static final String RESPONSE_MESSAGE_FORMAT_KEY = PircBotXChatBot.CHAT_BOT_MESSAGE_FORMAT_KEY;
  static final String RESPONSE_MESSAGE_FORMAT_DEFAULT = PircBotXChatBot.CHAT_BOT_MESSAGE_FORMAT_DEFAULT;

  private final Configuration configuration;
  private final ChatBotConfiguration chatBotConfiguration;
  private final ChatBotFactory<PircBotX> chatBotFactory;
  private final ChatUserFactory<User> chatUserFactory;
  private final CommandUtils commandUtils;
  private final ExecutionUtils executionUtils;
  private final GenericMessageEvent genericMessageEvent;

  PircBotXMessageEvent(final Configuration configuration, final ChatBotConfiguration chatBotConfiguration,
      final ChatBotFactory<PircBotX> chatBotFactory, final ChatUserFactory<User> chatUserFactory,
      final CommandUtils commandUtils, final ExecutionUtils executionUtils,
      final GenericMessageEvent genericMessageEvent) {
    this.configuration = configuration;
    this.chatBotConfiguration = chatBotConfiguration;
    this.chatBotFactory = chatBotFactory;
    this.chatUserFactory = chatUserFactory;
    this.commandUtils = commandUtils;
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
  public Message getMessage() {
    return Message.of(this.genericMessageEvent.getMessage());
  }

  @Override
  public void sendResponse(final Message message) {
    Objects.requireNonNull(message, this.executionUtils.nullMessageForName("message"));
    this.genericMessageEvent.respondWith(String.format(
        this.configuration.get(RESPONSE_MESSAGE_FORMAT_KEY, RESPONSE_MESSAGE_FORMAT_DEFAULT), message.value));
  }

}
