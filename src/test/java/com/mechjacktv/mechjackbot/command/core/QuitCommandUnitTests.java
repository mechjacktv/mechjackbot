package com.mechjacktv.mechjackbot.command.core;

import static com.mechjacktv.mechjackbot.command.core.QuitCommand.DEFAULT_DESCRIPTION;
import static com.mechjacktv.mechjackbot.command.core.QuitCommand.DEFAULT_MESSAGE_FORMAT;
import static com.mechjacktv.mechjackbot.command.core.QuitCommand.DEFAULT_TRIGGER;
import static com.mechjacktv.mechjackbot.command.core.QuitCommand.KEY_DESCRIPTION;
import static com.mechjacktv.mechjackbot.command.core.QuitCommand.KEY_MESSAGE_FORMAT;
import static com.mechjacktv.mechjackbot.command.core.QuitCommand.KEY_TRIGGER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import com.mechjacktv.configuration.ConfigurationKey;
import com.mechjacktv.configuration.MapConfiguration;
import com.mechjacktv.mechjackbot.CommandDescription;
import com.mechjacktv.mechjackbot.CommandTrigger;
import com.mechjacktv.mechjackbot.Message;
import com.mechjacktv.mechjackbot.TestChatBot;
import com.mechjacktv.mechjackbot.TestMessageEvent;
import com.mechjacktv.mechjackbot.command.BaseCommandContractTests;
import com.mechjacktv.mechjackbot.command.CommandConfigurationBuilder;
import com.mechjacktv.mechjackbot.command.CommandMessageFormat;
import com.mechjacktv.util.scheduleservice.ScheduleService;
import com.mechjacktv.util.scheduleservice.ScheduleServiceTestModule;
import com.mechjacktv.util.scheduleservice.TestScheduleService;

public class QuitCommandUnitTests extends BaseCommandContractTests {

  protected final void installModules() {
    super.installModules();
    this.testFrameworkRule.installModule(new ScheduleServiceTestModule());
  }

  @Override
  protected final QuitCommand givenASubjectToTest() {
    return new QuitCommand(this.testFrameworkRule.getInstance(CommandConfigurationBuilder.class),
        this.testFrameworkRule.getInstance(ScheduleService.class));
  }

  @Override
  protected final CommandDescription getDescriptionDefault() {
    return CommandDescription.of(DEFAULT_DESCRIPTION);
  }

  @Override
  protected final ConfigurationKey getDescriptionKey() {
    return ConfigurationKey.of(KEY_DESCRIPTION, QuitCommand.class);
  }

  @Override
  protected final CommandTrigger getTriggerDefault() {
    return CommandTrigger.of(DEFAULT_TRIGGER);
  }

  @Override
  protected final ConfigurationKey getTriggerKey() {
    return ConfigurationKey.of(KEY_TRIGGER, QuitCommand.class);
  }

  private CommandMessageFormat getMessageFormatDefault() {
    return CommandMessageFormat.of(DEFAULT_MESSAGE_FORMAT);
  }

  private ConfigurationKey getMessageFormatKey() {
    return ConfigurationKey.of(KEY_MESSAGE_FORMAT, QuitCommand.class);
  }

  @Test
  public final void handleMessageEvent_noMessageFormatConfigured_resultIsDefaultMessage() {
    this.installModules();
    final TestMessageEvent messageEvent = this.testFrameworkRule.getInstance(TestMessageEvent.class);
    final QuitCommand subjectUnderTest = this.givenASubjectToTest();

    subjectUnderTest.handleMessageEvent(messageEvent);
    final Message result = messageEvent.getResponseMessage();

    assertThat(result).isEqualTo(Message.of(this.getMessageFormatDefault().value));
  }

  @Test
  public final void handleMessageEvent_customMessageFormatConfigured_resultIsCustomMessage() {
    this.installModules();
    final String customMessageFormat = this.testFrameworkRule.getArbitraryString();
    final MapConfiguration configuration = this.testFrameworkRule.getInstance(MapConfiguration.class);
    configuration.set(this.getMessageFormatKey(), customMessageFormat);
    final TestMessageEvent messageEvent = this.testFrameworkRule.getInstance(TestMessageEvent.class);
    final QuitCommand subjectUnderTest = this.givenASubjectToTest();

    subjectUnderTest.handleMessageEvent(messageEvent);
    final Message result = messageEvent.getResponseMessage();

    assertThat(result).isEqualTo(Message.of(customMessageFormat));
  }

  @Test
  public final void handleMessageEvent_whenCalled_resultIsStoppedChatBot() {
    this.installModules();
    final TestMessageEvent messageEvent = this.testFrameworkRule.getInstance(TestMessageEvent.class);
    final QuitCommand subjectUnderTest = this.givenASubjectToTest();

    subjectUnderTest.handleMessageEvent(messageEvent);

    assertThat(((TestChatBot) messageEvent.getChatBot()).wasStopped()).isTrue();
  }

  @Test
  public final void handleMessageEvent_whenCalled_stopsScheduleService() {
    this.installModules();
    final TestScheduleService scheduleService = this.testFrameworkRule.getInstance(TestScheduleService.class);
    final TestMessageEvent messageEvent = this.testFrameworkRule.getInstance(TestMessageEvent.class);
    final QuitCommand subjectUnderTest = this.givenASubjectToTest();

    subjectUnderTest.handleMessageEvent(messageEvent);

    assertThat(scheduleService.wasStopped()).isTrue();
  }

}
