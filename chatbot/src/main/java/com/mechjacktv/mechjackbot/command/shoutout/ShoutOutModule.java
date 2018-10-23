package com.mechjacktv.mechjackbot.command.shoutout;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;
import com.mechjacktv.mechjackbot.Command;

public class ShoutOutModule extends AbstractModule {

  @Override
  protected void configure() {
    this.bind(ShoutOutDataStore.class).asEagerSingleton();
    this.bind(ShoutOutService.class).asEagerSingleton();

    Multibinder.newSetBinder(this.binder(), Command.class).addBinding().to(AddCasterCommand.class).asEagerSingleton();
    Multibinder.newSetBinder(this.binder(), Command.class).addBinding().to(CasterCommand.class).asEagerSingleton();
    Multibinder.newSetBinder(this.binder(), Command.class).addBinding().to(CasterListenerCommand.class).asEagerSingleton();
    Multibinder.newSetBinder(this.binder(), Command.class).addBinding().to(DelCasterCommand.class).asEagerSingleton();
  }

}
