package com.mechjacktv.mechjackbot.command;

import com.mechjacktv.configuration.Configuration;
import com.mechjacktv.mechjackbot.CommandUtils;
import com.mechjacktv.mechjackbot.CommandUtilsContractTests;
import com.mechjacktv.util.ExecutionUtils;
import com.mechjacktv.util.TimeUtils;

public class DefaultCommandUtilsUnitTests extends CommandUtilsContractTests {

  @Override
  protected CommandUtils givenASubjectToTest() {
    return new DefaultCommandUtils(this.testFrameworkRule.getInstance(Configuration.class),
        this.testFrameworkRule.getInstance(ExecutionUtils.class), this.testFrameworkRule.getInstance(TimeUtils.class));
  }

}
