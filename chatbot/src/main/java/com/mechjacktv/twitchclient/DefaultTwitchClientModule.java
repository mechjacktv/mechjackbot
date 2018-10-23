package com.mechjacktv.twitchclient;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;
import com.mechjacktv.gson.TypeAdapterRegistrar;
import com.mechjacktv.twitchclient.messageadapter.MessageAdapterRegistrar;

public final class DefaultTwitchClientModule extends AbstractModule {

  @Override
  protected final void configure() {
      this.bind(TwitchClientFactory.class).to(DefaultTwitchClientFactory.class).asEagerSingleton();

    Multibinder.newSetBinder(this.binder(), TypeAdapterRegistrar.class).addBinding()
        .to(MessageAdapterRegistrar.class).asEagerSingleton();

  }

}
