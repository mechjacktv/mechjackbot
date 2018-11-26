package com.mechjacktv.mechjackbot;

public enum ViewerRole {

  OWNER(AccessLevel.OWNER),
  MODERATOR(AccessLevel.MODERATOR),
  SUBSCRIBER_3(AccessLevel.SUBSCRIBER),
  SUBSCRIBER_2(AccessLevel.SUBSCRIBER),
  SUBSCRIBER_1(AccessLevel.SUBSCRIBER),
  VIP(AccessLevel.SUBSCRIBER),
  FOLLOWER(AccessLevel.FOLLOWER),
  VIEWER(AccessLevel.EVERYONE);

  private final AccessLevel accessLevel;

  ViewerRole(final AccessLevel accessLevel) {
    this.accessLevel = accessLevel;
  }

  public final AccessLevel getAccessLevel() {
    return this.accessLevel;
  }

}
