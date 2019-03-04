package tv.mechjack.mechjackbot.feature.points;

import tv.mechjack.mechjackbot.api.FeatureModule;

public class PointsFeatureModule extends FeatureModule {

  @Override
  protected void configure() {
    this.bindCommand(PointsListenerChatCommand.class);
  }

}
