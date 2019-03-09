package tv.mechjack.mechjackbot.feature.linkmoderation;

import com.google.inject.Scopes;

import tv.mechjack.mechjackbot.api.FeatureModule;

public class LinkModerationFeatureModule extends FeatureModule {

  @Override
  protected void configure() {
    this.bind(LinkModeratorService.class)
        .to(DefaultLinkModeratorService.class)
        .in(Scopes.SINGLETON);

    this.bindChatCommand(LinkModeratorChatCommand.class);
    this.bindChatCommand(PermitChatCommand.class);
  }

}
