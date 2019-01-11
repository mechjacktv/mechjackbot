package tv.mechjack.mechjackbot.api;

import tv.mechjack.util.typedobject.TypedString;

public final class UserPassword extends TypedString {

  public static UserPassword of(final String value) {
    return TypedString.of(UserPassword.class, value);
  }

  private UserPassword(final String value) {
    super(value);
  }

}
