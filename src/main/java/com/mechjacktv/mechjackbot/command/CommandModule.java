package com.mechjacktv.mechjackbot.command;

import com.google.inject.AbstractModule;

import com.mechjacktv.mechjackbot.command.core.CoreCommandModule;
import com.mechjacktv.mechjackbot.command.shoutout.ShoutOutCommandModule;

public class CommandModule extends AbstractModule {

  @Override
  protected void configure() {
    this.install(new CoreCommandModule());
    this.install(new ShoutOutCommandModule());

    this.bind(CommandConfigurationBuilder.class).to(DefaultCommandConfigurationBuilder.class);
  }
}
