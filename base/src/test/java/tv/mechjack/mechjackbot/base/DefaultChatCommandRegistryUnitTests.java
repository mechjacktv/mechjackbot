package tv.mechjack.mechjackbot.base;

import tv.mechjack.mechjackbot.api.ChatCommandRegistryContractTests;
import tv.mechjack.platform.utils.ExecutionUtils;

public class DefaultChatCommandRegistryUnitTests extends ChatCommandRegistryContractTests {

  @Override
  protected DefaultChatCommandRegistry givenASubjectToTest() {
    return new DefaultChatCommandRegistry(this.testFrameworkRule.getInstance(ExecutionUtils.class));
  }

}
