package tv.mechjack.mechjackbot.feature.linkmoderation;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;

public class TestLinkModerationFeatureModule extends AbstractModule {

  @Override
  protected void configure() {
    this.bind(LinkModeratorService.class).to(TestLinkModeratorService.class)
        .in(Scopes.SINGLETON);
  }

}
