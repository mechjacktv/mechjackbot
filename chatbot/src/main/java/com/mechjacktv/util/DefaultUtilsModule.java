package com.mechjacktv.util;

import com.google.inject.AbstractModule;

public final class DefaultUtilsModule extends AbstractModule {

    @Override
    protected void configure() {
        this.bind(ExecutionUtils.class).to(DefaultExecutionUtils.class).asEagerSingleton();
        this.bind(ProtobufUtils.class).to(DefaultProtobufUtils.class).asEagerSingleton();
        this.bind(TimeUtils.class).to(DefaultTimeUtils.class).asEagerSingleton();
    }

}
