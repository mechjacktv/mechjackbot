package com.mechjacktv.mechjackbot.configuration;

import com.google.inject.AbstractModule;
import com.mechjacktv.mechjackbot.AppConfiguration;
import com.mechjacktv.mechjackbot.ChatBotConfiguration;

public final class DefaultConfigurationModule extends AbstractModule {

  @Override
  protected void configure() {
    bind(AppConfiguration.class).to(KeyValueStoreAppConfiguration.class).asEagerSingleton();
    bind(ChatBotConfiguration.class).to(PropertiesChatBotConfiguration.class).asEagerSingleton();
  }

}
