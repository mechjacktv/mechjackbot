package tv.mechjack.twitchclient;

import tv.mechjack.platform.utils.typedobject.TypedString;

public final class TwitchClientId extends TypedString {

  public static TwitchClientId of(final String value) {
    return TypedString.of(TwitchClientId.class, value);
  }

  private TwitchClientId(final String value) {
    super(value);
  }

}
