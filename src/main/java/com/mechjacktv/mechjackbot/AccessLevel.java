package com.mechjacktv.mechjackbot;

public enum AccessLevel {

  OWNER(1),
  MODERATOR(2),
  SUBSCRIBER(3),
  FOLLOWER(4),
  EVERYONE(5);

  private final Integer value;

  AccessLevel(final Integer value) {
    this.value = value;
  }

  public final Integer value() {
    return this.value;
  }

}
