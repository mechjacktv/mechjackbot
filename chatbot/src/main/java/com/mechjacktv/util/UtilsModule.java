package com.mechjacktv.util;

import com.google.inject.AbstractModule;

public final class UtilsModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(ProtobufUtils.class).asEagerSingleton();
        bind(TimeUtils.class).asEagerSingleton();
    }

}
