package com.mechjacktv.mechjackbot.main;

import com.google.inject.AbstractModule;

import com.mechjacktv.gson.GsonModule;
import com.mechjacktv.keyvaluestore.MapDbKeyValueStoreModule;
import com.mechjacktv.mechjackbot.chatbot.PircBotXChatBotModule;
import com.mechjacktv.mechjackbot.command.DefaultCommandsModule;
import com.mechjacktv.mechjackbot.configuration.DefaultConfigurationModule;
import com.mechjacktv.twitchclient.DefaultTwitchClientModule;
import com.mechjacktv.util.DefaultUtilsModule;
import com.mechjacktv.util.scheduleservice.DefaultScheduleServiceModule;

final class MainModule extends AbstractModule {

  @Override
  protected final void configure() {
    this.install(new DefaultCommandsModule());
    this.install(new DefaultConfigurationModule());
    this.install(new DefaultScheduleServiceModule());
    this.install(new DefaultTwitchClientModule());
    this.install(new DefaultUtilsModule());
    this.install(new GsonModule());
    this.install(new MapDbKeyValueStoreModule());
    this.install(new PircBotXChatBotModule());
  }

}
