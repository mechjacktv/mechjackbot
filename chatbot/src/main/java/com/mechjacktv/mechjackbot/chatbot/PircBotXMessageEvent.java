package com.mechjacktv.mechjackbot.chatbot;

import org.pircbotx.hooks.types.GenericMessageEvent;

import com.mechjacktv.mechjackbot.ChatBot;
import com.mechjacktv.mechjackbot.ChatUser;
import com.mechjacktv.mechjackbot.Message;
import com.mechjacktv.mechjackbot.MessageEvent;
import com.mechjacktv.util.ExecutionUtils;

public final class PircBotXMessageEvent implements MessageEvent {

  private final ExecutionUtils executionUtils;
  private final GenericMessageEvent genericMessageEvent;

  PircBotXMessageEvent(final ExecutionUtils executionUtils, final GenericMessageEvent genericMessageEvent) {
    this.executionUtils = executionUtils;
    this.genericMessageEvent = genericMessageEvent;
  }

  @Override
  public ChatBot getChatBot() {
    return new PircBotXChatBot(this.executionUtils, this.genericMessageEvent.getBot());
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
    this.genericMessageEvent.respondWith(String.format("/me MrDestructoid > %s ", message.value));
  }

}
