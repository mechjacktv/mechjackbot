package tv.mechjack.mechjackbot.command;

import static org.mockito.Mockito.mock;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.google.inject.multibindings.Multibinder;

import tv.mechjack.mechjackbot.ChatCommand;
import tv.mechjack.mechjackbot.ChatCommandRegistry;
import tv.mechjack.mechjackbot.ChatCommandUtils;
import tv.mechjack.mechjackbot.ChatMessageEvent;
import tv.mechjack.mechjackbot.ChatMessageEventHandler;
import tv.mechjack.mechjackbot.TestChatCommand;
import tv.mechjack.mechjackbot.TestChatMessageEvent;

public class CommandTestModule extends AbstractModule {

  @Override
  protected void configure() {
    this.bind(CommandAssertionsUtils.class).in(Scopes.SINGLETON);
    this.bind(CommandConfigurationBuilder.class).to(DefaultCommandConfigurationBuilder.class);
    this.bind(ChatCommandRegistry.class).to(DefaultChatCommandRegistry.class).in(Scopes.SINGLETON);
    this.bind(ChatCommandUtils.class).to(DefaultChatCommandUtils.class).in(Scopes.SINGLETON);
    this.bind(TestChatMessageEvent.class);
    this.bind(ChatMessageEvent.class).to(TestChatMessageEvent.class);
    this.bind(ChatMessageEventHandler.class).toInstance(mock(ChatMessageEventHandler.class));

    this.bind(TestChatCommand.class);
    Multibinder.newSetBinder(this.binder(), ChatCommand.class).addBinding().to(TestChatCommand.class)
        .in(Scopes.SINGLETON);

  }

}
