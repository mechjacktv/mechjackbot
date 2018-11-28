package com.mechjacktv.mechjackbot.command.core;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.google.inject.multibindings.Multibinder;

import com.mechjacktv.mechjackbot.Command;
import com.mechjacktv.mechjackbot.CommandRegistry;
import com.mechjacktv.mechjackbot.CommandUtils;
import com.mechjacktv.mechjackbot.MessageEventHandler;
import com.mechjacktv.mechjackbot.command.shoutout.ShoutOutCommandModule;

public final class CoreCommandModule extends AbstractModule {

  @Override
  protected final void configure() {
    this.install(new ShoutOutCommandModule());

    this.bind(CommandRegistry.class).to(DefaultCommandRegistry.class).in(Scopes.SINGLETON);
    this.bind(CommandUtils.class).to(DefaultCommandUtils.class).in(Scopes.SINGLETON);
    this.bind(MessageEventHandler.class).to(DefaultMessageEventHandler.class).in(Scopes.SINGLETON);

    Multibinder.newSetBinder(this.binder(), Command.class).addBinding().to(CommandsCommand.class).in(Scopes.SINGLETON);
    Multibinder.newSetBinder(this.binder(), Command.class).addBinding().to(HelpCommand.class).in(Scopes.SINGLETON);
    Multibinder.newSetBinder(this.binder(), Command.class).addBinding().to(PingCommand.class).in(Scopes.SINGLETON);
    Multibinder.newSetBinder(this.binder(), Command.class).addBinding().to(QuitCommand.class).in(Scopes.SINGLETON);
    Multibinder.newSetBinder(this.binder(), Command.class).addBinding().to(TestCommand.class).in(Scopes.SINGLETON);
  }

}
