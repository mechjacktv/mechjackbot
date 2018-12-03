package com.mechjacktv.mechjackbot;

import com.google.inject.AbstractModule;

public class MechJackBotTestModule extends AbstractModule {

  @Override
  protected void configure() {
    this.bind(TestCommand.class);
    this.bind(Command.class).to(TestCommand.class);
    this.bind(TestMessageEvent.class);
    this.bind(MessageEvent.class).to(TestMessageEvent.class);
  }
}
