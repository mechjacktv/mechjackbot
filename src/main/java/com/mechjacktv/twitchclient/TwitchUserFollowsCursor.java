package com.mechjacktv.twitchclient;

import com.mechjacktv.util.typedobject.TypedString;

public final class TwitchUserFollowsCursor extends TypedString {

  public static TwitchUserFollowsCursor of(final String value) {
    return TypedString.of(TwitchUserFollowsCursor.class, value);
  }

  private TwitchUserFollowsCursor(final String value) {
    super(value);
  }

}
