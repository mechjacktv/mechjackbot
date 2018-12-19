package com.mechjacktv.mechjackbot.command.shoutout;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.google.inject.multibindings.Multibinder;

import com.mechjacktv.mechjackbot.Command;

public final class ShoutOutCommandModule extends AbstractModule {

  @Override
  protected final void configure() {
    this.bind(ShoutOutDataStore.class).to(DefaultShoutOutDataStore.class).in(Scopes.SINGLETON);

    Multibinder.newSetBinder(this.binder(), Command.class).addBinding().to(ShoutOutListenerCommand.class)
        .in(Scopes.SINGLETON);
  }

}
