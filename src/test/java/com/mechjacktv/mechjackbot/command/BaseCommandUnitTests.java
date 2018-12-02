package com.mechjacktv.mechjackbot.command;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

import org.junit.Test;

import com.mechjacktv.configuration.Configuration;
import com.mechjacktv.configuration.ConfigurationKey;
import com.mechjacktv.configuration.MapConfiguration;
import com.mechjacktv.mechjackbot.*;
import com.mechjacktv.util.DefaultExecutionUtils;
import com.mechjacktv.util.ExecutionUtils;

public class BaseCommandUnitTests extends BaseCommandContractTests {

  private final ArbitraryCommandTestUtils commandTestUtils = new ArbitraryCommandTestUtils(this.arbitraryDataGenerator);

  private final ExecutionUtils executionUtils = new DefaultExecutionUtils();

  @Override
  protected CommandDescription getDescriptionDefault() {
    return CommandDescription.of(TestBaseCommand.DESCRIPTION_DEFAULT);
  }

  @Override
  protected ConfigurationKey getDescriptionKey() {
    return ConfigurationKey.of(BaseCommand.DESCRIPTION_KEY, TestBaseCommand.class);
  }

  @Override
  protected CommandTrigger getTriggerDefault() {
    return CommandTrigger.of(TestBaseCommand.TRIGGER_DEFAULT);
  }

  @Override
  protected ConfigurationKey getTriggerKey() {
    return ConfigurationKey.of(BaseCommand.TRIGGER_KEY, TestBaseCommand.class);
  }

  @Override
  protected TestBaseCommand givenASubjectToTest(final Configuration configuration) {
    return this.givenASubjectToTest(configuration, new Object[] {});
  }

  private TestBaseCommand givenASubjectToTest(final Configuration configuration, final Object[] responseArgs) {
    final DefaultCommandConfigurationBuilder builder = new DefaultCommandConfigurationBuilder(
        this.commandTestUtils.givenACommandUtils(configuration), configuration, this.executionUtils);

    return new TestBaseCommand(builder, responseArgs);
  }

  @Test
  public final void handleMessageEvent_invalidMessageEvent_sendUsage() {
    final ArbitraryMessageEvent messageEvent = new ArbitraryMessageEvent(this.arbitraryDataGenerator);
    final Command subjectUnderTest = this.givenASubjectToTest(this.givenAConfiguration(), null);

    subjectUnderTest.handleMessageEvent(messageEvent);
    final Message result = messageEvent.getResponseMessage();

    assertThat(result).isEqualTo(Message.of(String.format(CommandUtils.COMMAND_USAGE_MESSAGE_FORMAT_DEFAULT,
        messageEvent.getChatUser().getTwitchLogin(), subjectUnderTest.getTrigger(), TestBaseCommand.USAGE)));
  }

  @Test
  public final void handleMessageEvent_nullMessageEvent_throwsNullPointerException() {
    final Command subjectUnderTest = this.givenASubjectToTest(this.givenAConfiguration());

    final Throwable thrown = catchThrowable(() -> subjectUnderTest.handleMessageEvent(null));

    assertThat(thrown).isInstanceOf(NullPointerException.class).hasMessage(this.executionUtils.nullMessageForName(
        "messageEvent"));
  }

  @Test
  public final void handleMessageEvent_nullMessageFormat_throwsNullPointerException() {
    final ArbitraryMessageEvent messageEvent = new ArbitraryMessageEvent(this.arbitraryDataGenerator);
    final TestBaseCommand subjectUnderTest = this.givenASubjectToTest(this.givenAConfiguration());
    subjectUnderTest.sendNullMessageFormat();

    final Throwable thrown = catchThrowable(() -> subjectUnderTest.handleMessageEvent(messageEvent));

    assertThat(thrown).isInstanceOf(NullPointerException.class).hasMessage(this.executionUtils.nullMessageForName(
        "messageFormat"));
  }

  @Test
  public final void handleMessageEvent_defaultMessageFormat_returnsExpectedMessage() {
    final Object[] args = new Object[] { this.arbitraryDataGenerator.getString(),
        this.arbitraryDataGenerator.getString(), this.arbitraryDataGenerator.getString() };
    final ArbitraryMessageEvent messageEvent = new ArbitraryMessageEvent(this.arbitraryDataGenerator);
    final TestBaseCommand subjectUnderTest = this.givenASubjectToTest(this.givenAConfiguration(), args);

    subjectUnderTest.handleMessageEvent(messageEvent);
    final Message result = messageEvent.getResponseMessage();

    assertThat(result).isEqualTo(Message.of(String.format(TestBaseCommand.MESSAGE_FORMAT_DEFAULT,
        messageEvent.getChatUser().getTwitchLogin(), args[0], args[1], args[2])));
  }

  @Test
  public final void handleMessageEvent_customMessageFormat_returnsExpectedMessage() {
    final String customMessageFormat = "%s";
    final MapConfiguration configuration = this.givenAConfiguration();
    configuration.set(ConfigurationKey.of(BaseCommand.MESSAGE_FORMAT_KEY, TestBaseCommand.class).value,
        customMessageFormat);
    final ArbitraryMessageEvent messageEvent = new ArbitraryMessageEvent(this.arbitraryDataGenerator);
    final TestBaseCommand subjectUnderTest = this.givenASubjectToTest(configuration);

    subjectUnderTest.handleMessageEvent(messageEvent);
    final Message result = messageEvent.getResponseMessage();

    assertThat(result).isEqualTo(Message.of(String.format(customMessageFormat,
        messageEvent.getChatUser().getTwitchLogin())));
  }

  private static final class TestBaseCommand extends BaseCommand {

    private static final String DESCRIPTION_DEFAULT = "DESCRIPTION_DEFAULT";
    private static final String MESSAGE_FORMAT_DEFAULT = "%s %s %s %s";
    private static final String TRIGGER_DEFAULT = "TRIGGER_DEFAULT";
    private static final String USAGE = "<1> <2> <3>";

    private final Object[] responseArgs;
    private boolean sendNullMessageFormat;

    protected TestBaseCommand(final CommandConfigurationBuilder commandConfigurationBuilder,
        final Object[] responseArgs) {
      super(commandConfigurationBuilder.setTrigger(TRIGGER_DEFAULT)
          .setDescription(DESCRIPTION_DEFAULT)
          .setMessageFormat(MESSAGE_FORMAT_DEFAULT)
          .setUsage(USAGE));
      this.responseArgs = responseArgs;
    }

    void sendNullMessageFormat() {
      this.sendNullMessageFormat = true;
    }

    @Override
    public void handleMessageEvent(final MessageEvent messageEvent) {
      if (this.sendNullMessageFormat) {
        this.sendResponse(messageEvent, null, this.responseArgs);
      } else if (this.responseArgs == null) {
        this.sendUsage(messageEvent);
      } else if (this.responseArgs.length == 3) {
        this.sendResponse(messageEvent, this.responseArgs);
      } else {
        this.sendResponse(messageEvent);
      }
    }

  }

}
