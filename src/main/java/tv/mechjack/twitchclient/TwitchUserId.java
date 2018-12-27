package tv.mechjack.twitchclient;

import tv.mechjack.util.typedobject.TypedString;

public final class TwitchUserId extends TypedString {

  public static TwitchUserId of(final String value) {
    return TypedString.of(TwitchUserId.class, value);
  }

  private TwitchUserId(final String value) {
    super(value);
  }

}
