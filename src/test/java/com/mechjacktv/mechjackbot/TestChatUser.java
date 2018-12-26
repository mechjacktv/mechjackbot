package com.mechjacktv.mechjackbot;

import java.util.function.Function;

import com.mechjacktv.testframework.ArbitraryDataGenerator;
import com.mechjacktv.twitchclient.TwitchLogin;

public class TestChatUser implements ChatUser {

  private TwitchLogin twitchLogin;
  private Function<UserRole, Boolean> hasUserRoleHandler;

  public TestChatUser(final ArbitraryDataGenerator arbitraryDataGenerator) {
    this.twitchLogin = TwitchLogin.of(arbitraryDataGenerator.getString());
    this.hasUserRoleHandler = UserRole.VIEWER::equals;
  }

  @Override
  public TwitchLogin getTwitchLogin() {
    return this.twitchLogin;
  }

  @Override
  public boolean hasUserRole(final UserRole userRole) {
    return this.hasUserRoleHandler.apply(userRole);
  }

  public void setHasUserRoleHandler(final Function<UserRole, Boolean> hasUserRoleHandler) {
    this.hasUserRoleHandler = hasUserRoleHandler;
  }

  public void setTwitchLogin(final TwitchLogin twitchLogin) {
    this.twitchLogin = twitchLogin;
  }

}
