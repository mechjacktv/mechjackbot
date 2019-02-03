package tv.mechjack.mechjackbot.feature.wouldyourather;

import tv.mechjack.mechjackbot.api.FeatureModule;

public class WouldYouRatherFeatureModule extends FeatureModule {

  @Override
  protected void configure() {
    this.bindCommand(WouldYouRatherChatCommand.class);
  }

}
