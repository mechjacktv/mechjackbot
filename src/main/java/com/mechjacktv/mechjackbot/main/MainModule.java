package com.mechjacktv.mechjackbot.main;

import com.google.inject.AbstractModule;

import com.mechjacktv.gson.GsonModule;
import com.mechjacktv.keyvaluestore.KeyValueStoreModule;
import com.mechjacktv.mechjackbot.chatbot.PircBotXChatBotModule;
import com.mechjacktv.mechjackbot.command.CommandsModule;
import com.mechjacktv.mechjackbot.configuration.ConfigurationModule;
import com.mechjacktv.twitchclient.TwitchClientModule;
import com.mechjacktv.util.UtilsModule;
import com.mechjacktv.util.scheduleservice.ScheduleServiceModule;

final class MainModule extends AbstractModule {

  @Override
  protected final void configure() {
    this.install(new CommandsModule());
    this.install(new ConfigurationModule());
    this.install(new GsonModule());
    this.install(new KeyValueStoreModule());
    this.install(new PircBotXChatBotModule());
    this.install(new ScheduleServiceModule());
    this.install(new TwitchClientModule());
    this.install(new UtilsModule());
  }

}
