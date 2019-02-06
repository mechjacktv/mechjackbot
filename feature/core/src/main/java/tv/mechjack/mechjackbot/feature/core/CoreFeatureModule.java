package tv.mechjack.mechjackbot.feature.core;

import tv.mechjack.mechjackbot.api.FeatureModule;

public class CoreFeatureModule extends FeatureModule {

  @Override
  protected void configure() {
    this.bindCommand(CommandsChatCommand.class);
    this.bindCommand(HelpChatCommand.class);
    this.bindCommand(PingChatCommand.class);
    this.bindCommand(QuitChatCommand.class);
    this.bindCommand(UsageChatCommand.class);
  }

}
