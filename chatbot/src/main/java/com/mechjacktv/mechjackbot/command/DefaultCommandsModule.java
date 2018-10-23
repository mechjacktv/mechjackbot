package com.mechjacktv.mechjackbot.command;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;
import com.mechjacktv.mechjackbot.Command;
import com.mechjacktv.mechjackbot.command.shoutout.*;
import com.mechjacktv.mechjackbot.command.interceptor.DefaultCommandInterceptorsModule;

public final class DefaultCommandsModule extends AbstractModule {

  @Override
  protected final void configure() {
    this.install(new DefaultCommandInterceptorsModule());
    this.install(new ShoutOutModule());

    this.bind(CommandUtils.class).asEagerSingleton();

    Multibinder.newSetBinder(this.binder(), Command.class).addBinding().to(CommandsCommand.class).asEagerSingleton();
    Multibinder.newSetBinder(this.binder(), Command.class).addBinding().to(DelConfigCommand.class).asEagerSingleton();
    Multibinder.newSetBinder(this.binder(), Command.class).addBinding().to(HelpCommand.class).asEagerSingleton();
    Multibinder.newSetBinder(this.binder(), Command.class).addBinding().to(InspectConfigCommand.class).asEagerSingleton();
    Multibinder.newSetBinder(this.binder(), Command.class).addBinding().to(PingCommand.class).asEagerSingleton();
    Multibinder.newSetBinder(this.binder(), Command.class).addBinding().to(SetConfigCommand.class).asEagerSingleton();
    Multibinder.newSetBinder(this.binder(), Command.class).addBinding().to(TestCommand.class).asEagerSingleton();
    Multibinder.newSetBinder(this.binder(), Command.class).addBinding().to(QuitCommand.class).asEagerSingleton();
  }

}
