package com.mechjacktv.mechjackbot.command;

import com.mechjacktv.mechjackbot.ChatCommandRegistryContractTests;
import com.mechjacktv.util.ExecutionUtils;

public class DefaultChatCommandRegistryUnitTests extends ChatCommandRegistryContractTests {

  @Override
  protected DefaultChatCommandRegistry givenASubjectToTest() {
    return new DefaultChatCommandRegistry(this.testFrameworkRule.getInstance(ExecutionUtils.class));
  }

}
