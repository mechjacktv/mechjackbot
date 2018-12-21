package com.mechjacktv.mechjackbot;

public class CommandException extends RuntimeException {

  private static final long serialVersionUID = 8227397365457524947L;

  public CommandException(final String message) {
    super(message);
  }

  public CommandException(final String message, final Throwable cause) {
    super(message, cause);
  }

}
