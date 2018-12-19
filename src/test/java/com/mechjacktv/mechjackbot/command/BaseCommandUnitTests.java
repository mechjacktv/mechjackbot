package com.mechjacktv.mechjackbot.command;

import static com.mechjacktv.mechjackbot.TestCommand.DEFAULT_DESCRIPTION;
import static com.mechjacktv.mechjackbot.TestCommand.DEFAULT_TRIGGER;
import static com.mechjacktv.mechjackbot.TestCommand.KEY_DESCRIPTION;
import static com.mechjacktv.mechjackbot.TestCommand.KEY_MESSAGE_FORMAT;
import static com.mechjacktv.mechjackbot.TestCommand.KEY_TRIGGER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

import org.junit.Test;

import com.mechjacktv.configuration.ConfigurationKey;
import com.mechjacktv.configuration.MapConfiguration;
import com.mechjacktv.mechjackbot.CommandDescription;
import com.mechjacktv.mechjackbot.CommandTrigger;
import com.mechjacktv.mechjackbot.Message;
import com.mechjacktv.mechjackbot.TestCommand;
import com.mechjacktv.mechjackbot.TestCommandConfigurationBuilder;
import com.mechjacktv.mechjackbot.TestMessageEvent;

public class BaseCommandUnitTests extends BaseCommandContractTests {

  @Override
  protected final TestCommand givenASubjectToTest() {
    return this.givenASubjectToTest(this.getMessageFormatDefault());
  }

  protected final TestCommand givenASubjectToTest(final CommandMessageFormat defaultMessageFormat) {
    final TestCommandConfigurationBuilder builder = new TestCommandConfigurationBuilder(
        this.testFrameworkRule.getInstance(CommandConfigurationBuilder.class));
    builder.setDefaultMessageFormat(defaultMessageFormat.value);

    return new TestCommand(builder);
  }

  @Override
  protected CommandDescription getDescriptionDefault() {
    return CommandDescription.of(DEFAULT_DESCRIPTION);
  }

  @Override
  protected ConfigurationKey getDescriptionKey() {
    return ConfigurationKey.of(KEY_DESCRIPTION, TestCommand.class);
  }

  @Override
  protected CommandTrigger getTriggerDefault() {
    return CommandTrigger.of(DEFAULT_TRIGGER);
  }

  @Override
  protected ConfigurationKey getTriggerKey() {
    return ConfigurationKey.of(KEY_TRIGGER, TestCommand.class);
  }

  private CommandMessageFormat getMessageFormatDefault() {
    return CommandMessageFormat.of("%s %s %s %s");
  }

  private ConfigurationKey getMessageFormatKey() {
    return ConfigurationKey.of(KEY_MESSAGE_FORMAT, TestCommand.class);
  }

  @Test
  public final void handleMessageEvent_sendUsage_resultIsUsageMessage() {
    this.installModules();
    final TestMessageEvent messageEvent = this.testFrameworkRule.getInstance(TestMessageEvent.class);
    final TestCommand subjectUnderTest = this.givenASubjectToTest();
    subjectUnderTest.setMessageEventHandler(subjectUnderTest::sendUsage);

    subjectUnderTest.handleMessageEvent(messageEvent);
    final Message result = messageEvent.getResponseMessage();

    this.assertUsageMessageForCommand(result, subjectUnderTest, messageEvent);
  }

  @Test
  public final void handleMessageEvent_sendResponseWithNullMessageFormat_throwsNullPointerException() {
    this.installModules();
    final TestMessageEvent messageEvent = this.testFrameworkRule.getInstance(TestMessageEvent.class);
    final TestCommand subjectUnderTest = this.givenASubjectToTest();
    subjectUnderTest.setMessageEventHandler(event -> subjectUnderTest.sendResponse(event, (CommandMessageFormat) null));

    final Throwable thrown = catchThrowable(() -> subjectUnderTest.handleMessageEvent(messageEvent));

    this.testFrameworkRule.assertNullPointerException(thrown, "messageFormat");
  }

  @Test
  public final void handleMessageEvent_noMessageFormatConfigured_resultIsDefaultMessage() {
    this.installModules();
    final Object[] responseArgs = new Object[] { this.testFrameworkRule.getArbitraryString(),
        this.testFrameworkRule.getArbitraryString(), this.testFrameworkRule.getArbitraryString() };
    final TestMessageEvent messageEvent = this.testFrameworkRule.getInstance(TestMessageEvent.class);
    final TestCommand subjectUnderTest = this.givenASubjectToTest();
    subjectUnderTest.setMessageEventHandler(event -> subjectUnderTest.sendResponse(event, responseArgs));

    subjectUnderTest.handleMessageEvent(messageEvent);
    final Message result = messageEvent.getResponseMessage();

    assertThat(result).isEqualTo(Message.of(String.format(subjectUnderTest.getDefaultMessageFormat().value,
        messageEvent.getChatUser().getTwitchLogin(), responseArgs[0], responseArgs[1], responseArgs[2])));
  }

  @Test
  public final void handleMessageEvent_customMessageFormatConfigured_resultIsCustomMessage() {
    this.installModules();
    final String customMessageFormat = "%s";
    final MapConfiguration configuration = this.testFrameworkRule.getInstance(MapConfiguration.class);
    configuration.set(this.getMessageFormatKey(), customMessageFormat);
    final TestMessageEvent messageEvent = this.testFrameworkRule.getInstance(TestMessageEvent.class);
    final TestCommand subjectUnderTest = this.givenASubjectToTest();
    subjectUnderTest.setMessageEventHandler(event -> subjectUnderTest.sendResponse(event));

    subjectUnderTest.handleMessageEvent(messageEvent);
    final Message result = messageEvent.getResponseMessage();

    assertThat(result).isEqualTo(Message.of(String.format(customMessageFormat,
        messageEvent.getChatUser().getTwitchLogin())));
  }

}
