package tv.mechjack.mechjackbot.feature.autotrigger;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;

public class TestAutoTriggerFeatureModule extends AbstractModule {

  @Override
  protected void configure() {
    this.bind(AutoTriggerDataStore.class).to(DefaultAutoTriggerDataStore.class)
        .in(Scopes.SINGLETON);
    this.bind(AutoTriggerService.class).to(DefaultAutoTriggerService.class)
        .in(Scopes.SINGLETON);
  }

}
