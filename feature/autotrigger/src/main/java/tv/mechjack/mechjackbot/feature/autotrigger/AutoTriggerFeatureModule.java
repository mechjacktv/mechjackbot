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

    this.bindCommand(AutoTriggerDeleteChatCommand.class);
    this.bindCommand(AutoTriggerListChatCommand.class);
    this.bindCommand(AutoTriggerListenerChatCommand.class);
    this.bindCommand(AutoTriggerSetChatCommand.class);
  }

}
