package com.mechjacktv.mechjackbot.command.custom;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.google.inject.multibindings.Multibinder;

import com.mechjacktv.mechjackbot.ChatCommand;

public class CustomCommandModule extends AbstractModule {

  @Override
  protected void configure() {
    this.bind(CustomCommandDataStore.class).to(DefaultCustomCommandDataStore.class).in(Scopes.SINGLETON);
    this.bind(CustomChatCommandService.class).to(DefaultCustomChatCommandService.class).asEagerSingleton();

    Multibinder.newSetBinder(this.binder(), ChatCommand.class).addBinding().to(SetCommandChatCommand.class)
        .in(Scopes.SINGLETON);
    Multibinder.newSetBinder(this.binder(), ChatCommand.class).addBinding().to(DeleteCommandChatCommand.class)
        .in(Scopes.SINGLETON);
  }

}
