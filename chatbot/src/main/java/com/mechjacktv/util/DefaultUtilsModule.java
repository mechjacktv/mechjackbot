package com.mechjacktv.util;

import com.google.inject.AbstractModule;

public final class DefaultUtilsModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(ProtobufUtils.class).to(DefaultProtobufUtils.class).asEagerSingleton();
        bind(TimeUtils.class).to(DefaultTimeUtils.class).asEagerSingleton();
    }

}
