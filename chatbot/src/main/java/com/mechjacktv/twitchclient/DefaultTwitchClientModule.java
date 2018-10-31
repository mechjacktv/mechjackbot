package com.mechjacktv.twitchclient;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;
import com.mechjacktv.gson.TypeAdapterRegistrar;
import com.mechjacktv.twitchclient.endpoint.DefaultTwitchUsersEndpoint;
import com.mechjacktv.twitchclient.endpoint.DefaultTwitchUsersFollowsEndpoint;
import com.mechjacktv.twitchclient.messageadapter.MessageTypeAdapterRegistrar;

public final class DefaultTwitchClientModule extends AbstractModule {

  @Override
  protected final void configure() {
    this.bind(TwitchClient.class).to(DefaultTwitchClient.class).asEagerSingleton();
    this.bind(TwitchClientUtils.class).to(DefaultTwitchClientUtils.class).asEagerSingleton();
    this.bind(TwitchUsersEndpoint.class).to(DefaultTwitchUsersEndpoint.class).asEagerSingleton();
    this.bind(TwitchUsersFollowsEndpoint.class).to(DefaultTwitchUsersFollowsEndpoint.class).asEagerSingleton();

    Multibinder.newSetBinder(this.binder(), TypeAdapterRegistrar.class).addBinding()
        .to(MessageTypeAdapterRegistrar.class).asEagerSingleton();
  }

}
