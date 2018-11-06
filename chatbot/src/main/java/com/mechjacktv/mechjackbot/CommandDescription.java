package com.mechjacktv.mechjackbot;

import com.mechjacktv.typedobject.TypedString;

public final class CommandDescription extends TypedString {

    private CommandDescription(final String value) {
        super(value);
    }

    public static CommandDescription of(final String value) {
        return new CommandDescription(value);
    }

}
