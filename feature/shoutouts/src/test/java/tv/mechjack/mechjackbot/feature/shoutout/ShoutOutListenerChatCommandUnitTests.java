package tv.mechjack.mechjackbot.feature.shoutout;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.assertj.core.api.Condition;
import org.assertj.core.api.SoftAssertions;
import org.junit.Test;

import tv.mechjack.mechjackbot.api.BaseChatCommand;
import tv.mechjack.mechjackbot.api.BaseChatCommandContractTests;
import tv.mechjack.mechjackbot.api.ChatCommandDescription;
import tv.mechjack.mechjackbot.api.ChatCommandRegistry;
import tv.mechjack.mechjackbot.api.ChatCommandTrigger;
import tv.mechjack.mechjackbot.api.ChatMessage;
import tv.mechjack.mechjackbot.api.ChatMessageEvent;
import tv.mechjack.mechjackbot.api.CommandConfigurationBuilder;
import tv.mechjack.mechjackbot.api.TestChatCommand;
import tv.mechjack.mechjackbot.api.TestChatMessageEvent;
import tv.mechjack.mechjackbot.chatbot.TestChatBotModule;
import tv.mechjack.mechjackbot.command.shoutout.ProtoMessage.Caster;
import tv.mechjack.mechjackbot.command.shoutout.ProtoMessage.CasterKey;
import tv.mechjack.platform.configuration.Configuration;
import tv.mechjack.platform.configuration.ConfigurationKey;
import tv.mechjack.platform.configuration.MapConfiguration;
import tv.mechjack.platform.keyvaluestore.TestKeyValueStoreModule;
import tv.mechjack.platform.util.TestTimeUtils;
import tv.mechjack.platform.util.TimeUtils;
import tv.mechjack.platform.util.scheduleservice.TestScheduleServiceModule;
import tv.mechjack.twitchclient.TestTwitchClientModule;

public final class ShoutOutListenerChatCommandUnitTests extends BaseChatCommandContractTests {

  @Override
  protected ChatCommandDescription getDescriptionDefault() {
    return ChatCommandDescription.of(ShoutOutListenerChatCommand.DEFAULT_DESCRIPTION);
  }

  @Override
  protected ConfigurationKey getDescriptionKey() {
    return ConfigurationKey.of(BaseChatCommand.KEY_DESCRIPTION, ShoutOutListenerChatCommand.class);
  }

  @Override
  protected ChatCommandTrigger getTriggerDefault() {
    return ChatCommandTrigger.of(UUID.randomUUID().toString());
  }

  @Override
  protected ConfigurationKey getTriggerKey() {
    return ConfigurationKey.of(BaseChatCommand.KEY_TRIGGER, ShoutOutListenerChatCommand.class);
  }

  @Test
  public final void isTriggered_notACaster_resultIsFalse() {
    this.installModules();
    final ChatMessageEvent chatMessageEvent = this.testFrameworkRule.getInstance(ChatMessageEvent.class);
    final ShoutOutListenerChatCommand subjectUnderTest = this.givenASubjectToTest();

    final boolean result = subjectUnderTest.isTriggered(chatMessageEvent);

    assertThat(result).isFalse();
  }

  protected final void installModules() {
    super.installModules();
    this.testFrameworkRule.installModule(new TestChatBotModule());
    this.testFrameworkRule.installModule(new TestKeyValueStoreModule());
    this.testFrameworkRule.installModule(new TestScheduleServiceModule());
    this.testFrameworkRule.installModule(new TestShoutOutCommandModule());
    this.testFrameworkRule.installModule(new TestTwitchClientModule());
  }

  @Override
  protected final ShoutOutListenerChatCommand givenASubjectToTest() {
    return this.givenASubjectToTest(this.testFrameworkRule.getInstance(ChatCommandRegistry.class));
  }

  private ShoutOutListenerChatCommand givenASubjectToTest(final ChatCommandRegistry chatCommandRegistry) {
    return new ShoutOutListenerChatCommand(this.testFrameworkRule.getInstance(CommandConfigurationBuilder.class),
        chatCommandRegistry, this.testFrameworkRule.getInstance(Configuration.class),
        this.testFrameworkRule.getInstance(ShoutOutDataStore.class),
        this.testFrameworkRule.getInstance(TimeUtils.class));
  }

  @Test
  public final void isTriggered_casterIsNotDue_resultIsFalse() {
    this.installModules();
    this.testFrameworkRule.currentTimeDelta(this.getFrequencyDefault(), TimeUnit.HOURS);
    final ChatMessageEvent chatMessageEvent = this.testFrameworkRule.getInstance(ChatMessageEvent.class);
    final ShoutOutDataStore shoutOutDataStore = this.testFrameworkRule.getInstance(ShoutOutDataStore.class);
    final CasterKey casterKey = shoutOutDataStore
        .createCasterKey(chatMessageEvent.getChatUser().getTwitchLogin().value);
    final Caster caster = shoutOutDataStore.createCaster(casterKey.getName(), 0L);
    shoutOutDataStore.put(casterKey, caster);
    final ShoutOutListenerChatCommand subjectUnderTest = this.givenASubjectToTest();

    final boolean result = subjectUnderTest.isTriggered(chatMessageEvent);

    assertThat(result).isFalse();
  }

  private Integer getFrequencyDefault() {
    return Integer.parseInt(ShoutOutListenerChatCommand.DEFAULT_FREQUENCY);
  }

  @Test
  public final void isTriggered_casterIsDue_resultIsTrue() {
    this.installModules();
    this.testFrameworkRule.currentTimeDelta(this.getFrequencyDefault(), TimeUnit.HOURS, 1);
    final ChatMessageEvent chatMessageEvent = this.testFrameworkRule.getInstance(ChatMessageEvent.class);
    final ShoutOutDataStore shoutOutDataStore = this.testFrameworkRule.getInstance(ShoutOutDataStore.class);
    final CasterKey casterKey = shoutOutDataStore
        .createCasterKey(chatMessageEvent.getChatUser().getTwitchLogin().value);
    final Caster caster = shoutOutDataStore.createCaster(casterKey.getName(), 0L);
    shoutOutDataStore.put(casterKey, caster);
    final ShoutOutListenerChatCommand subjectUnderTest = this.givenASubjectToTest();

    final boolean result = subjectUnderTest.isTriggered(chatMessageEvent);

    assertThat(result).isTrue();
  }

  @Test
  public final void isTriggered_casterIsNotDueWithCustomFrequency_resultIsFalse() {
    this.installModules();
    final int customFrequency = this.getFrequencyDefault() + 1;
    final MapConfiguration configuration = this.testFrameworkRule.getInstance(MapConfiguration.class);
    configuration.set(ConfigurationKey.of(ShoutOutListenerChatCommand.KEY_FREQUENCY, ShoutOutListenerChatCommand.class),
        Integer.toString(customFrequency));
    this.testFrameworkRule.currentTimeDelta(this.getFrequencyDefault(), TimeUnit.HOURS);
    final ChatMessageEvent chatMessageEvent = this.testFrameworkRule.getInstance(ChatMessageEvent.class);
    final ShoutOutDataStore shoutOutDataStore = this.testFrameworkRule.getInstance(ShoutOutDataStore.class);
    final CasterKey casterKey = shoutOutDataStore
        .createCasterKey(chatMessageEvent.getChatUser().getTwitchLogin().value);
    final Caster caster = shoutOutDataStore.createCaster(casterKey.getName(), 0L);
    shoutOutDataStore.put(casterKey, caster);
    final ShoutOutListenerChatCommand subjectUnderTest = this.givenASubjectToTest();

    final boolean result = subjectUnderTest.isTriggered(chatMessageEvent);

    assertThat(result).isFalse();
  }

  @Test
  public final void isTriggered_casterIsDueWithCustomFrequency_resultIsTrue() {
    this.installModules();
    final int customFrequency = this.getFrequencyDefault() + 1;
    final MapConfiguration configuration = this.testFrameworkRule.getInstance(MapConfiguration.class);
    configuration.set(ConfigurationKey.of(ShoutOutListenerChatCommand.KEY_FREQUENCY, ShoutOutListenerChatCommand.class),
        Integer.toString(customFrequency));
    this.testFrameworkRule.currentTimeDelta(customFrequency, TimeUnit.HOURS, 1);
    final ChatMessageEvent chatMessageEvent = this.testFrameworkRule.getInstance(ChatMessageEvent.class);
    final ShoutOutDataStore shoutOutDataStore = this.testFrameworkRule.getInstance(ShoutOutDataStore.class);
    final CasterKey casterKey = shoutOutDataStore
        .createCasterKey(chatMessageEvent.getChatUser().getTwitchLogin().value);
    final Caster caster = shoutOutDataStore.createCaster(casterKey.getName(), 0L);
    shoutOutDataStore.put(casterKey, caster);
    final ShoutOutListenerChatCommand subjectUnderTest = this.givenASubjectToTest();

    final boolean result = subjectUnderTest.isTriggered(chatMessageEvent);

    assertThat(result).isTrue();
  }

  @Test
  public final void handleMessageEvent_noMessageFormatConfigured_resultIsDefaultMessage() {
    this.installModules();
    final ChatCommandRegistry fakeChatCommandRegistry = mock(ChatCommandRegistry.class);
    final TestChatCommand chatCommand = this.testFrameworkRule.getInstance(TestChatCommand.class);
    when(fakeChatCommandRegistry.getCommand(eq(ShoutOutChatCommand.class))).thenReturn(Optional.of(chatCommand));
    final TestChatMessageEvent messageEvent = this.testFrameworkRule.getInstance(TestChatMessageEvent.class);
    final ShoutOutListenerChatCommand subjectUnderTest = this.givenASubjectToTest(fakeChatCommandRegistry);

    final ChatMessageEvent[] result = new ChatMessageEvent[1];
    chatCommand.setMessageEventHandler(chatMessageEvent -> result[0] = chatMessageEvent);
    subjectUnderTest.handleMessageEvent(messageEvent);

    final SoftAssertions softly = new SoftAssertions();
    softly.assertThat(result[0].getChatBot()).isEqualTo(messageEvent.getChatBot());
    softly.assertThat(result[0].getChatUser()).isEqualTo(messageEvent.getChatUser());
    softly.assertThat(result[0].getChatMessage()).isEqualTo(ChatMessage.of(String.format(
        ShoutOutListenerChatCommand.FORWARDED_MESSAGE_FORMAT,
        chatCommand.getTrigger(), messageEvent.getChatUser().getTwitchLogin())));
    softly.assertAll();
  }

  @Test
  public final void handleMessageEvent_isCalled_resultIsUpdatedDataStore() {
    this.installModules();
    final TestTimeUtils timeUtils = this.testFrameworkRule.getInstance(TestTimeUtils.class);
    final Long lastShoutOut = timeUtils.hoursAsMs(this.getFrequencyDefault());
    this.testFrameworkRule.currentTimeDelta(lastShoutOut, TimeUnit.MILLISECONDS);
    final TestChatMessageEvent messageEvent = this.testFrameworkRule.getInstance(TestChatMessageEvent.class);
    final ShoutOutDataStore shoutOutDataStore = this.testFrameworkRule.getInstance(ShoutOutDataStore.class);
    final CasterKey casterKey = shoutOutDataStore.createCasterKey(messageEvent.getChatUser().getTwitchLogin().value);
    final Caster caster = shoutOutDataStore.createCaster(casterKey.getName(), 0L);
    shoutOutDataStore.put(casterKey, caster);
    final ChatCommandRegistry fakeChatCommandRegistry = mock(ChatCommandRegistry.class);
    final TestChatCommand chatCommand = this.testFrameworkRule.getInstance(TestChatCommand.class);
    when(fakeChatCommandRegistry.getCommand(eq(ShoutOutChatCommand.class))).thenReturn(Optional.of(chatCommand));

    final ShoutOutListenerChatCommand subjectUnderTest = this.givenASubjectToTest(fakeChatCommandRegistry);

    subjectUnderTest.handleMessageEvent(messageEvent);

    assertThat(shoutOutDataStore).is(new Condition<ShoutOutDataStore>() {

      @Override
      public boolean matches(final ShoutOutDataStore actual) {
        return actual.get(casterKey).isPresent() && actual.get(casterKey).get().getLastShoutOut() == lastShoutOut;
      }

    });

  }

}
