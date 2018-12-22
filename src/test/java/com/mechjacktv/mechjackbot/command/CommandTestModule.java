package com.mechjacktv.mechjackbot.command;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;

import com.mechjacktv.mechjackbot.ChatCommand;
import com.mechjacktv.mechjackbot.ChatCommandRegistry;
import com.mechjacktv.mechjackbot.ChatCommandUtils;
import com.mechjacktv.mechjackbot.ChatMessageEvent;
import com.mechjacktv.mechjackbot.TestChatCommand;
import com.mechjacktv.mechjackbot.TestChatMessageEvent;

public class CommandTestModule extends AbstractModule {

  @Override
  protected void configure() {
    this.bind(TestChatCommand.class);
    this.bind(ChatCommand.class).to(TestChatCommand.class);
    this.bind(CommandAssertionsUtils.class).in(Scopes.SINGLETON);
    this.bind(CommandConfigurationBuilder.class).to(DefaultCommandConfigurationBuilder.class);
    this.bind(ChatCommandRegistry.class).to(DefaultChatCommandRegistry.class).in(Scopes.SINGLETON);
    this.bind(ChatCommandUtils.class).to(DefaultChatCommandUtils.class).in(Scopes.SINGLETON);
    this.bind(TestChatMessageEvent.class);
    this.bind(ChatMessageEvent.class).to(TestChatMessageEvent.class);
  }

}
