package tv.mechjack.mechjackbot.api;

import java.util.function.Function;

import javax.inject.Inject;

import tv.mechjack.testframework.ArbitraryData;
import tv.mechjack.twitchclient.TwitchLogin;

public class TestChatUser implements ChatUser {

  private TwitchLogin twitchLogin;
  private Function<UserRole, Boolean> hasAccessLevelHandler;

  @Inject
  public TestChatUser(final ArbitraryData arbitraryDataGenerator) {
    this.twitchLogin = TwitchLogin.of(arbitraryDataGenerator.getString());
    this.hasAccessLevelHandler = UserRole.VIEWER::equals;
  }

  @Override
  public TwitchLogin getTwitchLogin() {
    return this.twitchLogin;
  }

  @Override
  public boolean hasAccessLevel(final UserRole userRole) {
    return this.hasAccessLevelHandler.apply(userRole);
  }

  public void setHasAccessLevelHandler(final Function<UserRole, Boolean> hasAccessLevelHandler) {
    this.hasAccessLevelHandler = hasAccessLevelHandler;
  }

  public void setTwitchLogin(final TwitchLogin twitchLogin) {
    this.twitchLogin = twitchLogin;
  }

}
