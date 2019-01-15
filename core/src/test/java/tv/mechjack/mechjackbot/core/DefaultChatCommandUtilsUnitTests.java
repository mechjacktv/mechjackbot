package tv.mechjack.mechjackbot.core;

import tv.mechjack.mechjackbot.api.ChatCommandUtils;
import tv.mechjack.mechjackbot.api.ChatCommandUtilsContractTests;
import tv.mechjack.platform.configuration.Configuration;
import tv.mechjack.platform.util.ExecutionUtils;
import tv.mechjack.platform.util.TimeUtils;

public class DefaultChatCommandUtilsUnitTests extends ChatCommandUtilsContractTests {

  @Override
  protected ChatCommandUtils givenASubjectToTest() {
    return new DefaultChatCommandUtils(this.testFrameworkRule.getInstance(Configuration.class),
        this.testFrameworkRule.getInstance(ExecutionUtils.class), this.testFrameworkRule.getInstance(TimeUtils.class));
  }

}
