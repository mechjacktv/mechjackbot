package tv.mechjack.mechjackbot.chatbot;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;

import tv.mechjack.mechjackbot.ChatBotConfiguration;

public class ChatBotTestModule extends AbstractModule {

  @Override
  protected void configure() {
    this.bind(ChatBotConfiguration.class).to(TestChatBotConfiguration.class).in(Scopes.SINGLETON);
  }

}