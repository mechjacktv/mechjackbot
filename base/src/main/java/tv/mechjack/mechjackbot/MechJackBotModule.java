package tv.mechjack.mechjackbot;

import com.google.inject.AbstractModule;

import tv.mechjack.mechjackbot.base.CoreModule;
import tv.mechjack.mechjackbot.chatbot.kicl.KiclChatBotModule;
import tv.mechjack.twitchclient.TwitchClientModule;

public class MechJackBotModule extends AbstractModule {

  @Override
  protected void configure() {
    this.install(new CoreModule());
    this.install(new KiclChatBotModule());
    this.install(new TwitchClientModule());
  }

}
