package tv.mechjack.mechjackbot.base;

import tv.mechjack.mechjackbot.api.ChatCommandUtils;
import tv.mechjack.mechjackbot.api.ChatCommandUtilsContractTests;
import tv.mechjack.platform.configuration.Configuration;
import tv.mechjack.platform.utils.ExecutionUtils;
import tv.mechjack.platform.utils.TimeUtils;

public class DefaultChatCommandUtilsUnitTests extends ChatCommandUtilsContractTests {

  @Override
  protected ChatCommandUtils givenASubjectToTest() {
    return new DefaultChatCommandUtils(this.testFramework.getInstance(Configuration.class),
        this.testFramework.getInstance(ExecutionUtils.class), this.testFramework
            .getInstance(TimeUtils.class));
  }

}
