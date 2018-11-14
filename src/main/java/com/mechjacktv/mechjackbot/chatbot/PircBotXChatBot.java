package com.mechjacktv.mechjackbot.chatbot;

import java.util.function.Function;

import javax.inject.Inject;

import org.pircbotx.Configuration;
import org.pircbotx.PircBotX;
import org.pircbotx.hooks.Listener;

import com.mechjacktv.mechjackbot.AppConfiguration;
import com.mechjacktv.mechjackbot.ChatBot;
import com.mechjacktv.mechjackbot.ChatBotConfiguration;
import com.mechjacktv.util.ExecutionUtils;

public final class PircBotXChatBot implements ChatBot {

  public static final String TWITCH_IRC_SERVER_HOST = "irc.chat.twitch.tv";
  public static final Integer TWITCH_IRC_SERVER_PORT = 6667;

  public static final String SHUTDOWN_MESSAGE_KEY = "chat_bot.shutdown.message";
  public static final String SHUTDOWN_MESSAGE_DEFAULT = "Shutdown";

  private final AppConfiguration appConfiguration;
  private final ExecutionUtils executionUtils;
  private final PircBotX pircBotX;

  @Inject
  public PircBotXChatBot(final AppConfiguration appConfiguration, final ChatBotConfiguration chatBotConfiguration,
      final ExecutionUtils executionUtils, final Listener listener) {
    this(appConfiguration, chatBotConfiguration, executionUtils, listener,
        (configuration -> new PircBotX(configuration)));
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
  public void start() {
    this.executionUtils.softenException(this.pircBotX::startBot, PircBotXStartupException.class);
  }

  @Override
  public void stop() {
    this.pircBotX.stopBotReconnect();
    this.pircBotX.sendIRC().quitServer(this.appConfiguration.get(SHUTDOWN_MESSAGE_KEY, SHUTDOWN_MESSAGE_DEFAULT));
  }

}
