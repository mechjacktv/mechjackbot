package com.mechjacktv.mechjackbot.command;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;

import com.mechjacktv.mechjackbot.Command;
import com.mechjacktv.mechjackbot.CommandRegistry;
import com.mechjacktv.mechjackbot.CommandUtils;
import com.mechjacktv.mechjackbot.MessageEvent;
import com.mechjacktv.mechjackbot.TestCommand;
import com.mechjacktv.mechjackbot.TestMessageEvent;

public class CommandTestModule extends AbstractModule {

  @Override
  protected void configure() {
    this.bind(TestCommand.class);
    this.bind(Command.class).to(TestCommand.class);
    this.bind(CommandAssertionsUtils.class).in(Scopes.SINGLETON);
    this.bind(CommandConfigurationBuilder.class).to(DefaultCommandConfigurationBuilder.class);
    this.bind(CommandRegistry.class).to(DefaultCommandRegistry.class).in(Scopes.SINGLETON);
    this.bind(CommandUtils.class).to(DefaultCommandUtils.class).in(Scopes.SINGLETON);
    this.bind(TestMessageEvent.class);
    this.bind(MessageEvent.class).to(TestMessageEvent.class);
  }

}
