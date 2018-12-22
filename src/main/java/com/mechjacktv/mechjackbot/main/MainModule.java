package com.mechjacktv.mechjackbot.main;

import com.google.inject.AbstractModule;
import com.mechjacktv.gson.GsonModule;
import com.mechjacktv.keyvaluestore.KeyValueStoreModule;
import com.mechjacktv.mechjackbot.chatbot.kicl.KiclChatBotModule;
import com.mechjacktv.mechjackbot.command.CommandModule;
import com.mechjacktv.mechjackbot.command.custom.CustomCommandModule;
import com.mechjacktv.mechjackbot.command.shoutout.ShoutOutCommandModule;
import com.mechjacktv.twitchclient.TwitchClientModule;
import com.mechjacktv.util.UtilModule;
import com.mechjacktv.util.scheduleservice.ScheduleServiceModule;

final class MainModule extends AbstractModule {

  @Override
  protected final void configure() {
    this.install(new CommandModule());
    this.install(new CustomCommandModule());
    this.install(new GsonModule());
    this.install(new KeyValueStoreModule());
    this.install(new KiclChatBotModule());
    // this.install(new PircBotXChatBotModule());
    this.install(new ScheduleServiceModule());
    this.install(new ShoutOutCommandModule());
    this.install(new TwitchClientModule());
    this.install(new UtilModule());
  }

}
