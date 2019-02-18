package tv.mechjack.mechjackbot.chatbot.kicl;

import com.google.inject.Provides;
import com.google.inject.Scopes;
import com.google.inject.Singleton;

import org.kitteh.irc.client.library.Client;
import org.kitteh.irc.client.library.Client.Builder;
import org.kitteh.irc.client.library.feature.twitch.TwitchSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tv.mechjack.mechjackbot.api.ChatBot;
import tv.mechjack.mechjackbot.api.ChatBotConfiguration;
import tv.mechjack.mechjackbot.chatbot.ChatBotModule;

public class KiclChatBotModule extends ChatBotModule {

  public static final String TWITCH_IRC_SERVER_HOST = "irc.chat.twitch.tv";
  public static final Integer TWITCH_IRC_SERVER_PORT = 443;

  @Override
  protected void configure() {
    super.configure();
    this.bind(ChatBot.class).to(KiclChatBot.class).in(Scopes.SINGLETON);
    this.bind(KiclChatBotListener.class).in(Scopes.SINGLETON);
    this.bind(KiclChatMessageEventFactory.class).in(Scopes.SINGLETON);
    this.bind(KiclChatUserFactory.class).in(Scopes.SINGLETON);
  }

  @Provides
  @Singleton
  public final Client createIrcClient(final ChatBotConfiguration chatBotConfiguration,
      final KiclChatBotListener listener) {
    // if (!chatBotConfiguration.isReady()) {
    // throw new IllegalStateException("Chat bot not configured.");
    // }

    final Logger clientLogger = LoggerFactory.getLogger(Client.class);
    final Builder builder = Client.builder();

    builder.nick(chatBotConfiguration.getTwitchLogin().value);

    builder.listeners()
        .input(clientLogger::trace)
        .output(clientLogger::trace)
        .exception(throwable -> clientLogger.error(throwable.getMessage(), throwable));
    builder.server()
        .host(TWITCH_IRC_SERVER_HOST)
        .port(TWITCH_IRC_SERVER_PORT)
        .password(chatBotConfiguration.getUserPassword().value);

    final Client client = builder.build();

    TwitchSupport.addSupport(client);
    client.getEventManager().registerEventListener(listener);
    client.addChannel("#" + chatBotConfiguration.getChatChannel().value);
    return client;
  }

}
