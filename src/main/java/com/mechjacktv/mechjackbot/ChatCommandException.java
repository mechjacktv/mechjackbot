package com.mechjacktv.mechjackbot;

public class ChatCommandException extends RuntimeException {

  private static final long serialVersionUID = 8227397365457524947L;

  public ChatCommandException(final String message) {
    super(message);
  }

  public ChatCommandException(final String message, final Throwable cause) {
    super(message, cause);
  }

}
