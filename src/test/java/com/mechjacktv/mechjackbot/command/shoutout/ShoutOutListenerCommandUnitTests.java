package com.mechjacktv.mechjackbot.command.shoutout;

import com.mechjacktv.configuration.Configuration;
import com.mechjacktv.configuration.MapConfiguration;
import com.mechjacktv.configuration.SettingKey;
import com.mechjacktv.keyvaluestore.MapKeyValueStore;
import com.mechjacktv.mechjackbot.*;
import com.mechjacktv.mechjackbot.chatbot.ArbitraryChatBotConfiguration;
import com.mechjacktv.mechjackbot.command.AbstractCommand;
import com.mechjacktv.mechjackbot.command.ArbitraryCommandTestUtils;
import com.mechjacktv.mechjackbot.command.DefaultCommandConfigurationBuilder;
import com.mechjacktv.proto.mechjackbot.command.shoutout.ShoutOutServiceMessage.Caster;
import com.mechjacktv.proto.mechjackbot.command.shoutout.ShoutOutServiceMessage.CasterKey;
import com.mechjacktv.twitchclient.TwitchClient;
import com.mechjacktv.util.*;
import com.mechjacktv.util.scheduleservice.ScheduleService;

import org.junit.Test;

import static com.mechjacktv.mechjackbot.command.AbstractCommand.MESSAGE_FORMAT_KEY;
import static com.mechjacktv.mechjackbot.command.shoutout.ShoutOutListenerCommand.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public final class ShoutOutListenerCommandUnitTests extends CommandContractTests {

  private final ArbitraryDataGenerator arbitraryDataGenerator = new ArbitraryDataGenerator();

  private final ArbitraryCommandTestUtils commandTestUtils = new ArbitraryCommandTestUtils(this.arbitraryDataGenerator);

  private final ExecutionUtils executionUtils = new DefaultExecutionUtils();

  private final TimeUtils timeUtils = new DefaultTimeUtils();

  @Override
  protected final ShoutOutListenerCommand givenASubjectToTest(final Configuration configuration) {
    return this.givenASubjectToTest(configuration, this.commandTestUtils.givenACommandUtils(configuration),
        new DefaultTimeUtils(), this.givenAShoutOutDataStore(configuration));
  }

  private ShoutOutListenerCommand givenASubjectToTest(final Configuration configuration,
      final TimeUtils timeUtils, final ShoutOutDataStore shoutOutDataStore) {
    return this.givenASubjectToTest(configuration, this.commandTestUtils.givenACommandUtils(configuration),
        timeUtils, shoutOutDataStore);
  }

  private ShoutOutListenerCommand givenASubjectToTest(final Configuration configuration,
      final CommandUtils commandUtils, final TimeUtils timeUtils, final ShoutOutDataStore shoutOutDataStore) {
    final DefaultCommandConfigurationBuilder builder = new DefaultCommandConfigurationBuilder(commandUtils,
        configuration);

    return new ShoutOutListenerCommand(builder, configuration, shoutOutDataStore, timeUtils);
  }

  private ShoutOutDataStore givenAShoutOutDataStore(final Configuration configuration) {
    return new DefaultShoutOutDataStore(configuration,
        new ArbitraryChatBotConfiguration(this.arbitraryDataGenerator), (name) -> new MapKeyValueStore(),
        this.executionUtils, new DefaultProtobufUtils(this.executionUtils), mock(ScheduleService.class),
        mock(TwitchClient.class));
  }

  private TimeUtils givenAFakeTimeUtils() {
    final TimeUtils timeUtils = mock(TimeUtils.class);

    when(timeUtils.currentTime()).thenAnswer((invocation -> this.timeUtils.currentTime()));
    when(timeUtils.secondsAsMs(isA(Integer.class)))
        .thenAnswer((invocation -> this.timeUtils.secondsAsMs(invocation.getArgument(0))));
    when(timeUtils.hoursAsMs(isA(Integer.class)))
        .thenAnswer((invocation -> this.timeUtils.hoursAsMs(invocation.getArgument(0))));
    return timeUtils;
  }

  @Override
  protected SettingKey getCommandTriggerKey() {
    return SettingKey.of(ShoutOutListenerCommand.class, AbstractCommand.TRIGGER_KEY);
  }

  @Override
  protected CommandTrigger getCommandTriggerDefault() {
    return CommandTrigger.of(TRIGGER_DEFAULT);
  }

  @Test
  public final void isTriggered_noCasterPresent_returnsFalse() {
    final MessageEvent messageEvent = new ArbitraryMessageEvent(this.arbitraryDataGenerator);
    final Command subjectUnderTest = this.givenASubjectToTest();

    final boolean result = subjectUnderTest.isTriggered(messageEvent);

    assertThat(result).isFalse();
  }

  @Test
  public final void isTriggered_casterIsNotDue_returnsFalse() {
    final Configuration configuration = new MapConfiguration(this.executionUtils);
    final ShoutOutDataStore shoutOutDataStore = this.givenAShoutOutDataStore(configuration);
    final MessageEvent messageEvent = new ArbitraryMessageEvent(this.arbitraryDataGenerator);
    final CasterKey casterKey = shoutOutDataStore.createCasterKey(messageEvent.getChatUser().getTwitchLogin().value);
    final Caster caster = shoutOutDataStore.createCaster(casterKey.getName(), 0L);
    shoutOutDataStore.put(casterKey, caster);
    final TimeUtils timeUtils = this.givenAFakeTimeUtils();
    when(timeUtils.currentTime()).thenReturn(0L);
    final Command subjectUnderTest = this.givenASubjectToTest(configuration, timeUtils, shoutOutDataStore);

    final boolean result = subjectUnderTest.isTriggered(messageEvent);

    assertThat(result).isFalse();
  }

  @Test
  public final void isTriggered_casterIsDue_returnsTrue() {
    final MapConfiguration appConfiguration = new MapConfiguration(this.executionUtils);
    final ShoutOutDataStore shoutOutDataStore = this.givenAShoutOutDataStore(appConfiguration);
    final MessageEvent messageEvent = new ArbitraryMessageEvent(this.arbitraryDataGenerator);
    final CasterKey casterKey = shoutOutDataStore.createCasterKey(messageEvent.getChatUser().getTwitchLogin().value);
    final Caster caster = shoutOutDataStore.createCaster(casterKey.getName(), 0L);
    shoutOutDataStore.put(casterKey, caster);
    final TimeUtils timeUtils = this.givenAFakeTimeUtils();
    when(timeUtils.currentTime()).thenReturn(
        this.timeUtils.hoursAsMs(Integer.parseInt(FREQUENCY_DEFAULT) + 1));
    final Command subjectUnderTest = this.givenASubjectToTest(appConfiguration, timeUtils, shoutOutDataStore);

    final boolean result = subjectUnderTest.isTriggered(messageEvent);

    assertThat(result).isTrue();
  }

  @Test
  public final void isTriggered_casterIsNotDueCustomFrequency_returnsFalse() {
    final MapConfiguration appConfiguration = new MapConfiguration(this.executionUtils);
    appConfiguration.set(FREQUENCY_KEY, "2");
    final ShoutOutDataStore shoutOutDataStore = this.givenAShoutOutDataStore(appConfiguration);
    final MessageEvent messageEvent = new ArbitraryMessageEvent(this.arbitraryDataGenerator);
    final CasterKey casterKey = shoutOutDataStore.createCasterKey(messageEvent.getChatUser().getTwitchLogin().value);
    final Caster caster = shoutOutDataStore.createCaster(casterKey.getName(), 0L);
    shoutOutDataStore.put(casterKey, caster);
    final TimeUtils timeUtils = this.givenAFakeTimeUtils();
    when(timeUtils.currentTime()).thenReturn(this.timeUtils.hoursAsMs(1));
    final Command subjectUnderTest = this.givenASubjectToTest(appConfiguration, timeUtils, shoutOutDataStore);

    final boolean result = subjectUnderTest.isTriggered(messageEvent);

    assertThat(result).isFalse();
  }

  @Test
  public final void isTriggered_casterIsDueCustomFrequency_returnsTrue() {
    final String customFrequency = "2";
    final MapConfiguration appConfiguration = new MapConfiguration(this.executionUtils);
    appConfiguration.set(FREQUENCY_KEY, customFrequency);
    final ShoutOutDataStore shoutOutDataStore = this.givenAShoutOutDataStore(appConfiguration);
    final MessageEvent messageEvent = new ArbitraryMessageEvent(this.arbitraryDataGenerator);
    final CasterKey casterKey = shoutOutDataStore.createCasterKey(messageEvent.getChatUser().getTwitchLogin().value);
    final Caster caster = shoutOutDataStore.createCaster(casterKey.getName(), 0L);
    shoutOutDataStore.put(casterKey, caster);
    final TimeUtils timeUtils = this.givenAFakeTimeUtils();
    when(timeUtils.currentTime()).thenReturn(
        this.timeUtils.hoursAsMs(Integer.parseInt(customFrequency) + 1));
    final Command subjectUnderTest = this.givenASubjectToTest(appConfiguration, timeUtils, shoutOutDataStore);

    final boolean result = subjectUnderTest.isTriggered(messageEvent);

    assertThat(result).isTrue();
  }

  @Test
  public final void handleMessageEvent_isCalled_sendsResponse() {
    final MapConfiguration appConfiguration = new MapConfiguration(this.executionUtils);
    final ArbitraryMessageEvent messageEvent = new ArbitraryMessageEvent(this.arbitraryDataGenerator);
    final Command subjectUnderTest = this.givenASubjectToTest(appConfiguration, this.givenAFakeTimeUtils(),
        this.givenAShoutOutDataStore(appConfiguration));

    subjectUnderTest.handleMessageEvent(messageEvent);

    assertThat(messageEvent.getResponseMessage()).isNotNull();
    assertThat(messageEvent.getResponseMessage().value).isEqualTo(String.format(MESSAGE_FORMAT_DEFAULT,
        messageEvent.getChatUser().getTwitchLogin()));
  }

  @Test
  public final void handleMessageEvent_isCalledUsesCustomMessage_sendsResponse() {
    final String customMessageFormat = this.arbitraryDataGenerator.getString() + " %s";
    final MapConfiguration appConfiguration = new MapConfiguration(this.executionUtils);
    appConfiguration.set(SettingKey.of(ShoutOutListenerCommand.class, MESSAGE_FORMAT_KEY).value, customMessageFormat);
    final ArbitraryMessageEvent messageEvent = new ArbitraryMessageEvent(this.arbitraryDataGenerator);
    final Command subjectUnderTest = this.givenASubjectToTest(appConfiguration, this.givenAFakeTimeUtils(),
        this.givenAShoutOutDataStore(appConfiguration));

    subjectUnderTest.handleMessageEvent(messageEvent);

    assertThat(messageEvent.getResponseMessage()).isNotNull();
    assertThat(messageEvent.getResponseMessage().value).isEqualTo(String.format(customMessageFormat,
        messageEvent.getChatUser().getTwitchLogin()));
  }

  @Test
  public final void handleMessageEvent_isCalled_updateDataStore() {
    final MapConfiguration appConfiguration = new MapConfiguration(this.executionUtils);
    final ArbitraryMessageEvent messageEvent = new ArbitraryMessageEvent(this.arbitraryDataGenerator);
    final CasterKey casterKey = CasterKey.newBuilder()
        .setName(messageEvent.getChatUser().getTwitchLogin().value.toLowerCase()).build();
    final Caster caster = Caster.newBuilder().setName(casterKey.getName()).setLastShoutOut(0L).build();
    final ShoutOutDataStore shoutOutDataStore = mock(ShoutOutDataStore.class);
    when(shoutOutDataStore.createCasterKey(isA(String.class))).thenReturn(casterKey);
    when(shoutOutDataStore.createCaster(isA(String.class), anyLong())).thenReturn(caster);
    final Command subjectUnderTest = this.givenASubjectToTest(appConfiguration, this.givenAFakeTimeUtils(),
        shoutOutDataStore);

    subjectUnderTest.handleMessageEvent(messageEvent);

    verify(shoutOutDataStore).put(eq(casterKey), eq(caster));
  }

}
