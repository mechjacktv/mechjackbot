package com.mechjacktv.mechjackbot.configuration;

import com.google.inject.AbstractModule;

import com.mechjacktv.mechjackbot.AppConfiguration;
import com.mechjacktv.mechjackbot.ChatBotConfiguration;
import com.mechjacktv.twitchclient.TwitchClientConfiguration;

public final class DefaultConfigurationModule extends AbstractModule {

  @Override
  protected void configure() {
    this.bind(AppConfiguration.class).to(KeyValueStoreAppConfiguration.class).asEagerSingleton();
    this.bind(PropertiesChatBotConfiguration.class).asEagerSingleton();
    this.bind(ChatBotConfiguration.class).to(PropertiesChatBotConfiguration.class);
    this.bind(TwitchClientConfiguration.class).to(PropertiesChatBotConfiguration.class);
  }

}
