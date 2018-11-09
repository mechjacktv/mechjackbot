package com.mechjacktv.mechjackbot.chatbot;

class PircBotXException extends RuntimeException {

  private static final long serialVersionUID = -2526517027015909574L;

  PircBotXException(final Throwable cause) {
    this(cause.getMessage(), cause);
  }

  PircBotXException(final String message, final Throwable cause) {
    super(message, cause);
  }

}
