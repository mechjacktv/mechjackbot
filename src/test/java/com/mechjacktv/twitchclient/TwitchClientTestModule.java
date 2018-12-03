package com.mechjacktv.twitchclient;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;

public class TwitchClientTestModule extends AbstractModule {

  @Override
  protected void configure() {
    this.bind(TestTwitchClient.class).in(Scopes.SINGLETON);
    this.bind(TwitchClient.class).to(TestTwitchClient.class);
  }

}
