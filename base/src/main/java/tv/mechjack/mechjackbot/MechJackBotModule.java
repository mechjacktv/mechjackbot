package tv.mechjack.mechjackbot;

import com.google.inject.AbstractModule;

import tv.mechjack.mechjackbot.base.BaseModule;
import tv.mechjack.mechjackbot.chatbot.kicl.KiclChatBotModule;
import tv.mechjack.mechjackbot.webapp.WebApplicationModule;
import tv.mechjack.twitchclient.TwitchClientModule;

public class MechJackBotModule extends AbstractModule {

  @Override
  protected void configure() {
    this.install(new BaseModule());
    this.install(new KiclChatBotModule());
    this.install(new TwitchClientModule());
    this.install(new WebApplicationModule());
  }

}
