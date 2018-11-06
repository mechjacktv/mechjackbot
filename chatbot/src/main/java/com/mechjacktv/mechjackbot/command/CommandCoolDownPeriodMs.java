package com.mechjacktv.mechjackbot.command;

import com.mechjacktv.typedobject.TypedInteger;

public final class CommandCoolDownPeriodMs extends TypedInteger {

    private CommandCoolDownPeriodMs(final Integer value) {
        super(value);
    }

    public static CommandCoolDownPeriodMs of(final Integer value) {
        return new CommandCoolDownPeriodMs(value);
    }

}
