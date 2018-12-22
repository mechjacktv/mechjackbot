package com.mechjacktv.mechjackbot.command;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;

import com.mechjacktv.mechjackbot.ChatCommandRegistry;
import com.mechjacktv.mechjackbot.ChatCommandUtils;
import com.mechjacktv.mechjackbot.ChatMessageEventHandler;
import com.mechjacktv.mechjackbot.command.core.CoreCommandModule;
import com.mechjacktv.mechjackbot.command.shoutout.ShoutOutCommandModule;

public final class CommandModule extends AbstractModule {

  @Override
  protected void configure() {
    this.install(new CoreCommandModule());
    this.install(new ShoutOutCommandModule());

    this.bind(CommandConfigurationBuilder.class).to(DefaultCommandConfigurationBuilder.class);
    this.bind(ChatCommandRegistry.class).to(DefaultChatCommandRegistry.class).in(Scopes.SINGLETON);
    this.bind(ChatCommandUtils.class).to(DefaultChatCommandUtils.class).in(Scopes.SINGLETON);
    this.bind(ChatMessageEventHandler.class).to(DefaultChatMessageEventHandler.class).in(Scopes.SINGLETON);
  }
}
