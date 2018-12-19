package com.mechjacktv.mechjackbot.command;

import com.mechjacktv.mechjackbot.CommandRegistryContractTests;
import com.mechjacktv.util.ExecutionUtils;

public class DefaultCommandRegistryUnitTests extends CommandRegistryContractTests {

  @Override
  protected DefaultCommandRegistry givenASubjectToTest() {
    return new DefaultCommandRegistry(this.testFrameworkRule.getInstance(ExecutionUtils.class));
  }

}
