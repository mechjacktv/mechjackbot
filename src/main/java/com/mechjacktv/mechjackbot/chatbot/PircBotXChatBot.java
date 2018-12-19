package com.mechjacktv.mechjackbot.chatbot;

import java.util.Objects;
import java.util.function.Function;

import javax.inject.Inject;

import org.pircbotx.PircBotX;
import org.pircbotx.hooks.Listener;

import com.mechjacktv.configuration.Configuration;
import com.mechjacktv.mechjackbot.ChatBot;
import com.mechjacktv.mechjackbot.ChatBotConfiguration;
import com.mechjacktv.mechjackbot.Message;
import com.mechjacktv.mechjackbot.TwitchChannel;
import com.mechjacktv.util.ExecutionUtils;

public final class PircBotXChatBot implements ChatBot {

  static final String TWITCH_IRC_SERVER_HOST = "irc.chat.twitch.tv";
  static final Integer TWITCH_IRC_SERVER_PORT = 6667;

  static final String SHUTDOWN_MESSAGE_KEY = "chat_bot.shutdown.message";
  private static final String SHUTDOWN_MESSAGE_DEFAULT = "Shutdown";

  static final String CHAT_BOT_MESSAGE_FORMAT_KEY = "chat_bot.message_format";
  static final String CHAT_BOT_MESSAGE_FORMAT_DEFAULT = "/me MrDestructoid > %s";

  private final Configuration configuration;
  private final ExecutionUtils executionUtils;
  private final PircBotX pircBotX;

  @Inject
  PircBotXChatBot(final Configuration configuration, final ChatBotConfiguration chatBotConfiguration,
      final ExecutionUtils executionUtils, final Listener listener) {
    this(configuration, chatBotConfiguration, executionUtils, listener, PircBotX::new);
  }

  PircBotXChatBot(final Configuration appConfiguration, final ChatBotConfiguration chatBotConfiguration,
      final ExecutionUtils executionUtils, final Listener listener,
      final Function<org.pircbotx.Configuration, PircBotX> botFactory) {
    final org.pircbotx.Configuration configuration = new org.pircbotx.Configuration.Builder()
        .setName(chatBotConfiguration.getTwitchLogin().value)
        .addServer(TWITCH_IRC_SERVER_HOST, TWITCH_IRC_SERVER_PORT)
        .setServerPassword(chatBotConfiguration.getTwitchPassword().value)
        .addListener(listener)
        .addAutoJoinChannel("#" + chatBotConfiguration.getTwitchChannel().value)
        .buildConfiguration();

    this.configuration = appConfiguration;
    this.executionUtils = executionUtils;
    this.pircBotX = botFactory.apply(configuration);
  }

  PircBotXChatBot(final Configuration configuration, final ExecutionUtils executionUtils,
      final PircBotX pircBotX) {
    this.configuration = configuration;
    this.executionUtils = executionUtils;
    this.pircBotX = pircBotX;
  }

  @Override
  public void sendMessage(final TwitchChannel channel, final Message message) {
    Objects.requireNonNull(channel, this.executionUtils.nullMessageForName("channel"));
    Objects.requireNonNull(message, this.executionUtils.nullMessageForName("message"));
    this.pircBotX.sendIRC().message(channel.value, String.format(this.configuration.get(CHAT_BOT_MESSAGE_FORMAT_KEY,
        CHAT_BOT_MESSAGE_FORMAT_DEFAULT), message));
  }

  @Override
  public void start() {
    this.executionUtils.softenException(this.pircBotX::startBot, ChatBotStartupException.class);
  }

  @Override
  public void stop() {
    this.pircBotX.stopBotReconnect();
    this.pircBotX.sendIRC().quitServer(this.configuration.get(SHUTDOWN_MESSAGE_KEY, SHUTDOWN_MESSAGE_DEFAULT));
  }

}
