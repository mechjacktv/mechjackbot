package com.mechjacktv.mechjackbot.chatbot;

import org.pircbotx.hooks.types.GenericMessageEvent;

import com.mechjacktv.mechjackbot.*;
import com.mechjacktv.util.ExecutionUtils;

public final class PircBotXMessageEvent implements MessageEvent {

  private static final String RESPONSE_MESSAGE_FORMAT_KEY = "chat_bot.message_event.message_format";
  private static final String RESPONSE_MESSAGE_FORMAT_DEFAULT = "/me MrDestructoid > %s";

  private final AppConfiguration appConfiguration;
  private final ExecutionUtils executionUtils;
  private final GenericMessageEvent genericMessageEvent;

  PircBotXMessageEvent(final AppConfiguration appConfiguration, final ExecutionUtils executionUtils,
      final GenericMessageEvent genericMessageEvent) {
    this.appConfiguration = appConfiguration;
    this.executionUtils = executionUtils;
    this.genericMessageEvent = genericMessageEvent;
  }

  @Override
  public ChatBot getChatBot() {
    return new PircBotXChatBot(this.appConfiguration, this.executionUtils, this.genericMessageEvent.getBot());
  }

  @Override
  public ChatUser getChatUser() {
    return new PircBotXChatUser(this.genericMessageEvent.getUser());
  }

  @Override
  public Message getMessage() {
    return Message.of(this.genericMessageEvent.getMessage());
  }

  @Override
  public void sendResponse(final Message message) {
    this.genericMessageEvent.respondWith(String.format(
        this.appConfiguration.get(RESPONSE_MESSAGE_FORMAT_KEY, RESPONSE_MESSAGE_FORMAT_DEFAULT), message.value));
  }

}
