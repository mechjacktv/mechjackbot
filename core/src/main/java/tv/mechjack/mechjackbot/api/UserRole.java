package tv.mechjack.mechjackbot.api;

public enum UserRole {

  BROADCASTER(1),
  MODERATOR(2),
  VIP(3),
  SUBSCRIBER(4),
  VIEWER(5);

  private final Integer accessLevel;

  UserRole(final Integer accessLevel) {
    this.accessLevel = accessLevel;
  }

  public final Integer accessLevel() {
    return this.accessLevel;
  }

}
