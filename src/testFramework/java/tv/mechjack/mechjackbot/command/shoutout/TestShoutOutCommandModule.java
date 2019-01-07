package tv.mechjack.mechjackbot.command.shoutout;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;

public class TestShoutOutCommandModule extends AbstractModule {

  @Override
  protected void configure() {
    this.bind(ShoutOutDataStore.class).to(DefaultShoutOutDataStore.class).in(Scopes.SINGLETON);
  }

}
