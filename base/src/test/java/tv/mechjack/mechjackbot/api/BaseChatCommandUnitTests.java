package tv.mechjack.mechjackbot.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static tv.mechjack.mechjackbot.api.TestChatCommand.DEFAULT_DESCRIPTION;
import static tv.mechjack.mechjackbot.api.TestChatCommand.DEFAULT_TRIGGER;
import static tv.mechjack.mechjackbot.api.TestChatCommand.KEY_DESCRIPTION;
import static tv.mechjack.mechjackbot.api.TestChatCommand.KEY_MESSAGE_FORMAT;
import static tv.mechjack.mechjackbot.api.TestChatCommand.KEY_TRIGGER;

import com.google.common.collect.Sets;

import org.junit.Test;

import picocli.CommandLine.Model.OptionSpec;
import picocli.CommandLine.Model.PositionalParamSpec;

import tv.mechjack.platform.configuration.ConfigurationKey;
import tv.mechjack.platform.configuration.MapConfiguration;
import tv.mechjack.platform.utils.ExecutionUtils;

public class BaseChatCommandUnitTests extends BaseChatCommandContractTests {

  @Override
  protected final TestChatCommand givenASubjectToTest() {
    return this.givenASubjectToTest(this.getMessageFormatDefault());
  }

  protected final TestChatCommand givenASubjectToTest(final CommandMessageFormat defaultMessageFormat) {
    final TestCommandConfigurationBuilder builder = new TestCommandConfigurationBuilder(
        this.testFramework.getInstance(CommandConfigurationBuilder.class));
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
    return CommandMessageFormat.of("%s %s %s");
  }

  private ConfigurationKey getMessageFormatKey() {
    return ConfigurationKey.of(KEY_MESSAGE_FORMAT, TestChatCommand.class);
  }

  @Test
  public final void handleMessageEvent_sendUsage_resultIsUsageMessage() {
    this.registerModules();
    final TestChatMessageEvent messageEvent = this.testFramework.getInstance(TestChatMessageEvent.class);
    final TestChatCommand subjectUnderTest = this.givenASubjectToTest();
    subjectUnderTest.setMessageEventHandler(subjectUnderTest::sendUsage);

    subjectUnderTest.handleMessageEvent(messageEvent);
    final ChatMessage result = messageEvent.getResponseChatMessage();

    this.assertUsageMessageForCommand(result, subjectUnderTest, messageEvent);
  }

  @Test
  public final void handleMessageEvent_sendResponseWithNullMessageFormat_throwsNullPointerException() {
    this.registerModules();
    final TestChatMessageEvent messageEvent = this.testFramework.getInstance(TestChatMessageEvent.class);
    final TestChatCommand subjectUnderTest = this.givenASubjectToTest();
    subjectUnderTest.setMessageEventHandler(event -> subjectUnderTest.sendResponse(event, (CommandMessageFormat) null));

    final Throwable thrown = catchThrowable(() -> subjectUnderTest.handleMessageEvent(messageEvent));

    assertThat(thrown).isInstanceOf(NullPointerException.class)
        .hasMessage(this.testFramework.getInstance(ExecutionUtils.class)
            .nullMessageForName("messageFormat"));
  }

  @Test
  public final void handleMessageEvent_noMessageFormatConfigured_resultIsDefaultMessage() {
    this.registerModules();
    final Object[] responseArgs = new Object[] { this.testFramework.arbitraryData().getString(),
        this.testFramework.arbitraryData().getString(), this.testFramework.arbitraryData().getString() };
    final TestChatMessageEvent messageEvent = this.testFramework.getInstance(TestChatMessageEvent.class);
    final TestChatCommand subjectUnderTest = this.givenASubjectToTest();
    subjectUnderTest.setMessageEventHandler(event -> subjectUnderTest.sendResponse(event, responseArgs));

    subjectUnderTest.handleMessageEvent(messageEvent);
    final ChatMessage result = messageEvent.getResponseChatMessage();

    assertThat(result).isEqualTo(ChatMessage.of(String.format(subjectUnderTest.getDefaultMessageFormat().value,
        responseArgs[0], responseArgs[1], responseArgs[2])));
  }

  @Test
  public final void handleMessageEvent_customMessageFormatConfigured_resultIsCustomMessage() {
    this.registerModules();
    final String customMessageFormat = "%s";
    final MapConfiguration configuration = this.testFramework.getInstance(MapConfiguration.class);
    configuration.set(this.getMessageFormatKey(), customMessageFormat);
    final Object[] responseArgs = new Object[] { this.testFramework.arbitraryData().getString(),
        this.testFramework.arbitraryData().getString(), this.testFramework.arbitraryData().getString() };
    final TestChatMessageEvent messageEvent = this.testFramework.getInstance(TestChatMessageEvent.class);
    final TestChatCommand subjectUnderTest = this.givenASubjectToTest();
    subjectUnderTest.setMessageEventHandler(event -> subjectUnderTest.sendResponse(event, responseArgs));

    subjectUnderTest.handleMessageEvent(messageEvent);
    final ChatMessage result = messageEvent.getResponseChatMessage();

    assertThat(result).isEqualTo(ChatMessage.of(String.format(customMessageFormat,
        responseArgs[0])));
  }

  @Test
  public final void parseArguments_noMessageBody_resultIsFalse() {
    this.registerModules();
    final PicoCliUtils picoCliUtils = this.testFramework.getInstance(PicoCliUtils.class);
    final PositionalParamSpec stringListParam = picoCliUtils.createStringListParam(false, "0..*");

    final TestChatCommand subjectUnderTest = this.givenASubjectToTest();
    final TestChatMessageEvent chatMessageEvent = this.testFramework.getInstance(TestChatMessageEvent.class);
    chatMessageEvent.setChatMessage(ChatMessage.of(subjectUnderTest.getTrigger().value));

    final boolean result = subjectUnderTest.parseArguments(Sets.newHashSet(stringListParam),
        chatMessageEvent,
        parseResult -> true);

    assertThat(result).isFalse();
  }

  @Test
  public final void parseArguments_properlyFormattedCommand_resultIsTrue() {
    this.registerModules();
    final PicoCliUtils picoCliUtils = this.testFramework.getInstance(PicoCliUtils.class);
    final OptionSpec stringOption = picoCliUtils.createStringOption(true, "-r");
    final PositionalParamSpec stringParam = picoCliUtils.createStringParam(true, "0");
    final PositionalParamSpec stringListParam = picoCliUtils.createStringListParam(true, "1..*");

    final TestChatCommand subjectUnderTest = this.givenASubjectToTest();
    final TestChatMessageEvent chatMessageEvent = this.testFramework.getInstance(TestChatMessageEvent.class);
    chatMessageEvent.setChatMessage(ChatMessage.of(String.format("%s %s -r %s %s", subjectUnderTest.getTrigger(),
        this.testFramework.arbitraryData().getString(), this.testFramework.arbitraryData().getString(),
        this.testFramework.arbitraryData().getString())));

    final boolean result = subjectUnderTest.parseArguments(Sets.newHashSet(stringOption, stringParam, stringListParam),
        chatMessageEvent,
        parseResult -> true);

    assertThat(result).isTrue();
  }

  @Test
  public final void parseArguments_improperlyFormattedCommand_resultIsFalse() {
    this.registerModules();
    final PicoCliUtils picoCliUtils = this.testFramework.getInstance(PicoCliUtils.class);
    final OptionSpec stringOption = picoCliUtils.createStringOption(true, "-r");
    final PositionalParamSpec stringParam = picoCliUtils.createStringParam(true, "0");
    final PositionalParamSpec stringListParam = picoCliUtils.createStringListParam(true, "1..*");

    final TestChatCommand subjectUnderTest = this.givenASubjectToTest();
    final TestChatMessageEvent chatMessageEvent = this.testFramework.getInstance(TestChatMessageEvent.class);
    chatMessageEvent.setChatMessage(ChatMessage.of(String.format("%s %s %s", subjectUnderTest.getTrigger(),
        this.testFramework.arbitraryData().getString(), this.testFramework.arbitraryData().getString())));

    final boolean result = subjectUnderTest.parseArguments(Sets.newHashSet(stringOption, stringParam, stringListParam),
        chatMessageEvent,
        parseResult -> true);

    assertThat(result).isFalse();
  }

}
