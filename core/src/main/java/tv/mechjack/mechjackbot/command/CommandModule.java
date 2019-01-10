package tv.mechjack.mechjackbot.command;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;

import tv.mechjack.mechjackbot.ChatCommandRegistry;
import tv.mechjack.mechjackbot.ChatCommandUtils;
import tv.mechjack.mechjackbot.ChatMessageEventHandler;
import tv.mechjack.mechjackbot.command.core.CoreCommandModule;

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
