package tv.mechjack.mechjackbot.feature.shoutout;

import com.google.inject.Scopes;

import tv.mechjack.mechjackbot.api.FeatureModule;

public final class ShoutOutFeatureModule extends FeatureModule {

  @Override
  protected final void configure() {
    this.bind(ShoutOutDataStore.class).to(DefaultShoutOutDataStore.class).in(Scopes.SINGLETON);
    this.bindChatCommand(ShoutOutChatCommand.class);
    this.bindChatCommand(ShoutOutListenerChatCommand.class);
  }

}
