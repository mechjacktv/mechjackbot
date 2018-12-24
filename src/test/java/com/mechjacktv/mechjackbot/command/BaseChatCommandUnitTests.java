package com.mechjacktv.mechjackbot.command;

import com.google.common.collect.Sets;
import com.mechjacktv.configuration.ConfigurationKey;
import com.mechjacktv.configuration.MapConfiguration;
import com.mechjacktv.mechjackbot.ChatCommandDescription;
import com.mechjacktv.mechjackbot.ChatCommandTrigger;
import com.mechjacktv.mechjackbot.ChatMessage;
import com.mechjacktv.mechjackbot.TestChatCommand;
import com.mechjacktv.mechjackbot.TestChatMessageEvent;
import com.mechjacktv.mechjackbot.TestCommandConfigurationBuilder;

import org.junit.Test;

import picocli.CommandLine.Model.OptionSpec;
import picocli.CommandLine.Model.PositionalParamSpec;

import static com.mechjacktv.mechjackbot.TestChatCommand.DEFAULT_DESCRIPTION;
import static com.mechjacktv.mechjackbot.TestChatCommand.DEFAULT_TRIGGER;
import static com.mechjacktv.mechjackbot.TestChatCommand.KEY_DESCRIPTION;
import static com.mechjacktv.mechjackbot.TestChatCommand.KEY_MESSAGE_FORMAT;
import static com.mechjacktv.mechjackbot.TestChatCommand.KEY_TRIGGER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

public class BaseChatCommandUnitTests extends BaseChatCommandContractTests {

  @Override
  protected final TestChatCommand givenASubjectToTest() {
    return this.givenASubjectToTest(this.getMessageFormatDefault());
  }

  protected final TestChatCommand givenASubjectToTest(final CommandMessageFormat defaultMessageFormat) {
    final TestCommandConfigurationBuilder builder = new TestCommandConfigurationBuilder(
        this.testFrameworkRule.getInstance(CommandConfigurationBuilder.class));
    builder.setDefaultMessageFormat(defaultMessageFormat.value);

    return new TestChatCommand(builder);
  }

  @Override
  protected ChatCommandDescription getDescriptionDefault() {
    return ChatCommandDescription.of(DEFAULT_DESCRIPTION);
  }

  @Override
  protected ConfigurationKey getDescriptionKey() {
    return ConfigurationKey.of(KEY_DESCRIPTION, TestChatCommand.class);
  }

  @Override
  protected ChatCommandTrigger getTriggerDefault() {
    return ChatCommandTrigger.of(DEFAULT_TRIGGER);
  }

  @Override
  protected ConfigurationKey getTriggerKey() {
    return ConfigurationKey.of(KEY_TRIGGER, TestChatCommand.class);
  }

  private CommandMessageFormat getMessageFormatDefault() {
    return CommandMessageFormat.of("%s %s %s %s");
  }

  private ConfigurationKey getMessageFormatKey() {
    return ConfigurationKey.of(KEY_MESSAGE_FORMAT, TestChatCommand.class);
  }

  @Test
  public final void handleMessageEvent_sendUsage_resultIsUsageMessage() {
    this.installModules();
    final TestChatMessageEvent messageEvent = this.testFrameworkRule.getInstance(TestChatMessageEvent.class);
    final TestChatCommand subjectUnderTest = this.givenASubjectToTest();
    subjectUnderTest.setMessageEventHandler(subjectUnderTest::sendUsage);

    subjectUnderTest.handleMessageEvent(messageEvent);
    final ChatMessage result = messageEvent.getResponseChatMessage();

    this.assertUsageMessageForCommand(result, subjectUnderTest, messageEvent);
  }

  @Test
  public final void handleMessageEvent_sendResponseWithNullMessageFormat_throwsNullPointerException() {
    this.installModules();
    final TestChatMessageEvent messageEvent = this.testFrameworkRule.getInstance(TestChatMessageEvent.class);
    final TestChatCommand subjectUnderTest = this.givenASubjectToTest();
    subjectUnderTest.setMessageEventHandler(event -> subjectUnderTest.sendResponse(event, (CommandMessageFormat) null));

    final Throwable thrown = catchThrowable(() -> subjectUnderTest.handleMessageEvent(messageEvent));

    this.testFrameworkRule.assertNullPointerException(thrown, "messageFormat");
  }

  @Test
  public final void handleMessageEvent_noMessageFormatConfigured_resultIsDefaultMessage() {
    this.installModules();
    final Object[] responseArgs = new Object[] { this.testFrameworkRule.getArbitraryString(),
        this.testFrameworkRule.getArbitraryString(), this.testFrameworkRule.getArbitraryString() };
    final TestChatMessageEvent messageEvent = this.testFrameworkRule.getInstance(TestChatMessageEvent.class);
    final TestChatCommand subjectUnderTest = this.givenASubjectToTest();
    subjectUnderTest.setMessageEventHandler(event -> subjectUnderTest.sendResponse(event, responseArgs));

    subjectUnderTest.handleMessageEvent(messageEvent);
    final ChatMessage result = messageEvent.getResponseChatMessage();

    assertThat(result).isEqualTo(ChatMessage.of(String.format(subjectUnderTest.getDefaultMessageFormat().value,
        messageEvent.getChatUser().getTwitchLogin(), responseArgs[0], responseArgs[1], responseArgs[2])));
  }

  @Test
  public final void handleMessageEvent_customMessageFormatConfigured_resultIsCustomMessage() {
    this.installModules();
    final String customMessageFormat = "%s";
    final MapConfiguration configuration = this.testFrameworkRule.getInstance(MapConfiguration.class);
    configuration.set(this.getMessageFormatKey(), customMessageFormat);
    final TestChatMessageEvent messageEvent = this.testFrameworkRule.getInstance(TestChatMessageEvent.class);
    final TestChatCommand subjectUnderTest = this.givenASubjectToTest();
    subjectUnderTest.setMessageEventHandler(event -> subjectUnderTest.sendResponse(event));

    subjectUnderTest.handleMessageEvent(messageEvent);
    final ChatMessage result = messageEvent.getResponseChatMessage();

    assertThat(result).isEqualTo(ChatMessage.of(String.format(customMessageFormat,
        messageEvent.getChatUser().getTwitchLogin())));
  }

  @Test
  public final void parseArguments_noMessageBody_resultIsFalse() {
    this.installModules();
    final PositionalParamSpec stringListParam = PicoCliUtil.createStringListParam(false, "0..*");

    final TestChatCommand subjectUnderTest = this.givenASubjectToTest();
    final TestChatMessageEvent chatMessageEvent = this.testFrameworkRule.getInstance(TestChatMessageEvent.class);
    chatMessageEvent.setChatMessage(ChatMessage.of(subjectUnderTest.getTrigger().value));

    final boolean result = subjectUnderTest.parseArguments(Sets.newHashSet(stringListParam),
        chatMessageEvent,
        parseResult -> true);

    assertThat(result).isFalse();
  }

  @Test
  public final void parseArguments_properlyFormattedCommand_resultIsTrue() {
    this.installModules();
    final OptionSpec stringOption = PicoCliUtil.createStringOption(true, "-r");
    final PositionalParamSpec stringParam = PicoCliUtil.createStringParam(true, "0");
    final PositionalParamSpec stringListParam = PicoCliUtil.createStringListParam(true, "1..*");

    final TestChatCommand subjectUnderTest = this.givenASubjectToTest();
    final TestChatMessageEvent chatMessageEvent = this.testFrameworkRule.getInstance(TestChatMessageEvent.class);
    chatMessageEvent.setChatMessage(ChatMessage.of(String.format("%s %s -r %s %s", subjectUnderTest.getTrigger(),
        this.testFrameworkRule.getArbitraryString(), this.testFrameworkRule.getArbitraryString(),
        this.testFrameworkRule.getArbitraryString())));

    final boolean result = subjectUnderTest.parseArguments(Sets.newHashSet(stringOption, stringParam, stringListParam),
        chatMessageEvent,
        parseResult -> true);

    assertThat(result).isTrue();
  }

  @Test
  public final void parseArguments_improperlyFormattedCommand_resultIsFalse() {
    this.installModules();
    final OptionSpec stringOption = PicoCliUtil.createStringOption(true, "-r");
    final PositionalParamSpec stringParam = PicoCliUtil.createStringParam(true, "0");
    final PositionalParamSpec stringListParam = PicoCliUtil.createStringListParam(true, "1..*");

    final TestChatCommand subjectUnderTest = this.givenASubjectToTest();
    final TestChatMessageEvent chatMessageEvent = this.testFrameworkRule.getInstance(TestChatMessageEvent.class);
    chatMessageEvent.setChatMessage(ChatMessage.of(String.format("%s %s %s", subjectUnderTest.getTrigger(),
        this.testFrameworkRule.getArbitraryString(), this.testFrameworkRule.getArbitraryString())));

    final boolean result = subjectUnderTest.parseArguments(Sets.newHashSet(stringOption, stringParam, stringListParam),
        chatMessageEvent,
        parseResult -> true);

    assertThat(result).isFalse();
  }

}
