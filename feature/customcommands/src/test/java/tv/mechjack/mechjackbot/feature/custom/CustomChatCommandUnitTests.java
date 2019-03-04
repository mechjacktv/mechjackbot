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
    return this.givenASubjectToTest(
        CommandBody.of(this.testFramework.arbitraryData().getString()));
  }

  private CustomChatCommand givenASubjectToTest(final CommandBody commandBody) {
    return new CustomChatCommand(
        this.testFramework.getInstance(ChatCommandUtils.class),
        ChatCommandTrigger
            .of(this.testFramework.arbitraryData().getString()),
        commandBody,
        ChatCommandDescription
            .of(this.testFramework.arbitraryData().getString()),
        UserRole.VIEWER);
  }

  @Test
  public final void handleMessageEvent_noArgumentsRequired_resultIsChatMessage() {
    this.registerModules();
    final String bodyPart = this.testFramework.arbitraryData().getString();
    final CommandBody commandBody = CommandBody.of(bodyPart);
    final TestChatMessageEvent messageEvent = this.testFramework.getInstance(TestChatMessageEvent.class);
    final CustomChatCommand subjectUnderTest = this.givenASubjectToTest(commandBody);
    messageEvent.setChatMessage(ChatMessage.of(
        String.format("%s", subjectUnderTest.getTrigger())));

    subjectUnderTest.handleMessageEvent(messageEvent);
    final ChatMessage result = messageEvent.getResponseChatMessage();

    assertThat(result).isEqualTo(ChatMessage.of(bodyPart));
  }

  @Test
  public final void handleMessageEvent_calledWithArguments_resultIsChatMessageWithReplacedArguments() {
    this.registerModules();
    final String bodyPart = this.testFramework.arbitraryData().getString();
    final String argumentName = this.testFramework.arbitraryData()
        .getString();
    final CommandBody commandBody = CommandBody
        .of(String.format("%s ${%s}", bodyPart, argumentName));
    final TestChatMessageEvent messageEvent = this.testFramework
        .getInstance(TestChatMessageEvent.class);
    final String argumentValue = this.testFramework.arbitraryData()
        .getString();
    final CustomChatCommand subjectUnderTest = this
        .givenASubjectToTest(commandBody);
    messageEvent.setChatMessage(ChatMessage.of(String
        .format("%s %s", subjectUnderTest.getTrigger(), argumentValue)));

    subjectUnderTest.handleMessageEvent(messageEvent);
    final ChatMessage result = messageEvent.getResponseChatMessage();

    assertThat(result).isEqualTo(
        ChatMessage.of(String.format("%s %s", bodyPart, argumentValue)));
  }

  @Test
  public final void handleMessageEvent_duplicateArgument_resultIsChatMessageWithReplacedArguments() {
    this.registerModules();
    final String bodyPart = this.testFramework.arbitraryData().getString();
    final String argumentName = this.testFramework.arbitraryData()
        .getString();
    final CommandBody commandBody = CommandBody
        .of(String.format("%s ${%2$s} ${%2$s}", bodyPart, argumentName));
    final TestChatMessageEvent messageEvent = this.testFramework
        .getInstance(TestChatMessageEvent.class);
    final String argumentValue = this.testFramework.arbitraryData()
        .getString();
    final CustomChatCommand subjectUnderTest = this
        .givenASubjectToTest(commandBody);
    messageEvent.setChatMessage(ChatMessage.of(String
        .format("%s %s", subjectUnderTest.getTrigger(), argumentValue)));

    subjectUnderTest.handleMessageEvent(messageEvent);
    final ChatMessage result = messageEvent.getResponseChatMessage();

    assertThat(result).isEqualTo(
        ChatMessage.of(String.format("%s %2$s %2$s", bodyPart, argumentValue)));
  }

  @Test
  public final void handleMessageEvent_multipleArguments_resultIsChatMessageWithReplacedArguments() {
    this.registerModules();
    final String bodyPart = this.testFramework.arbitraryData().getString();
    final String argName1 = this.testFramework.arbitraryData().getString();
    final String argName2 = this.testFramework.arbitraryData().getString();
    final CommandBody commandBody = CommandBody.of(
        String.format("%s ${%s} ${%s}", bodyPart, argName1, argName2));
    final TestChatMessageEvent messageEvent = this.testFramework.getInstance(TestChatMessageEvent.class);
    final String argValue1 = this.testFramework.arbitraryData().getString();
    final String argValue2 = this.testFramework.arbitraryData().getString();
    final CustomChatCommand subjectUnderTest = this.givenASubjectToTest(commandBody);
    messageEvent.setChatMessage(ChatMessage.of(
        String.format("%s %s %s", subjectUnderTest.getTrigger(), argValue1,
            argValue2)));

    subjectUnderTest.handleMessageEvent(messageEvent);
    final ChatMessage result = messageEvent.getResponseChatMessage();

    assertThat(result).isEqualTo(ChatMessage.of(
        String.format("%s %s %s", bodyPart, argValue1, argValue2)));
  }

  @Test
  public final void handleMessageEvent_emptyArgumentsArgumentsRequired_resultIsUsageMessage() {
    this.registerModules();
    final String bodyPart = this.testFramework.arbitraryData().getString();
    final String argumentName1 = this.testFramework.arbitraryData()
        .getString();
    final String argumentName2 = this.testFramework.arbitraryData()
        .getString();
    final CommandBody commandBody = CommandBody
        .of(String.format("%s ${%s} ${%s}", bodyPart, argumentName1,
            argumentName2));
    final TestChatMessageEvent messageEvent = this.testFramework
        .getInstance(TestChatMessageEvent.class);
    final CustomChatCommand subjectUnderTest = this
        .givenASubjectToTest(commandBody);
    messageEvent.setChatMessage(
        ChatMessage.of(String.format("%s", subjectUnderTest.getTrigger())));

    subjectUnderTest.handleMessageEvent(messageEvent);
    final ChatMessage result = messageEvent.getResponseChatMessage();

    final ChatCommandUtils commandUtils = this.testFramework
        .getInstance(ChatCommandUtils.class);
    final ChatMessage usageMessage = commandUtils
        .createUsageMessage(subjectUnderTest, messageEvent);
    assertThat(result)
        .isEqualTo(commandUtils
            .replaceChatMessageVariables(subjectUnderTest, messageEvent,
                usageMessage));
  }

  @Test
  public final void handleMessageEvent_missingArgument_resultIsUsageMessage() {
    this.registerModules();
    final String bodyPart = this.testFramework.arbitraryData().getString();
    final String argName1 = this.testFramework.arbitraryData().getString();
    final String argName2 = this.testFramework.arbitraryData().getString();
    final CommandBody commandBody = CommandBody.of(
        String.format("%s ${%s} ${%s}", bodyPart, argName1, argName2));
    final TestChatMessageEvent messageEvent = this.testFramework.getInstance(TestChatMessageEvent.class);
    final String argValue = this.testFramework.arbitraryData().getString();
    final CustomChatCommand subjectUnderTest = this.givenASubjectToTest(commandBody);
    messageEvent.setChatMessage(ChatMessage.of(
        String.format("%s %s", subjectUnderTest.getTrigger(), argValue)));

    subjectUnderTest.handleMessageEvent(messageEvent);
    final ChatMessage result = messageEvent.getResponseChatMessage();

    final ChatCommandUtils commandUtils = this.testFramework.getInstance(ChatCommandUtils.class);
    final ChatMessage usageMessage = commandUtils.createUsageMessage(subjectUnderTest, messageEvent);
    assertThat(result).isEqualTo(
        commandUtils.replaceChatMessageVariables(subjectUnderTest, messageEvent,
            usageMessage));
  }

}
