package tv.mechjack.mechjackbot.core;

import tv.mechjack.mechjackbot.api.ChatCommandRegistryContractTests;
import tv.mechjack.util.ExecutionUtils;

public class DefaultChatCommandRegistryUnitTests extends ChatCommandRegistryContractTests {

  @Override
  protected DefaultChatCommandRegistry givenASubjectToTest() {
    return new DefaultChatCommandRegistry(this.testFrameworkRule.getInstance(ExecutionUtils.class));
  }

}
