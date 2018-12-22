package com.mechjacktv.mechjackbot.command.core;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.google.inject.multibindings.Multibinder;

import com.mechjacktv.mechjackbot.ChatCommand;

public final class CoreCommandModule extends AbstractModule {

  @Override
  protected final void configure() {
    Multibinder.newSetBinder(this.binder(), ChatCommand.class).addBinding().to(CommandsChatCommand.class)
        .in(Scopes.SINGLETON);
    Multibinder.newSetBinder(this.binder(), ChatCommand.class).addBinding().to(HelpChatCommand.class)
        .in(Scopes.SINGLETON);
    Multibinder.newSetBinder(this.binder(), ChatCommand.class).addBinding().to(PingChatCommand.class)
        .in(Scopes.SINGLETON);
    Multibinder.newSetBinder(this.binder(), ChatCommand.class).addBinding().to(QuitChatCommand.class)
        .in(Scopes.SINGLETON);
  }

}
