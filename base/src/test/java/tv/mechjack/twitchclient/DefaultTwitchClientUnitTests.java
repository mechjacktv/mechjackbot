package tv.mechjack.twitchclient;

import tv.mechjack.platform.utils.TestUtilsModule;

public class DefaultTwitchClientUnitTests extends TwitchClientContractTests {

  @Override
  protected void installModules() {
    super.installModules();
    this.testFrameworkRule.installModule(new TestUtilsModule());
  }

  @Override
  protected TwitchClient givenASubjectToTest() {
    return new DefaultTwitchClient(this.testFrameworkRule.getInstance(TwitchUsersEndpoint.class),
        this.testFrameworkRule.getInstance(TwitchUsersFollowsEndpoint.class));
  }

}
