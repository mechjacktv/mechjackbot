package com.mechjacktv.mechjackbot.chatbot;


class PircBotXException extends RuntimeException {

  private static final long serialVersionUID = -2526517027015909574L;

  PircBotXException(final Throwable cause) {
    super("Failed to connect to chat", cause);
  }

}
