package com.mechjacktv.twitchclient;

import com.google.inject.AbstractModule;

public class DefaultTwitchClientModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(TwitchClientFactory.class).to(DefaultTwitchClientFactory.class).asEagerSingleton();
    }

}
