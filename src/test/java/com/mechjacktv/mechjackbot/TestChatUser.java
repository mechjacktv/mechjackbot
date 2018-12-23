package com.mechjacktv.mechjackbot;

import java.util.function.Function;

import com.mechjacktv.testframework.ArbitraryDataGenerator;
import com.mechjacktv.twitchclient.TwitchLogin;

public class TestChatUser implements ChatUser {

  private TwitchLogin twitchLogin;
  private Function<UserRole, Boolean> accessLevelCheck;

  public TestChatUser(final ArbitraryDataGenerator arbitraryDataGenerator) {
    this.twitchLogin = TwitchLogin.of(arbitraryDataGenerator.getString());
    this.accessLevelCheck = (accessLevel -> false);
  }

  @Override
  public TwitchLogin getTwitchLogin() {
    return this.twitchLogin;
  }

  @Override
  public boolean hasUserRole(final UserRole userRole) {
    return accessLevelCheck.apply(userRole);
  }

  public void setAccessLevelCheck(final Function<UserRole, Boolean> accessLevelCheck) {
    this.accessLevelCheck = accessLevelCheck;
  }

  public void setTwitchLogin(final TwitchLogin twitchLogin) {
    this.twitchLogin = twitchLogin;
  }

}
