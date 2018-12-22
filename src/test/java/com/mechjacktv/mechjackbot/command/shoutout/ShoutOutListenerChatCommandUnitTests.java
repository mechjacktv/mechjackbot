package com.mechjacktv.mechjackbot.command.shoutout;

import static com.mechjacktv.mechjackbot.command.shoutout.ShoutOutListenerChatCommand.DEFAULT_DESCRIPTION;
import static com.mechjacktv.mechjackbot.command.shoutout.ShoutOutListenerChatCommand.DEFAULT_FREQUENCY;
import static com.mechjacktv.mechjackbot.command.shoutout.ShoutOutListenerChatCommand.DEFAULT_MESSAGE_FORMAT;
import static com.mechjacktv.mechjackbot.command.shoutout.ShoutOutListenerChatCommand.KEY_DESCRIPTION;
import static com.mechjacktv.mechjackbot.command.shoutout.ShoutOutListenerChatCommand.KEY_FREQUENCY;
import static com.mechjacktv.mechjackbot.command.shoutout.ShoutOutListenerChatCommand.KEY_MESSAGE_FORMAT;
import static com.mechjacktv.mechjackbot.command.shoutout.ShoutOutListenerChatCommand.KEY_TRIGGER;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.assertj.core.api.Condition;
import org.junit.Test;

import com.mechjacktv.configuration.Configuration;
import com.mechjacktv.configuration.ConfigurationKey;
import com.mechjacktv.configuration.MapConfiguration;
import com.mechjacktv.keyvaluestore.KeyValueStoreTestModule;
import com.mechjacktv.mechjackbot.ChatCommandDescription;
import com.mechjacktv.mechjackbot.ChatCommandTrigger;
import com.mechjacktv.mechjackbot.ChatMessage;
import com.mechjacktv.mechjackbot.ChatMessageEvent;
import com.mechjacktv.mechjackbot.TestChatMessageEvent;
import com.mechjacktv.mechjackbot.chatbot.ChatBotTestModule;
import com.mechjacktv.mechjackbot.command.BaseChatCommandContractTests;
import com.mechjacktv.mechjackbot.command.CommandConfigurationBuilder;
import com.mechjacktv.mechjackbot.command.CommandMessageFormat;
import com.mechjacktv.proto.mechjackbot.command.shoutout.ShoutOutServiceMessage.Caster;
import com.mechjacktv.proto.mechjackbot.command.shoutout.ShoutOutServiceMessage.CasterKey;
import com.mechjacktv.twitchclient.TwitchClientTestModule;
import com.mechjacktv.util.TestTimeUtils;
import com.mechjacktv.util.TimeUtils;
import com.mechjacktv.util.scheduleservice.ScheduleServiceTestModule;

public final class ShoutOutListenerChatCommandUnitTests extends BaseChatCommandContractTests {

  protected final void installModules() {
    super.installModules();
    this.testFrameworkRule.installModule(new ChatBotTestModule());
    this.testFrameworkRule.installModule(new KeyValueStoreTestModule());
    this.testFrameworkRule.installModule(new ScheduleServiceTestModule());
    this.testFrameworkRule.installModule(new ShoutOutCommandTestModule());
    this.testFrameworkRule.installModule(new TwitchClientTestModule());
  }

  @Override
  protected final ShoutOutListenerChatCommand givenASubjectToTest() {
    return new ShoutOutListenerChatCommand(this.testFrameworkRule.getInstance(CommandConfigurationBuilder.class),
        this.testFrameworkRule.getInstance(Configuration.class),
        this.testFrameworkRule.getInstance(ShoutOutDataStore.class),
        this.testFrameworkRule.getInstance(TimeUtils.class));
  }

  @Override
  protected ChatCommandDescription getDescriptionDefault() {
    return ChatCommandDescription.of(DEFAULT_DESCRIPTION);
  }

  @Override
  protected ConfigurationKey getDescriptionKey() {
    return ConfigurationKey.of(KEY_DESCRIPTION, ShoutOutListenerChatCommand.class);
  }

  @Override
  protected ChatCommandTrigger getTriggerDefault() {
    return ChatCommandTrigger.of(UUID.randomUUID().toString());
  }

  @Override
  protected ConfigurationKey getTriggerKey() {
    return ConfigurationKey.of(KEY_TRIGGER, ShoutOutListenerChatCommand.class);
  }

  private CommandMessageFormat getMessageFormatDefault() {
    return CommandMessageFormat.of(DEFAULT_MESSAGE_FORMAT);
  }

  private ConfigurationKey getMessageFormatKey() {
    return ConfigurationKey.of(KEY_MESSAGE_FORMAT, ShoutOutListenerChatCommand.class);
  }

  private Integer getFrequencyDefault() {
    return Integer.parseInt(DEFAULT_FREQUENCY);
  }

  private ConfigurationKey getFrequencyKey() {
    return ConfigurationKey.of(KEY_FREQUENCY, ShoutOutListenerChatCommand.class);
  }

  @Test
  public final void isTriggered_notACaster_resultIsFalse() {
    this.installModules();
    final ChatMessageEvent chatMessageEvent = this.testFrameworkRule.getInstance(ChatMessageEvent.class);
    final ShoutOutListenerChatCommand subjectUnderTest = this.givenASubjectToTest();

    final boolean result = subjectUnderTest.isTriggered(chatMessageEvent);

    assertThat(result).isFalse();
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
    configuration.set(ConfigurationKey.of(KEY_FREQUENCY, ShoutOutListenerChatCommand.class),
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
    configuration.set(ConfigurationKey.of(KEY_FREQUENCY, ShoutOutListenerChatCommand.class),
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
    final TestChatMessageEvent messageEvent = this.testFrameworkRule.getInstance(TestChatMessageEvent.class);
    final ShoutOutListenerChatCommand subjectUnderTest = this.givenASubjectToTest();

    subjectUnderTest.handleMessageEvent(messageEvent);
    final ChatMessage result = messageEvent.getResponseChatMessage();

    assertThat(result).isEqualTo(ChatMessage.of(String.format(this.getMessageFormatDefault().value,
        messageEvent.getChatUser().getTwitchLogin())));
  }

  @Test
  public final void handleMessageEvent_customMessageFormatConfigured_resultIsCustomMessage() {
    this.installModules();
    final String customMessageFormat = this.testFrameworkRule.getArbitraryString() + " %s";
    final MapConfiguration configuration = this.testFrameworkRule.getInstance(MapConfiguration.class);
    configuration.set(this.getMessageFormatKey(), customMessageFormat);
    final TestChatMessageEvent messageEvent = this.testFrameworkRule.getInstance(TestChatMessageEvent.class);
    final ShoutOutListenerChatCommand subjectUnderTest = this.givenASubjectToTest();

    subjectUnderTest.handleMessageEvent(messageEvent);
    final ChatMessage result = messageEvent.getResponseChatMessage();

    assertThat(result).isEqualTo(ChatMessage.of(String.format(customMessageFormat,
        messageEvent.getChatUser().getTwitchLogin())));
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

    final ShoutOutListenerChatCommand subjectUnderTest = this.givenASubjectToTest();

    subjectUnderTest.handleMessageEvent(messageEvent);

    assertThat(shoutOutDataStore).is(new Condition<ShoutOutDataStore>() {

      @Override
      public boolean matches(final ShoutOutDataStore actual) {
        return actual.get(casterKey).isPresent() && actual.get(casterKey).get().getLastShoutOut() == lastShoutOut;
      }

    });

  }

}
