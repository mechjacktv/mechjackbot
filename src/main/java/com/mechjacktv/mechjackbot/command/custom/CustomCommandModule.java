package com.mechjacktv.mechjackbot.command.custom;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.google.inject.multibindings.Multibinder;

import com.mechjacktv.mechjackbot.Command;

public class CustomCommandModule extends AbstractModule {

  @Override
  protected void configure() {
    this.bind(CustomCommandDataStore.class).to(DefaultCustomCommandDataStore.class).in(Scopes.SINGLETON);

    Multibinder.newSetBinder(this.binder(), Command.class).addBinding().to(SetCommandCommand.class)
        .in(Scopes.SINGLETON);
  }

}
