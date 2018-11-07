package com.mechjacktv.twitchclient;

import com.mechjacktv.util.typedobject.TypedString;

public final class TwitchUserFollowsCursor extends TypedString {

  private TwitchUserFollowsCursor(final String value) {
    super(value);
  }

  public static TwitchUserFollowsCursor of(final String value) {
    return new TwitchUserFollowsCursor(value);
  }

}
