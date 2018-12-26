package com.mechjacktv.mechjackbot.command.custom;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;

public class CustomCommandTestModule extends AbstractModule {

  @Override
  protected void configure() {
    this.bind(CustomChatCommandService.class).to(DefaultCustomChatCommandService.class).in(Scopes.SINGLETON);
    this.bind(CustomCommandDataStore.class).to(DefaultCustomCommandDataStore.class).in(Scopes.SINGLETON);
  }

}
