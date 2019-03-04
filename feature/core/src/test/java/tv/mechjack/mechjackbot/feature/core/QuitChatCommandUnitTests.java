package tv.mechjack.mechjackbot.feature.core;

import static org.assertj.core.api.Assertions.assertThat;
import static tv.mechjack.mechjackbot.feature.core.QuitChatCommand.KEY_DESCRIPTION;
import static tv.mechjack.mechjackbot.feature.core.QuitChatCommand.KEY_MESSAGE_FORMAT;
import static tv.mechjack.mechjackbot.feature.core.QuitChatCommand.KEY_TRIGGER;

import org.junit.Test;

import tv.mechjack.mechjackbot.api.BaseChatCommandContractTests;
import tv.mechjack.mechjackbot.api.ChatCommandDescription;
import tv.mechjack.mechjackbot.api.ChatCommandTrigger;
import tv.mechjack.mechjackbot.api.ChatMessage;
import tv.mechjack.mechjackbot.api.CommandConfigurationBuilder;
import tv.mechjack.mechjackbot.api.CommandMessageFormat;
import tv.mechjack.mechjackbot.api.TestChatBot;
import tv.mechjack.mechjackbot.api.TestChatMessageEvent;
import tv.mechjack.platform.configuration.ConfigurationKey;
import tv.mechjack.platform.configuration.MapConfiguration;
import tv.mechjack.platform.utils.scheduleservice.ScheduleService;
import tv.mechjack.platform.utils.scheduleservice.TestScheduleService;
import tv.mechjack.platform.utils.scheduleservice.TestScheduleServiceModule;

public class QuitChatCommandUnitTests extends BaseChatCommandContractTests {

  protected final void registerModules() {
    super.registerModules();
    this.testFramework.registerModule(new TestScheduleServiceModule());
  }

  @Override
  protected final QuitChatCommand givenASubjectToTest() {
    return new QuitChatCommand(this.testFramework.getInstance(CommandConfigurationBuilder.class),
        this.testFramework.getInstance(ScheduleService.class));
  }

  @Override
  protected final ChatCommandDescription getDescriptionDefault() {
    return ChatCommandDescription.of(QuitChatCommand.DEFAULT_DESCRIPTION);
  }

  @Override
  protected final ConfigurationKey getDescriptionKey() {
    return ConfigurationKey.of(KEY_DESCRIPTION, QuitChatCommand.class);
  }

  @Override
  protected final ChatCommandTrigger getTriggerDefault() {
    return ChatCommandTrigger.of(QuitChatCommand.DEFAULT_TRIGGER);
  }

  @Override
  protected final ConfigurationKey getTriggerKey() {
    return ConfigurationKey.of(KEY_TRIGGER, QuitChatCommand.class);
  }

  private CommandMessageFormat getMessageFormatDefault() {
    return CommandMessageFormat.of(QuitChatCommand.DEFAULT_MESSAGE_FORMAT);
  }

  private ConfigurationKey getMessageFormatKey() {
    return ConfigurationKey.of(KEY_MESSAGE_FORMAT, QuitChatCommand.class);
  }

  @Test
  public final void handleMessageEvent_noMessageFormatConfigured_resultIsDefaultMessage() {
    this.registerModules();
    final TestChatMessageEvent messageEvent = this.testFramework.getInstance(TestChatMessageEvent.class);
    final QuitChatCommand subjectUnderTest = this.givenASubjectToTest();

    subjectUnderTest.handleMessageEvent(messageEvent);
    final ChatMessage result = messageEvent.getResponseChatMessage();

    assertThat(result).isEqualTo(ChatMessage.of(this.getMessageFormatDefault().value));
  }

  @Test
  public final void handleMessageEvent_customMessageFormatConfigured_resultIsCustomMessage() {
    this.registerModules();
    final String customMessageFormat = this.testFramework.arbitraryData().getString();
    final MapConfiguration configuration = this.testFramework.getInstance(MapConfiguration.class);
    configuration.set(this.getMessageFormatKey(), customMessageFormat);
    final TestChatMessageEvent messageEvent = this.testFramework.getInstance(TestChatMessageEvent.class);
    final QuitChatCommand subjectUnderTest = this.givenASubjectToTest();

    subjectUnderTest.handleMessageEvent(messageEvent);
    final ChatMessage result = messageEvent.getResponseChatMessage();

    assertThat(result).isEqualTo(ChatMessage.of(customMessageFormat));
  }

  @Test
  public final void handleMessageEvent_whenCalled_resultIsStoppedChatBot() {
    this.registerModules();
    final TestChatMessageEvent messageEvent = this.testFramework.getInstance(TestChatMessageEvent.class);
    final QuitChatCommand subjectUnderTest = this.givenASubjectToTest();

    subjectUnderTest.handleMessageEvent(messageEvent);

    assertThat(((TestChatBot) messageEvent.getChatBot()).wasStopped()).isTrue();
  }

  @Test
  public final void handleMessageEvent_whenCalled_stopsScheduleService() {
    this.registerModules();
    final TestScheduleService scheduleService = this.testFramework.getInstance(TestScheduleService.class);
    final TestChatMessageEvent messageEvent = this.testFramework.getInstance(TestChatMessageEvent.class);
    final QuitChatCommand subjectUnderTest = this.givenASubjectToTest();

    subjectUnderTest.handleMessageEvent(messageEvent);

    assertThat(scheduleService.wasStopped()).isTrue();
  }

}
