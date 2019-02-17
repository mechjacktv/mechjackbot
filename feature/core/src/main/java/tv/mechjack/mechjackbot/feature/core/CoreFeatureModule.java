package tv.mechjack.mechjackbot.feature.core;

import tv.mechjack.mechjackbot.api.FeatureModule;
import tv.mechjack.platform.webserver.ControllerHandler;
import tv.mechjack.platform.webserver.WebApplication;

public class CoreFeatureModule extends FeatureModule {

  @Override
  protected void configure() {
    this.bindCommand(CommandsChatCommand.class);
    this.bindCommand(HelpChatCommand.class);
    this.bindCommand(PingChatCommand.class);
    this.bindCommand(QuitChatCommand.class);
    this.bindCommand(UsageChatCommand.class);

    // Multibinder.newSetBinder(this.binder(), WebApplication.class).addBinding()
    // .to(FeatureWebApplication.class).in(Scopes.SINGLETON);
  }

  private static class FeatureWebApplication implements WebApplication {

    @Override
    public String getContextPath() {
      return "/" + CoreFeatureModule.class.getCanonicalName();
    }

    @Override
    public String getResourceBase() {
      return "/Users/mechjack/Documents/mechjackbot/feature/core/src/main"
          + "/resources/public";
    }

    @Override
    public void registerControllers(final ControllerHandler controllerHandler) {
      /* no-op (2019-02-14 mechjack) */
    }

  }

}
