package tv.mechjack.mechjackbot.main;

import com.google.inject.AbstractModule;

import tv.mechjack.gson.GsonModule;
import tv.mechjack.keyvaluestore.KeyValueStoreModule;
import tv.mechjack.mechjackbot.chatbot.kicl.KiclChatBotModule;
import tv.mechjack.mechjackbot.command.CommandModule;
import tv.mechjack.mechjackbot.command.custom.CustomCommandModule;
import tv.mechjack.mechjackbot.command.shoutout.ShoutOutCommandModule;
import tv.mechjack.twitchclient.TwitchClientModule;
import tv.mechjack.util.UtilModule;
import tv.mechjack.util.scheduleservice.ScheduleServiceModule;

final class MainModule extends AbstractModule {

  @Override
  protected final void configure() {
    this.install(new CommandModule());
    this.install(new CustomCommandModule());
    this.install(new GsonModule());
    this.install(new KeyValueStoreModule());
    this.install(new KiclChatBotModule());
    this.install(new ScheduleServiceModule());
    this.install(new ShoutOutCommandModule());
    this.install(new TwitchClientModule());
    this.install(new UtilModule());
  }

}
