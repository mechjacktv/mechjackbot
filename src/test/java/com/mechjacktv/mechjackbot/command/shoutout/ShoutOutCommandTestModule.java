package com.mechjacktv.mechjackbot.command.shoutout;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;

public class ShoutOutCommandTestModule extends AbstractModule {

  @Override
  protected void configure() {
    this.bind(ShoutOutDataStore.class).to(DefaultShoutOutDataStore.class).in(Scopes.SINGLETON);
  }

}
