package com.mechjacktv.mechjackbot;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;

import com.mechjacktv.mechjackbot.command.DefaultCommandUtils;

public class MechJackBotTestModule extends AbstractModule {

  @Override
  protected void configure() {
    this.bind(CommandUtils.class).to(DefaultCommandUtils.class).in(Scopes.SINGLETON);
    this.bind(TestCommand.class);
    this.bind(TestMessageEvent.class);
    this.bind(MessageEvent.class).to(TestMessageEvent.class);
  }
}
