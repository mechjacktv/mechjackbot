package tv.mechjack.twitchclient;

import tv.mechjack.util.typedobject.TypedString;

public final class TwitchLogin extends TypedString {

  public static TwitchLogin of(final String value) {
    String sanitizedValue = value.trim().toLowerCase();

    if (sanitizedValue.startsWith("@")) {
      sanitizedValue = sanitizedValue.substring(1);
    }
    return TypedString.of(TwitchLogin.class, sanitizedValue);
  }

  private TwitchLogin(final String value) {
    super(value);
  }

}
