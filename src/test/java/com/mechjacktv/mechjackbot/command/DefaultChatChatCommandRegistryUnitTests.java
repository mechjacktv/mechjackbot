package com.mechjacktv.mechjackbot.command;

import com.mechjacktv.mechjackbot.ChatChatCommandRegistryContractTests;
import com.mechjacktv.util.ExecutionUtils;

public class DefaultChatChatCommandRegistryUnitTests extends ChatChatCommandRegistryContractTests {

  @Override
  protected DefaultChatCommandRegistry givenASubjectToTest() {
    return new DefaultChatCommandRegistry(this.testFrameworkRule.getInstance(ExecutionUtils.class));
  }

}
