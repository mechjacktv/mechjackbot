package com.mechjacktv.mechjackbot.command.shoutout;

import static com.mechjacktv.mechjackbot.command.shoutout.ShoutOutListenerCommand.DEFAULT_DESCRIPTION;
import static com.mechjacktv.mechjackbot.command.shoutout.ShoutOutListenerCommand.DEFAULT_FREQUENCY;
import static com.mechjacktv.mechjackbot.command.shoutout.ShoutOutListenerCommand.DEFAULT_MESSAGE_FORMAT;
import static com.mechjacktv.mechjackbot.command.shoutout.ShoutOutListenerCommand.KEY_DESCRIPTION;
import static com.mechjacktv.mechjackbot.command.shoutout.ShoutOutListenerCommand.KEY_FREQUENCY;
import static com.mechjacktv.mechjackbot.command.shoutout.ShoutOutListenerCommand.KEY_MESSAGE_FORMAT;
import static com.mechjacktv.mechjackbot.command.shoutout.ShoutOutListenerCommand.KEY_TRIGGER;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.assertj.core.api.Condition;
import org.junit.Test;

import com.mechjacktv.configuration.Configuration;
import com.mechjacktv.configuration.ConfigurationKey;
import com.mechjacktv.configuration.MapConfiguration;
import com.mechjacktv.keyvaluestore.KeyValueStoreTestModule;
import com.mechjacktv.mechjackbot.CommandDescription;
import com.mechjacktv.mechjackbot.CommandTrigger;
import com.mechjacktv.mechjackbot.Message;
import com.mechjacktv.mechjackbot.MessageEvent;
import com.mechjacktv.mechjackbot.TestMessageEvent;
import com.mechjacktv.mechjackbot.chatbot.ChatBotTestModule;
import com.mechjacktv.mechjackbot.command.BaseCommandContractTests;
import com.mechjacktv.mechjackbot.command.CommandConfigurationBuilder;
import com.mechjacktv.mechjackbot.command.CommandMessageFormat;
import com.mechjacktv.proto.mechjackbot.command.shoutout.ShoutOutServiceMessage.Caster;
import com.mechjacktv.proto.mechjackbot.command.shoutout.ShoutOutServiceMessage.CasterKey;
import com.mechjacktv.twitchclient.TwitchClientTestModule;
import com.mechjacktv.util.TestTimeUtils;
import com.mechjacktv.util.TimeUtils;
import com.mechjacktv.util.scheduleservice.ScheduleServiceTestModule;

public final class ShoutOutListenerCommandUnitTests extends BaseCommandContractTests {

  protected final void installModules() {
    super.installModules();
    this.testFrameworkRule.installModule(new ChatBotTestModule());
    this.testFrameworkRule.installModule(new KeyValueStoreTestModule());
    this.testFrameworkRule.installModule(new ScheduleServiceTestModule());
    this.testFrameworkRule.installModule(new ShoutOutCommandTestModule());
    this.testFrameworkRule.installModule(new TwitchClientTestModule());
  }

  @Override
  protected final ShoutOutListenerCommand givenASubjectToTest() {
    return new ShoutOutListenerCommand(this.testFrameworkRule.getInstance(CommandConfigurationBuilder.class),
        this.testFrameworkRule.getInstance(Configuration.class),
        this.testFrameworkRule.getInstance(ShoutOutDataStore.class),
        this.testFrameworkRule.getInstance(TimeUtils.class));
  }

  @Override
  protected CommandDescription getDescriptionDefault() {
    return CommandDescription.of(DEFAULT_DESCRIPTION);
  }

  @Override
  protected ConfigurationKey getDescriptionKey() {
    return ConfigurationKey.of(KEY_DESCRIPTION, ShoutOutListenerCommand.class);
  }

  @Override
  protected CommandTrigger getTriggerDefault() {
    return CommandTrigger.of(UUID.randomUUID().toString());
  }

  @Override
  protected ConfigurationKey getTriggerKey() {
    return ConfigurationKey.of(KEY_TRIGGER, ShoutOutListenerCommand.class);
  }

  private CommandMessageFormat getMessageFormatDefault() {
    return CommandMessageFormat.of(DEFAULT_MESSAGE_FORMAT);
  }

  private ConfigurationKey getMessageFormatKey() {
    return ConfigurationKey.of(KEY_MESSAGE_FORMAT, ShoutOutListenerCommand.class);
  }

  private Integer getFrequencyDefault() {
    return Integer.parseInt(DEFAULT_FREQUENCY);
  }

  private ConfigurationKey getFrequencyKey() {
    return ConfigurationKey.of(KEY_FREQUENCY, ShoutOutListenerCommand.class);
  }

  @Test
  public final void isTriggered_notACaster_resultIsFalse() {
    this.installModules();
    final MessageEvent messageEvent = this.testFrameworkRule.getInstance(MessageEvent.class);
    final ShoutOutListenerCommand subjectUnderTest = this.givenASubjectToTest();

    final boolean result = subjectUnderTest.isTriggered(messageEvent);

    assertThat(result).isFalse();
  }

  @Test
  public final void isTriggered_casterIsNotDue_resultIsFalse() {
    this.installModules();
    this.testFrameworkRule.currentTimeDelta(this.getFrequencyDefault(), TimeUnit.HOURS);
    final MessageEvent messageEvent = this.testFrameworkRule.getInstance(MessageEvent.class);
    final ShoutOutDataStore shoutOutDataStore = this.testFrameworkRule.getInstance(ShoutOutDataStore.class);
    final CasterKey casterKey = shoutOutDataStore.createCasterKey(messageEvent.getChatUser().getTwitchLogin().value);
    final Caster caster = shoutOutDataStore.createCaster(casterKey.getName(), 0L);
    shoutOutDataStore.put(casterKey, caster);
    final ShoutOutListenerCommand subjectUnderTest = this.givenASubjectToTest();

    final boolean result = subjectUnderTest.isTriggered(messageEvent);

    assertThat(result).isFalse();
  }

  @Test
  public final void isTriggered_casterIsDue_resultIsTrue() {
    this.installModules();
    this.testFrameworkRule.currentTimeDelta(this.getFrequencyDefault(), TimeUnit.HOURS, 1);
    final MessageEvent messageEvent = this.testFrameworkRule.getInstance(MessageEvent.class);
    final ShoutOutDataStore shoutOutDataStore = this.testFrameworkRule.getInstance(ShoutOutDataStore.class);
    final CasterKey casterKey = shoutOutDataStore.createCasterKey(messageEvent.getChatUser().getTwitchLogin().value);
    final Caster caster = shoutOutDataStore.createCaster(casterKey.getName(), 0L);
    shoutOutDataStore.put(casterKey, caster);
    final ShoutOutListenerCommand subjectUnderTest = this.givenASubjectToTest();

    final boolean result = subjectUnderTest.isTriggered(messageEvent);

    assertThat(result).isTrue();
  }

  @Test
  public final void isTriggered_casterIsNotDueWithCustomFrequency_resultIsFalse() {
    this.installModules();
    final int customFrequency = this.getFrequencyDefault() + 1;
    final MapConfiguration configuration = this.testFrameworkRule.getInstance(MapConfiguration.class);
    configuration.set(ConfigurationKey.of(KEY_FREQUENCY, ShoutOutListenerCommand.class),
        Integer.toString(customFrequency));
    this.testFrameworkRule.currentTimeDelta(this.getFrequencyDefault(), TimeUnit.HOURS);
    final MessageEvent messageEvent = this.testFrameworkRule.getInstance(MessageEvent.class);
    final ShoutOutDataStore shoutOutDataStore = this.testFrameworkRule.getInstance(ShoutOutDataStore.class);
    final CasterKey casterKey = shoutOutDataStore.createCasterKey(messageEvent.getChatUser().getTwitchLogin().value);
    final Caster caster = shoutOutDataStore.createCaster(casterKey.getName(), 0L);
    shoutOutDataStore.put(casterKey, caster);
    final ShoutOutListenerCommand subjectUnderTest = this.givenASubjectToTest();

    final boolean result = subjectUnderTest.isTriggered(messageEvent);

    assertThat(result).isFalse();
  }

  @Test
  public final void isTriggered_casterIsDueWithCustomFrequency_resultIsTrue() {
    this.installModules();
    final int customFrequency = this.getFrequencyDefault() + 1;
    final MapConfiguration configuration = this.testFrameworkRule.getInstance(MapConfiguration.class);
    configuration.set(ConfigurationKey.of(KEY_FREQUENCY, ShoutOutListenerCommand.class),
        Integer.toString(customFrequency));
    this.testFrameworkRule.currentTimeDelta(customFrequency, TimeUnit.HOURS, 1);
    final MessageEvent messageEvent = this.testFrameworkRule.getInstance(MessageEvent.class);
    final ShoutOutDataStore shoutOutDataStore = this.testFrameworkRule.getInstance(ShoutOutDataStore.class);
    final CasterKey casterKey = shoutOutDataStore.createCasterKey(messageEvent.getChatUser().getTwitchLogin().value);
    final Caster caster = shoutOutDataStore.createCaster(casterKey.getName(), 0L);
    shoutOutDataStore.put(casterKey, caster);
    final ShoutOutListenerCommand subjectUnderTest = this.givenASubjectToTest();

    final boolean result = subjectUnderTest.isTriggered(messageEvent);

    assertThat(result).isTrue();
  }

  @Test
  public final void handleMessageEvent_noMessageFormatConfigured_resultIsDefaultMessage() {
    this.installModules();
    final TestMessageEvent messageEvent = this.testFrameworkRule.getInstance(TestMessageEvent.class);
    final ShoutOutListenerCommand subjectUnderTest = this.givenASubjectToTest();

    subjectUnderTest.handleMessageEvent(messageEvent);
    final Message result = messageEvent.getResponseMessage();

    assertThat(result).isEqualTo(Message.of(String.format(this.getMessageFormatDefault().value,
        messageEvent.getChatUser().getTwitchLogin())));
  }

  @Test
  public final void handleMessageEvent_customMessageFormatConfigured_resultIsCustomMessage() {
    this.installModules();
    final String customMessageFormat = this.testFrameworkRule.getArbitraryString() + " %s";
    final MapConfiguration configuration = this.testFrameworkRule.getInstance(MapConfiguration.class);
    configuration.set(this.getMessageFormatKey(), customMessageFormat);
    final TestMessageEvent messageEvent = this.testFrameworkRule.getInstance(TestMessageEvent.class);
    final ShoutOutListenerCommand subjectUnderTest = this.givenASubjectToTest();

    subjectUnderTest.handleMessageEvent(messageEvent);
    final Message result = messageEvent.getResponseMessage();

    assertThat(result).isEqualTo(Message.of(String.format(customMessageFormat,
        messageEvent.getChatUser().getTwitchLogin())));
  }

  @Test
  public final void handleMessageEvent_isCalled_resultIsUpdatedDataStore() {
    this.installModules();
    final TestTimeUtils timeUtils = this.testFrameworkRule.getInstance(TestTimeUtils.class);
    final Long lastShoutOut = timeUtils.hoursAsMs(this.getFrequencyDefault());
    this.testFrameworkRule.currentTimeDelta(lastShoutOut, TimeUnit.MILLISECONDS);
    final TestMessageEvent messageEvent = this.testFrameworkRule.getInstance(TestMessageEvent.class);
    final ShoutOutDataStore shoutOutDataStore = this.testFrameworkRule.getInstance(ShoutOutDataStore.class);
    final CasterKey casterKey = shoutOutDataStore.createCasterKey(messageEvent.getChatUser().getTwitchLogin().value);
    final Caster caster = shoutOutDataStore.createCaster(casterKey.getName(), 0L);
    shoutOutDataStore.put(casterKey, caster);

    final ShoutOutListenerCommand subjectUnderTest = this.givenASubjectToTest();

    subjectUnderTest.handleMessageEvent(messageEvent);

    assertThat(shoutOutDataStore).is(new Condition<ShoutOutDataStore>() {

      @Override
      public boolean matches(final ShoutOutDataStore actual) {
        return actual.get(casterKey).isPresent() && actual.get(casterKey).get().getLastShoutOut() == lastShoutOut;
      }

    });

  }

}
