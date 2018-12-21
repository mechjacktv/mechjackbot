package com.mechjacktv.mechjackbot.command.custom;

import com.mechjacktv.mechjackbot.CommandException;

public final class UsageException extends CommandException {

  private static final long serialVersionUID = -5205822143278258132L;

  public UsageException(final String message) {
    super(message);
  }

}
