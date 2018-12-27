package tv.mechjack.mechjackbot.chatbot;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;

import tv.mechjack.configuration.Configuration;
import tv.mechjack.mechjackbot.ChatBotConfiguration;
import tv.mechjack.twitchclient.TwitchClientConfiguration;

public class ChatBotModule extends AbstractModule {

  @Override
  protected void configure() {
    this.bind(DefaultChatBotConfiguration.class).in(Scopes.SINGLETON);
    this.bind(ChatBotConfiguration.class).to(DefaultChatBotConfiguration.class);
    this.bind(TwitchClientConfiguration.class).to(DefaultChatBotConfiguration.class);
    this.bind(Configuration.class).to(DefaultConfiguration.class).in(Scopes.SINGLETON);
  }

}
