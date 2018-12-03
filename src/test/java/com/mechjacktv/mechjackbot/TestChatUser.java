package com.mechjacktv.mechjackbot;

import java.util.function.Function;

import com.mechjacktv.testframework.ArbitraryDataGenerator;
import com.mechjacktv.twitchclient.TwitchLogin;

public class TestChatUser implements ChatUser {

  private TwitchLogin twitchLogin;
  private Function<AccessLevel, Boolean> accessLevelCheck;

  public TestChatUser(final ArbitraryDataGenerator arbitraryDataGenerator) {
    this.twitchLogin = TwitchLogin.of(arbitraryDataGenerator.getString());
    this.accessLevelCheck = (accessLevel -> false);
  }

  @Override
  public TwitchLogin getTwitchLogin() {
    return this.twitchLogin;
  }

  @Override
  public boolean hasAccessLevel(final AccessLevel accessLevel) {
    return accessLevelCheck.apply(accessLevel);
  }

  public void setAccessLevelCheck(final Function<AccessLevel, Boolean> accessLevelCheck) {
    this.accessLevelCheck = accessLevelCheck;
  }

  public void setTwitchLogin(final TwitchLogin twitchLogin) {
    this.twitchLogin = twitchLogin;
  }

}
