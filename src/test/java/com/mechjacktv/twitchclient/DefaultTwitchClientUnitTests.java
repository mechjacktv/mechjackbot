package com.mechjacktv.twitchclient;

import com.mechjacktv.util.UtilTestModule;

public class DefaultTwitchClientUnitTests extends TwitchClientContractTests {

  @Override
  protected void installModules() {
    super.installModules();
    this.testFrameworkRule.installModule(new UtilTestModule());
  }

  @Override
  protected TwitchClient givenASubjectToTest() {
    return new DefaultTwitchClient(this.testFrameworkRule.getInstance(TwitchUsersEndpoint.class),
        this.testFrameworkRule.getInstance(TwitchUsersFollowsEndpoint.class));
  }

}
