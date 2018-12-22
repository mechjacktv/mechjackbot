package com.mechjacktv.mechjackbot.chatbot.kicl;

import com.google.inject.Provides;
import com.google.inject.Scopes;
import com.google.inject.Singleton;

import org.kitteh.irc.client.library.Client;
import org.kitteh.irc.client.library.Client.Builder;
import org.kitteh.irc.client.library.feature.twitch.TwitchDelaySender;
import org.kitteh.irc.client.library.feature.twitch.TwitchListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mechjacktv.mechjackbot.ChatBot;
import com.mechjacktv.mechjackbot.ChatBotConfiguration;
import com.mechjacktv.mechjackbot.chatbot.ChatBotModule;

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
    final Logger clientLogger = LoggerFactory.getLogger(Client.class);
    final Builder builder = Client.builder();

    builder.serverHost(TWITCH_IRC_SERVER_HOST);
    builder.serverPort(TWITCH_IRC_SERVER_PORT);
    builder.serverPassword(chatBotConfiguration.getUserPassword().value);
    builder.nick(chatBotConfiguration.getTwitchLogin().value);
    builder.messageSendingQueueSupplier(TwitchDelaySender.getSupplier(false));
    builder.inputListener(clientLogger::trace);
    builder.outputListener(clientLogger::trace);
    builder.exceptionListener(throwable -> clientLogger.error(throwable.getMessage(), throwable));

    final Client client = builder.build();

    client.getEventManager().registerEventListener(new TwitchListener(client));
    client.getEventManager().registerEventListener(listener);
    client.addChannel("#" + chatBotConfiguration.getChatChannel().value);
    return client;
  }

}
