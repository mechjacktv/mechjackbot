package tv.mechjack.mechjackbot.feature.custom;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;

public class TestCustomCommandModule extends AbstractModule {

  @Override
  protected void configure() {
    this.bind(CustomChatCommandService.class).to(DefaultCustomChatCommandService.class).in(Scopes.SINGLETON);
    this.bind(CustomCommandDataStore.class).to(DefaultCustomCommandDataStore.class).in(Scopes.SINGLETON);
  }

}
