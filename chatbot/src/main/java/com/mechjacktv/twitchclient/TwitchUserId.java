package com.mechjacktv.twitchclient;

import com.mechjacktv.typedobject.TypedString;

public final class TwitchUserId extends TypedString {

    private TwitchUserId(final String value) {
        super(value);
    }

    public static TwitchUserId of(final String value) {
        return new TwitchUserId(value);
    }

}
