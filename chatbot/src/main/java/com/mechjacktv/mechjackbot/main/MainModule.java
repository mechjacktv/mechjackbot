package com.mechjacktv.mechjackbot.main;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.mechjacktv.gson.GsonModule;
import com.mechjacktv.mechjackbot.ChatBotConfiguration;
import com.mechjacktv.mechjackbot.chatbot.PircBotXChatBotModule;
import com.mechjacktv.mechjackbot.command.DefaultCommandsModule;
import com.mechjacktv.mechjackbot.configuration.DefaultConfigurationModule;
import com.mechjacktv.keyvaluestore.MapDbKeyValueStoreModule;
import com.mechjacktv.twitchclient.DefaultTwitchClientModule;
import com.mechjacktv.twitchclient.TwitchClient;
import com.mechjacktv.twitchclient.TwitchClientFactory;
import com.mechjacktv.util.DefaultUtilsModule;

import javax.inject.Singleton;

final class MainModule extends AbstractModule {

  @Override
  protected final void configure() {
    this.install(new DefaultCommandsModule());
    this.install(new DefaultConfigurationModule());
    this.install(new DefaultTwitchClientModule());
    this.install(new DefaultUtilsModule());
    this.install(new GsonModule());
    this.install(new MapDbKeyValueStoreModule());
    this.install(new PircBotXChatBotModule());
  }

  @Provides
  @Singleton
  TwitchClient provideTwitchClient(final ChatBotConfiguration chatBotConfiguration,
                                   final TwitchClientFactory twitchClientFactory) {
    return twitchClientFactory.createTwitchClient(chatBotConfiguration.getTwitchClientId());
  }

}
