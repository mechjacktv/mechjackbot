package com.mechjacktv.mechjackbot.chatbot;

final class PircBotXStartupException extends PircBotXException {

  private static final long serialVersionUID = -5027414115096871357L;

  public PircBotXStartupException(final Throwable cause) {
    this(cause.getMessage(), cause);
  }

  public PircBotXStartupException(final String message, final Throwable cause) {
    super(message, cause);
  }

}