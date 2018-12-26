package com.mechjacktv.mechjackbot;

public enum UserRole {

  BROADCASTER(1),
  MODERATOR(2),
  VIP(3),
  SUBSCRIBER(4),
  VIEWER(5);

  private final Integer value;

  UserRole(final Integer value) {
    this.value = value;
  }

  public final Integer value() {
    return this.value;
  }

}
