package tv.mechjack.mechjackbot.feature.custom;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import tv.mechjack.mechjackbot.api.ChatCommandContractTests;
import tv.mechjack.mechjackbot.api.ChatCommandDescription;
import tv.mechjack.mechjackbot.api.ChatCommandTrigger;
import tv.mechjack.mechjackbot.api.ChatCommandUtils;
import tv.mechjack.mechjackbot.api.ChatMessage;
import tv.mechjack.mechjackbot.api.TestChatMessageEvent;
import tv.mechjack.mechjackbot.api.UserRole;

public class CustomChatCommandUnitTests extends ChatCommandContractTests {

  @Override
  protected CustomChatCommand givenASubjectToTest() {
    return this.givenASubjectToTest(CommandBody.of(this.testFrameworkRule.getArbitraryString()));
  }

  private CustomChatCommand givenASubjectToTest(final CommandBody commandBody) {
    return new CustomChatCommand(this.testFrameworkRule.getInstance(ChatCommandUtils.class),
        ChatCommandTrigger.of(this.testFrameworkRule.getArbitraryString()), commandBody,
        ChatCommandDescription.of(this.testFrameworkRule.getArbitraryString()), UserRole.VIEWER);
  }

  @Test
  public final void handleMessageEvent_noArgumentsRequired_resultIsChatMessage() {
    this.installModules();
    final String bodyPart = this.testFrameworkRule.getArbitraryString();
    final CommandBody commandBody = CommandBody.of(bodyPart);
    final TestChatMessageEvent messageEvent = this.testFrameworkRule.getInstance(TestChatMessageEvent.class);
    final CustomChatCommand subjectUnderTest = this.givenASubjectToTest(commandBody);
    messageEvent.setChatMessage(ChatMessage.of(String.format("%s", subjectUnderTest.getTrigger())));

    subjectUnderTest.handleMessageEvent(messageEvent);
    final ChatMessage result = messageEvent.getResponseChatMessage();

    assertThat(result).isEqualTo(ChatMessage.of(bodyPart));
  }

  @Test
  public final void handleMessageEvent_calledWithArguments_resultIsChatMessageWithReplacedArguments() {
    this.installModules();
    final String bodyPart = this.testFrameworkRule.getArbitraryString();
    final String argumentName = this.testFrameworkRule.getArbitraryString();
    final CommandBody commandBody = CommandBody.of(String.format("%s ${%s}", bodyPart, argumentName));
    final TestChatMessageEvent messageEvent = this.testFrameworkRule.getInstance(TestChatMessageEvent.class);
    final String argumentValue = this.testFrameworkRule.getArbitraryString();
    final CustomChatCommand subjectUnderTest = this.givenASubjectToTest(commandBody);
    messageEvent.setChatMessage(ChatMessage.of(String.format("%s %s", subjectUnderTest.getTrigger(), argumentValue)));

    subjectUnderTest.handleMessageEvent(messageEvent);
    final ChatMessage result = messageEvent.getResponseChatMessage();

    assertThat(result).isEqualTo(ChatMessage.of(String.format("%s %s", bodyPart, argumentValue)));
  }

  @Test
  public final void handleMessageEvent_duplicateArgument_resultIsChatMessageWithReplacedArguments() {
    this.installModules();
    final String bodyPart = this.testFrameworkRule.getArbitraryString();
    final String argumentName = this.testFrameworkRule.getArbitraryString();
    final CommandBody commandBody = CommandBody.of(String.format("%s ${%2$s} ${%2$s}", bodyPart, argumentName));
    final TestChatMessageEvent messageEvent = this.testFrameworkRule.getInstance(TestChatMessageEvent.class);
    final String argumentValue = this.testFrameworkRule.getArbitraryString();
    final CustomChatCommand subjectUnderTest = this.givenASubjectToTest(commandBody);
    messageEvent.setChatMessage(ChatMessage.of(String.format("%s %s", subjectUnderTest.getTrigger(), argumentValue)));

    subjectUnderTest.handleMessageEvent(messageEvent);
    final ChatMessage result = messageEvent.getResponseChatMessage();

    assertThat(result).isEqualTo(ChatMessage.of(String.format("%s %2$s %2$s", bodyPart, argumentValue)));
  }

  @Test
  public final void handleMessageEvent_multipleArguments_resultIsChatMessageWithReplacedArguments() {
    this.installModules();
    final String bodyPart = this.testFrameworkRule.getArbitraryString();
    final String argumentName1 = this.testFrameworkRule.getArbitraryString();
    final String argumentName2 = this.testFrameworkRule.getArbitraryString();
    final CommandBody commandBody = CommandBody.of(String.format("%s ${%s} ${%s}", bodyPart, argumentName1,
        argumentName2));
    final TestChatMessageEvent messageEvent = this.testFrameworkRule.getInstance(TestChatMessageEvent.class);
    final String argumentValue1 = this.testFrameworkRule.getArbitraryString();
    final String argumentValue2 = this.testFrameworkRule.getArbitraryString();
    final CustomChatCommand subjectUnderTest = this.givenASubjectToTest(commandBody);
    messageEvent.setChatMessage(ChatMessage.of(String.format("%s %s %s", subjectUnderTest.getTrigger(),
        argumentValue1, argumentValue2)));

    subjectUnderTest.handleMessageEvent(messageEvent);
    final ChatMessage result = messageEvent.getResponseChatMessage();

    assertThat(result).isEqualTo(ChatMessage.of(String.format("%s %s %s", bodyPart, argumentValue1, argumentValue2)));
  }

  @Test
  public final void handleMessageEvent_emptyArgumentsArgumentsRequired_resultIsUsageMessage() {
    this.installModules();
    final String bodyPart = this.testFrameworkRule.getArbitraryString();
    final String argumentName1 = this.testFrameworkRule.getArbitraryString();
    final String argumentName2 = this.testFrameworkRule.getArbitraryString();
    final CommandBody commandBody = CommandBody.of(String.format("%s ${%s} ${%s}", bodyPart, argumentName1,
        argumentName2));
    final TestChatMessageEvent messageEvent = this.testFrameworkRule.getInstance(TestChatMessageEvent.class);
    final CustomChatCommand subjectUnderTest = this.givenASubjectToTest(commandBody);
    messageEvent.setChatMessage(ChatMessage.of(String.format("%s", subjectUnderTest.getTrigger())));

    subjectUnderTest.handleMessageEvent(messageEvent);
    final ChatMessage result = messageEvent.getResponseChatMessage();

    final ChatCommandUtils commandUtils = this.testFrameworkRule.getInstance(ChatCommandUtils.class);
    final ChatMessage usageMessage = commandUtils.createUsageMessage(subjectUnderTest, messageEvent);
    assertThat(result)
        .isEqualTo(commandUtils.replaceChatMessageVariables(subjectUnderTest, messageEvent, usageMessage));
  }

  @Test
  public final void handleMessageEvent_missingArgument_resultIsUsageMessage() {
    this.installModules();
    final String bodyPart = this.testFrameworkRule.getArbitraryString();
    final String argumentName1 = this.testFrameworkRule.getArbitraryString();
    final String argumentName2 = this.testFrameworkRule.getArbitraryString();
    final CommandBody commandBody = CommandBody.of(String.format("%s ${%s} ${%s}", bodyPart, argumentName1,
        argumentName2));
    final TestChatMessageEvent messageEvent = this.testFrameworkRule.getInstance(TestChatMessageEvent.class);
    final String argumentValue = this.testFrameworkRule.getArbitraryString();
    final CustomChatCommand subjectUnderTest = this.givenASubjectToTest(commandBody);
    messageEvent.setChatMessage(ChatMessage.of(String.format("%s %s", subjectUnderTest.getTrigger(), argumentValue)));

    subjectUnderTest.handleMessageEvent(messageEvent);
    final ChatMessage result = messageEvent.getResponseChatMessage();

    final ChatCommandUtils commandUtils = this.testFrameworkRule.getInstance(ChatCommandUtils.class);
    final ChatMessage usageMessage = commandUtils.createUsageMessage(subjectUnderTest, messageEvent);
    assertThat(result)
        .isEqualTo(commandUtils.replaceChatMessageVariables(subjectUnderTest, messageEvent, usageMessage));
  }

}
