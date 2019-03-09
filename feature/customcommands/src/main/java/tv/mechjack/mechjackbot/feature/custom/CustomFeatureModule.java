package tv.mechjack.mechjackbot.feature.custom;

import com.google.inject.Scopes;

import tv.mechjack.mechjackbot.api.FeatureModule;

public class CustomFeatureModule extends FeatureModule {

  @Override
  protected void configure() {
    this.bind(CustomCommandDataStore.class).to(DefaultCustomCommandDataStore.class).in(Scopes.SINGLETON);
    this.bind(CustomChatCommandService.class).to(DefaultCustomChatCommandService.class).asEagerSingleton();
    this.bindChatCommand(SetCommandChatCommand.class);
    this.bindChatCommand(DeleteCommandChatCommand.class);
  }

}
