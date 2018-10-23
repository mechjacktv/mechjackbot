package com.mechjacktv.twitchclient;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;
import com.mechjacktv.gson.TypeAdapterRegistrar;
import com.mechjacktv.twitchclient.messageadapter.MessageAdapterRegistrar;

public class DefaultTwitchClientModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(TwitchClientFactory.class).to(DefaultTwitchClientFactory.class).asEagerSingleton();

        Multibinder.newSetBinder(binder(), TypeAdapterRegistrar.class).addBinding()
                .to(MessageAdapterRegistrar.class).asEagerSingleton();

    }

}
