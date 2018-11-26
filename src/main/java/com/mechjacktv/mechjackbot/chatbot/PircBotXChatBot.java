package com.mechjacktv.mechjackbot.chatbot;

import java.util.Objects;
import java.util.function.Function;

import javax.inject.Inject;

import org.pircbotx.Configuration;
import org.pircbotx.PircBotX;
import org.pircbotx.hooks.Listener;

import com.mechjacktv.mechjackbot.*;
import com.mechjacktv.util.ExecutionUtils;

public final class PircBotXChatBot implements ChatBot {

  static final String TWITCH_IRC_SERVER_HOST = "irc.chat.twitch.tv";
  static final Integer TWITCH_IRC_SERVER_PORT = 6667;

  static final String SHUTDOWN_MESSAGE_KEY = "chat_bot.shutdown.message";
  private static final String SHUTDOWN_MESSAGE_DEFAULT = "Shutdown";

  static final String CHAT_BOT_MESSAGE_FORMAT_KEY = "chat_bot.message_format";
  static final String CHAT_BOT_MESSAGE_FORMAT_DEFAULT = "/me MrDestructoid > %s";

  private final AppConfiguration appConfiguration;
  private final ExecutionUtils executionUtils;
  private final PircBotX pircBotX;

  @Inject
  public PircBotXChatBot(final AppConfiguration appConfiguration, final ChatBotConfiguration chatBotConfiguration,
      final ExecutionUtils executionUtils, final Listener listener) {
    this(appConfiguration, chatBotConfiguration, executionUtils, listener, PircBotX::new);
  }

  PircBotXChatBot(final AppConfiguration appConfiguration, final ChatBotConfiguration chatBotConfiguration,
      final ExecutionUtils executionUtils, final Listener listener,
      final Function<Configuration, PircBotX> botFactory) {
    final Configuration configuration = new Configuration.Builder()
        .setName(chatBotConfiguration.getTwitchUsername().value)
        .addServer(TWITCH_IRC_SERVER_HOST, TWITCH_IRC_SERVER_PORT)
        .setServerPassword(chatBotConfiguration.getTwitchPassword().value)
        .addListener(listener)
        .addAutoJoinChannel("#" + chatBotConfiguration.getTwitchChannel().value)
        .buildConfiguration();

    this.appConfiguration = appConfiguration;
    this.executionUtils = executionUtils;
    this.pircBotX = botFactory.apply(configuration);
  }

  PircBotXChatBot(final AppConfiguration appConfiguration, final ExecutionUtils executionUtils,
      final PircBotX pircBotX) {
    this.appConfiguration = appConfiguration;
    this.executionUtils = executionUtils;
    this.pircBotX = pircBotX;
  }

  @Override
  public void sendMessage(final TwitchChannel channel, final Message message) {
    Objects.requireNonNull(channel, this.executionUtils.nullMessageForName("channel"));
    Objects.requireNonNull(message, this.executionUtils.nullMessageForName("message"));
    this.pircBotX.sendIRC().message(channel.value, String.format(this.appConfiguration.get(CHAT_BOT_MESSAGE_FORMAT_KEY,
        CHAT_BOT_MESSAGE_FORMAT_DEFAULT), message));
  }

  @Override
  public void start() {
    this.executionUtils.softenException(this.pircBotX::startBot, ChatBotStartupException.class);
  }

  @Override
  public void stop() {
    this.pircBotX.stopBotReconnect();
    this.pircBotX.sendIRC().quitServer(this.appConfiguration.get(SHUTDOWN_MESSAGE_KEY, SHUTDOWN_MESSAGE_DEFAULT));
  }

}
