package tv.mechjack.mechjackbot.chatbot.kicl;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

import com.google.inject.Provides;
import com.google.inject.Scopes;
import com.google.inject.Singleton;

import org.kitteh.irc.client.library.Client;
import org.kitteh.irc.client.library.element.Channel;
import org.kitteh.irc.client.library.element.User;
import org.kitteh.irc.client.library.event.channel.ChannelMessageEvent;
import org.mockito.Mockito;

import tv.mechjack.mechjackbot.ChatBot;
import tv.mechjack.mechjackbot.chatbot.ChatBotTestModule;
import tv.mechjack.testframework.ArbitraryDataGenerator;

public class KiclChatBotTestModule extends ChatBotTestModule {

  @Override
  protected void configure() {
    super.configure();
    this.bind(KiclChatBot.class).in(Scopes.SINGLETON);
    this.bind(ChatBot.class).to(KiclChatBot.class);
    this.bind(KiclChatBotListener.class).in(Scopes.SINGLETON);
    this.bind(KiclChatMessageEventFactory.class).in(Scopes.SINGLETON);
    this.bind(KiclChatUserFactory.class).in(Scopes.SINGLETON);
  }

  @Provides
  @Singleton
  public final Client createFakeIrcClient() {
    return Mockito.mock(Client.class);
  }

  @Provides
  public final ChannelMessageEvent createChannelMessageEvent(final ArbitraryDataGenerator arbitraryDataGenerator,
      final Client client) {
    final Channel channel = mock(Channel.class);
    final User user = mock(User.class);

    when(channel.getClient()).thenReturn(client);
    when(channel.getName()).thenReturn(arbitraryDataGenerator.getString());
    when(user.getClient()).thenReturn(client);
    when(user.getNick()).thenReturn(arbitraryDataGenerator.getString());
    return new ChannelMessageEvent(client, new ArrayList<>(), user, channel, arbitraryDataGenerator.getString());
  }

}
