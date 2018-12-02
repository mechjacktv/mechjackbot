package com.mechjacktv.mechjackbot.command;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;

import com.mechjacktv.mechjackbot.CommandRegistry;
import com.mechjacktv.mechjackbot.CommandUtils;
import com.mechjacktv.mechjackbot.MessageEventHandler;
import com.mechjacktv.mechjackbot.command.core.CoreCommandModule;
import com.mechjacktv.mechjackbot.command.shoutout.ShoutOutCommandModule;

public class CommandModule extends AbstractModule {

  @Override
  protected void configure() {
    this.install(new CoreCommandModule());
    this.install(new ShoutOutCommandModule());

    this.bind(CommandConfigurationBuilder.class).to(DefaultCommandConfigurationBuilder.class);
    this.bind(CommandRegistry.class).to(DefaultCommandRegistry.class).in(Scopes.SINGLETON);
    this.bind(CommandUtils.class).to(DefaultCommandUtils.class).in(Scopes.SINGLETON);
    this.bind(MessageEventHandler.class).to(DefaultMessageEventHandler.class).in(Scopes.SINGLETON);
  }
}
