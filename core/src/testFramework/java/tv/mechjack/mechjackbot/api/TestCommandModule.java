package tv.mechjack.mechjackbot.api;

import javax.inject.Singleton;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Scopes;
import com.google.inject.multibindings.Multibinder;

import tv.mechjack.mechjackbot.core.DefaultChatCommandRegistry;
import tv.mechjack.mechjackbot.core.DefaultChatCommandUtils;
import tv.mechjack.mechjackbot.core.DefaultCommandConfigurationBuilder;
import tv.mechjack.mechjackbot.core.DefaultPicoCliUtils;
import tv.mechjack.testframework.fake.FakeFactory;

public class TestCommandModule extends AbstractModule {

  @Override
  protected void configure() {
    this.bind(CommandAssertionsUtils.class).in(Scopes.SINGLETON);
    this.bind(CommandConfigurationBuilder.class).to(DefaultCommandConfigurationBuilder.class);
    this.bind(ChatCommandRegistry.class).to(DefaultChatCommandRegistry.class).in(Scopes.SINGLETON);
    this.bind(ChatCommandUtils.class).to(DefaultChatCommandUtils.class).in(Scopes.SINGLETON);
    this.bind(TestChatMessageEvent.class);
    this.bind(ChatMessageEvent.class).to(TestChatMessageEvent.class);
    this.bind(PicoCliUtils.class).to(DefaultPicoCliUtils.class).in(Scopes.SINGLETON);

    this.bind(TestChatCommand.class);
    Multibinder.newSetBinder(this.binder(), ChatCommand.class).addBinding().to(TestChatCommand.class)
        .in(Scopes.SINGLETON);

  }

  @Provides
  @Singleton
  public final ChatMessageEventHandler createChatMessageEventHandler(final FakeFactory fakeFactory) {
    // TODO (2019-01-10 mechjack): Create TestChatMessageEventHandler
    return fakeFactory.fake(ChatMessageEventHandler.class);
  }

}
