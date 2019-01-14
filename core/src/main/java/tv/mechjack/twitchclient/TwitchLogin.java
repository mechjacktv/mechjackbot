package tv.mechjack.twitchclient;

import tv.mechjack.util.typedobject.TypedString;

public final class TwitchLogin extends TypedString {

  public static TwitchLogin of(final String value) {
    String sanitizedValue = value.trim().toLowerCase();

    // TODO (2019-01-13 mechjack): Validate no illegal characters?
    if (sanitizedValue.startsWith("@")) {
      sanitizedValue = sanitizedValue.substring(1);
    }
    return TypedString.of(TwitchLogin.class, sanitizedValue);
  }

  private TwitchLogin(final String value) {
    super(value);
  }

}
