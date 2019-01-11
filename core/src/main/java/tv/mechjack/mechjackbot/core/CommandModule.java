package tv.mechjack.mechjackbot.core;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;

import tv.mechjack.mechjackbot.api.ChatCommandRegistry;
import tv.mechjack.mechjackbot.api.ChatCommandUtils;
import tv.mechjack.mechjackbot.api.ChatMessageEventHandler;
import tv.mechjack.mechjackbot.api.CommandConfigurationBuilder;

public final class CommandModule extends AbstractModule {

  @Override
  protected void configure() {
    this.install(new CoreCommandModule());

    this.bind(CommandConfigurationBuilder.class).to(DefaultCommandConfigurationBuilder.class);
    this.bind(ChatCommandRegistry.class).to(DefaultChatCommandRegistry.class).in(Scopes.SINGLETON);
    this.bind(ChatCommandUtils.class).to(DefaultChatCommandUtils.class).in(Scopes.SINGLETON);
    this.bind(ChatMessageEventHandler.class).to(DefaultChatMessageEventHandler.class).in(Scopes.SINGLETON);
  }
}
