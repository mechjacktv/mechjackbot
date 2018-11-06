package com.mechjacktv.twitchclient;

import com.mechjacktv.typedobject.TypedString;

public final class TwitchLogin extends TypedString {

    private TwitchLogin(final String value) {
        super(value);
    }

    public static TwitchLogin of(final String value) {
        return new TwitchLogin(value);
    }

}
