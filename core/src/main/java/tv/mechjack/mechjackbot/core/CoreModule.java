package tv.mechjack.mechjackbot.core;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;

import tv.mechjack.mechjackbot.api.ChatCommandRegistry;
import tv.mechjack.mechjackbot.api.ChatCommandUtils;
import tv.mechjack.mechjackbot.api.ChatMessageEventHandler;
import tv.mechjack.mechjackbot.api.CommandConfigurationBuilder;
import tv.mechjack.mechjackbot.api.PicoCliUtils;

public final class CoreModule extends AbstractModule {

  @Override
  protected final void configure() {
    this.bind(CommandConfigurationBuilder.class).to(DefaultCommandConfigurationBuilder.class);
    this.bind(ChatCommandRegistry.class).to(DefaultChatCommandRegistry.class).in(Scopes.SINGLETON);
    this.bind(ChatCommandUtils.class).to(DefaultChatCommandUtils.class).in(Scopes.SINGLETON);
    this.bind(ChatMessageEventHandler.class).to(DefaultChatMessageEventHandler.class).in(Scopes.SINGLETON);
    this.bind(PicoCliUtils.class).to(DefaultPicoCliUtils.class).in(Scopes.SINGLETON);
  }

}
