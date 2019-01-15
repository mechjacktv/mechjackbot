package tv.mechjack.mechjackbot.feature.wouldyourather;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.google.inject.multibindings.Multibinder;

import tv.mechjack.mechjackbot.api.ChatCommand;

public class WouldYouRatherCommandModule extends AbstractModule {

  @Override
  protected void configure() {
    Multibinder.newSetBinder(this.binder(), ChatCommand.class).addBinding().to(WouldYouRatherChatCommand.class)
        .in(Scopes.SINGLETON);
  }

}
