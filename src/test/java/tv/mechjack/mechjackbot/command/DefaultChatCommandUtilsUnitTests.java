package tv.mechjack.mechjackbot.command;

import tv.mechjack.configuration.Configuration;
import tv.mechjack.mechjackbot.ChatCommandUtils;
import tv.mechjack.mechjackbot.ChatCommandUtilsContractTests;
import tv.mechjack.util.ExecutionUtils;
import tv.mechjack.util.TimeUtils;

public class DefaultChatCommandUtilsUnitTests extends ChatCommandUtilsContractTests {

  @Override
  protected ChatCommandUtils givenASubjectToTest() {
    return new DefaultChatCommandUtils(this.testFrameworkRule.getInstance(Configuration.class),
        this.testFrameworkRule.getInstance(ExecutionUtils.class), this.testFrameworkRule.getInstance(TimeUtils.class));
  }

}
