package com.mechjacktv.twitchclient;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.google.inject.multibindings.Multibinder;

import com.mechjacktv.gson.TypeAdapterRegistrar;
import com.mechjacktv.twitchclient.messageadapter.MessageTypeAdapterRegistrar;

public final class TwitchClientModule extends AbstractModule {

  @Override
  protected final void configure() {
    this.bind(TwitchClient.class).to(DefaultTwitchClient.class).in(Scopes.SINGLETON);
    this.bind(TwitchClientUtils.class).to(DefaultTwitchClientUtils.class).in(Scopes.SINGLETON);
    this.bind(TwitchUsersEndpoint.class).to(DefaultTwitchUsersEndpoint.class).in(Scopes.SINGLETON);
    this.bind(TwitchUsersFollowsEndpoint.class).to(DefaultTwitchUsersFollowsEndpoint.class).in(Scopes.SINGLETON);

    Multibinder.newSetBinder(this.binder(), TypeAdapterRegistrar.class).addBinding()
        .to(MessageTypeAdapterRegistrar.class).in(Scopes.SINGLETON);
  }

}
