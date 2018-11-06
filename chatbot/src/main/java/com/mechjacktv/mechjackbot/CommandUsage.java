package com.mechjacktv.mechjackbot;

import com.mechjacktv.typedobject.TypedString;

public final class CommandUsage extends TypedString {

    private CommandUsage(final String value) {
        super(value);
    }

    public static CommandUsage of(final String value) {
        return new CommandUsage(value);
    }

}
