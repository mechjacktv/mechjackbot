package tv.mechjack.mechjackbot.feature.autotrigger;

import com.google.inject.Scopes;

import tv.mechjack.mechjackbot.api.FeatureModule;

public class AutoTriggerFeatureModule extends FeatureModule {

  @Override
  protected void configure() {
    this.bind(AutoTriggerDataStore.class).to(DefaultAutoTriggerDataStore.class)
        .in(Scopes.SINGLETON);
    this.bind(AutoTriggerService.class).to(DefaultAutoTriggerService.class)
        .in(Scopes.SINGLETON);

    this.bindChatCommand(AutoTriggerDeleteChatCommand.class);
    this.bindChatCommand(AutoTriggerListChatCommand.class);
    this.bindChatCommand(AutoTriggerListenerChatCommand.class);
    this.bindChatCommand(AutoTriggerSetChatCommand.class);
  }

}
