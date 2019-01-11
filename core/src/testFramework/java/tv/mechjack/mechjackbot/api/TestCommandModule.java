package tv.mechjack.mechjackbot.api;

import static org.mockito.Mockito.mock;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.google.inject.multibindings.Multibinder;

import tv.mechjack.mechjackbot.core.DefaultChatCommandRegistry;
import tv.mechjack.mechjackbot.core.DefaultChatCommandUtils;
import tv.mechjack.mechjackbot.core.DefaultCommandConfigurationBuilder;
import tv.mechjack.mechjackbot.core.DefaultPicoCliUtils;

public class TestCommandModule extends AbstractModule {

  @Override
  protected void configure() {
    this.bind(CommandAssertionsUtils.class).in(Scopes.SINGLETON);
    this.bind(CommandConfigurationBuilder.class).to(DefaultCommandConfigurationBuilder.class);
    this.bind(ChatCommandRegistry.class).to(DefaultChatCommandRegistry.class).in(Scopes.SINGLETON);
    this.bind(ChatCommandUtils.class).to(DefaultChatCommandUtils.class).in(Scopes.SINGLETON);
    this.bind(TestChatMessageEvent.class);
    this.bind(ChatMessageEvent.class).to(TestChatMessageEvent.class);
    // TODO (2019-01-10 mechjack): Create TestChatMessageEventHandler
    this.bind(ChatMessageEventHandler.class).toInstance(mock(ChatMessageEventHandler.class));
    this.bind(PicoCliUtils.class).to(DefaultPicoCliUtils.class).in(Scopes.SINGLETON);

    this.bind(TestChatCommand.class);
    Multibinder.newSetBinder(this.binder(), ChatCommand.class).addBinding().to(TestChatCommand.class)
        .in(Scopes.SINGLETON);

  }

}
