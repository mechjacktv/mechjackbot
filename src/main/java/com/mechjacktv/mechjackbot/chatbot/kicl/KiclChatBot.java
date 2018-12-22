package com.mechjacktv.mechjackbot.chatbot.kicl;

import java.util.Objects;

import javax.inject.Inject;

import com.mechjacktv.mechjackbot.ChatBot;
import com.mechjacktv.mechjackbot.ChatChannel;
import com.mechjacktv.mechjackbot.ChatMessage;
import com.mechjacktv.util.ExecutionUtils;

public class KiclChatBot implements ChatBot {

  private final ExecutionUtils executionUtils;

  @Inject
  KiclChatBot(final ExecutionUtils executionUtils) {
    this.executionUtils = executionUtils;
  }

  @Override
  public void sendMessage(final ChatChannel chatChannel, final ChatMessage chatMessage) {
    Objects.requireNonNull(chatChannel, this.executionUtils.nullMessageForName("chatChannel"));
    Objects.requireNonNull(chatMessage, this.executionUtils.nullMessageForName("chatMessage"));
  }

  @Override
  public void start() {

  }

  @Override
  public void stop() {

  }

}
