package com.mechjacktv.mechjackbot.command;

import com.mechjacktv.configuration.Configuration;
import com.mechjacktv.mechjackbot.ChatCommandUtilsContractTests;
import com.mechjacktv.mechjackbot.ChatCommandUtils;
import com.mechjacktv.util.ExecutionUtils;
import com.mechjacktv.util.TimeUtils;

public class DefaultChatCommandUtilsUnitTests extends ChatCommandUtilsContractTests {

  @Override
  protected ChatCommandUtils givenASubjectToTest() {
    return new DefaultChatCommandUtils(this.testFrameworkRule.getInstance(Configuration.class),
        this.testFrameworkRule.getInstance(ExecutionUtils.class), this.testFrameworkRule.getInstance(TimeUtils.class));
  }

}
