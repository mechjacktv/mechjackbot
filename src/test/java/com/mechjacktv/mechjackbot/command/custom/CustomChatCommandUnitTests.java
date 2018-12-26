package com.mechjacktv.mechjackbot.command.custom;

import com.mechjacktv.mechjackbot.ChatCommandContractTests;
import com.mechjacktv.mechjackbot.ChatCommandTrigger;
import com.mechjacktv.mechjackbot.ChatCommandUtils;
import com.mechjacktv.mechjackbot.ChatMessage;
import com.mechjacktv.mechjackbot.TestChatMessageEvent;
import com.mechjacktv.mechjackbot.UserRole;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CustomChatCommandUnitTests extends ChatCommandContractTests {

  @Override
  protected CustomChatCommand givenASubjectToTest() {
    return this.givenASubjectToTest(CommandBody.of(this.testFrameworkRule.getArbitraryString()));
  }

  private CustomChatCommand givenASubjectToTest(final CommandBody commandBody) {
    return new CustomChatCommand(this.testFrameworkRule.getInstance(ChatCommandUtils.class),
        ChatCommandTrigger.of(this.testFrameworkRule.getArbitraryString()), commandBody, UserRole.VIEWER);
  }

  @Test
  public final void handleMessageEvent_whenCalled_resultIsCommandBody() {
    this.installModules();
    final CommandBody commandBody = CommandBody.of(this.testFrameworkRule.getArbitraryString());
    final TestChatMessageEvent messageEvent = this.testFrameworkRule.getInstance(TestChatMessageEvent.class);
    final CustomChatCommand subjectUnderTest = this.givenASubjectToTest(commandBody);

    subjectUnderTest.handleMessageEvent(messageEvent);
    final ChatMessage result = messageEvent.getResponseChatMessage();

    assertThat(result.value).isEqualTo(commandBody.value);
  }

}
