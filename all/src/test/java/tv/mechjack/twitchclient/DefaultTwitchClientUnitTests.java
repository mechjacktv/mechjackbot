package tv.mechjack.twitchclient;

import tv.mechjack.util.TestUtilModule;

public class DefaultTwitchClientUnitTests extends TwitchClientContractTests {

  @Override
  protected void installModules() {
    super.installModules();
    this.testFrameworkRule.installModule(new TestUtilModule());
  }

  @Override
  protected TwitchClient givenASubjectToTest() {
    return new DefaultTwitchClient(this.testFrameworkRule.getInstance(TwitchUsersEndpoint.class),
        this.testFrameworkRule.getInstance(TwitchUsersFollowsEndpoint.class));
  }

}
