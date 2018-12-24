package com.mechjacktv.mechjackbot.command.custom;

import com.mechjacktv.mechjackbot.ChatCommandException;

public final class UsageException extends ChatCommandException {

  private static final long serialVersionUID = -5205822143278258132L;

  public UsageException(final String message) {
    super(message);
  }

  public UsageException(final String message, final Throwable cause) {
    super(message, cause);
  }

}
