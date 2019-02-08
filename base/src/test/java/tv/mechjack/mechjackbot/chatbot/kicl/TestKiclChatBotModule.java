package tv.mechjack.mechjackbot.chatbot.kicl;

import java.util.ArrayList;

import com.google.inject.Provides;
import com.google.inject.Scopes;
import com.google.inject.Singleton;

import org.kitteh.irc.client.library.Client;
import org.kitteh.irc.client.library.element.Channel;
import org.kitteh.irc.client.library.element.User;
import org.kitteh.irc.client.library.event.channel.ChannelMessageEvent;

import tv.mechjack.mechjackbot.api.ChatBot;
import tv.mechjack.mechjackbot.chatbot.TestChatBotModule;
import tv.mechjack.testframework.ArbitraryDataGenerator;
import tv.mechjack.testframework.fake.FakeBuilder;
import tv.mechjack.testframework.fake.FakeFactory;

public class TestKiclChatBotModule extends TestChatBotModule {

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
  public final Client createFakeClient(final FakeFactory fakeFactory) {
    return fakeFactory.fake(Client.class);
  }

  @Provides
  @Singleton
  public final Channel createFakeChannel(final ArbitraryDataGenerator arbitraryDataGenerator,
      final FakeFactory fakeFactory, final Client client) {
    final FakeBuilder<Channel> fakeBuilder = fakeFactory.builder(Channel.class);
    final String channelName = arbitraryDataGenerator.getString();
    fakeBuilder.forMethod("getClient").addHandler(invocation -> client);
    fakeBuilder.forMethod("getName").addHandler(invocation -> channelName);

    return fakeBuilder.build();
  }

  @Provides
  public final User createFakeUser(final ArbitraryDataGenerator arbitraryDataGenerator,
      final FakeFactory fakeFactory, final Client client) {
    final FakeBuilder<User> fakeBuilder = fakeFactory.builder(User.class);
    final String userNick = arbitraryDataGenerator.getString();

    fakeBuilder.forMethod("getClient").addHandler(invocation -> client);
    fakeBuilder.forMethod("getNick").addHandler(invocation -> userNick);
    return fakeBuilder.build();
  }

  @Provides
  public final ChannelMessageEvent createChannelMessageEvent(final ArbitraryDataGenerator arbitraryDataGenerator,
      final Client client, final Channel channel, final User user) {
    return this.createTestChannelMessageEvent(arbitraryDataGenerator, client, channel, user);
  }

  @Provides
  public final TestChannelMessageEvent createTestChannelMessageEvent(
      final ArbitraryDataGenerator arbitraryDataGenerator,
      final Client client, final Channel channel, final User user) {
    return new TestChannelMessageEvent(client, new ArrayList<>(), user, channel,
        arbitraryDataGenerator.getString());
  }

}
