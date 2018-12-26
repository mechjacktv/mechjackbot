package com.mechjacktv.mechjackbot.chatbot.kicl;

import java.util.ArrayList;

import com.google.inject.Provides;
import com.google.inject.Scopes;
import com.google.inject.Singleton;
import com.mechjacktv.mechjackbot.ChatBot;
import com.mechjacktv.mechjackbot.chatbot.ChatBotTestModule;
import com.mechjacktv.testframework.ArbitraryDataGenerator;

import org.kitteh.irc.client.library.Client;
import org.kitteh.irc.client.library.element.Channel;
import org.kitteh.irc.client.library.element.User;
import org.kitteh.irc.client.library.event.channel.ChannelMessageEvent;
import org.mockito.Mockito;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
