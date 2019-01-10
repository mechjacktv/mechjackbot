package tv.mechjack.mechjackbot.command;

import tv.mechjack.mechjackbot.ChatCommandRegistryContractTests;
import tv.mechjack.util.ExecutionUtils;

public class DefaultChatCommandRegistryUnitTests extends ChatCommandRegistryContractTests {

  @Override
  protected DefaultChatCommandRegistry givenASubjectToTest() {
    return new DefaultChatCommandRegistry(this.testFrameworkRule.getInstance(ExecutionUtils.class));
  }

}
