package com.mechjacktv.mechjackbot.command.shoutout;

import org.junit.Test;
import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mapdb.Serializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mechjacktv.keyvaluestore.MapKeyValueStore;
import com.mechjacktv.mechjackbot.*;
import com.mechjacktv.mechjackbot.command.ArbitraryCommandTestUtils;
import com.mechjacktv.mechjackbot.configuration.ArbitraryChatBotConfiguration;
import com.mechjacktv.mechjackbot.configuration.MapAppConfiguration;
import com.mechjacktv.proto.mechjackbot.command.shoutout.ShoutOutServiceMessage.Caster;
import com.mechjacktv.proto.mechjackbot.command.shoutout.ShoutOutServiceMessage.CasterKey;
import com.mechjacktv.twitchclient.TwitchClient;
import com.mechjacktv.util.*;
import com.mechjacktv.util.scheduleservice.ScheduleService;

import static com.mechjacktv.mechjackbot.command.shoutout.ShoutOutListenerCommand.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.*;

public final class ShoutOutListenerCommandUnitTests extends CommandContractTests {

  private static final Logger LOGGER = LoggerFactory.getLogger(ShoutOutListenerCommandUnitTests.class);

  private final ArbitraryDataGenerator arbitraryDataGenerator = new ArbitraryDataGenerator();

  private final ArbitraryCommandTestUtils commandTestUtils = new ArbitraryCommandTestUtils(this.arbitraryDataGenerator);

  private final ExecutionUtils executionUtils = new DefaultExecutionUtils();

  private final TimeUtils timeUtils = new DefaultTimeUtils();

  @Override
  protected final ShoutOutListenerCommand givenASubjectToTest(final AppConfiguration appConfiguration) {
    return this.givenASubjectToTest(appConfiguration, this.commandTestUtils.givenACommandUtils(appConfiguration),
        new DefaultTimeUtils(), this.givenAShoutOutDataStore(appConfiguration));
  }

  private ShoutOutListenerCommand givenASubjectToTest(final AppConfiguration appConfiguration,
      final TimeUtils timeUtils, final ShoutOutDataStore shoutOutDataStore) {
    return this.givenASubjectToTest(appConfiguration, this.commandTestUtils.givenACommandUtils(appConfiguration),
        timeUtils, shoutOutDataStore);
  }

  private ShoutOutListenerCommand givenASubjectToTest(final AppConfiguration appConfiguration,
      final CommandUtils commandUtils, final TimeUtils timeUtils, final ShoutOutDataStore shoutOutDataStore) {
    return new ShoutOutListenerCommand(appConfiguration, commandUtils, timeUtils, shoutOutDataStore);
  }

  private ShoutOutDataStore givenAShoutOutDataStore(final AppConfiguration appConfiguration) {
    final DB db = DBMaker.memoryDB().closeOnJvmShutdown().make();

    return new DefaultShoutOutDataStore(appConfiguration,
        new ArbitraryChatBotConfiguration(this.arbitraryDataGenerator),
        (name) -> new MapKeyValueStore(db.hashMap(name, Serializer.BYTE_ARRAY,
            Serializer.BYTE_ARRAY).createOrOpen()),
        this.executionUtils,
        new DefaultProtobufUtils(this.executionUtils), mock(ScheduleService.class), mock(TwitchClient.class));
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
  protected CommandTriggerKey getCommandTriggerKey() {
    return CommandTriggerKey.of(COMMAND_TRIGGER_KEY);
  }

  @Override
  protected CommandTrigger getCommandTriggerDefault() {
    return CommandTrigger.of(COMMAND_TRIGGER_DEFAULT);
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
    final AppConfiguration appConfiguration = new MapAppConfiguration(this.executionUtils);
    final ShoutOutDataStore shoutOutDataStore = this.givenAShoutOutDataStore(appConfiguration);
    final MessageEvent messageEvent = new ArbitraryMessageEvent(this.arbitraryDataGenerator);
    final CasterKey casterKey = shoutOutDataStore.createCasterKey(messageEvent.getChatUser().getUsername().value);
    final Caster caster = shoutOutDataStore.createCaster(casterKey.getName(), 0L);
    shoutOutDataStore.put(casterKey, caster);
    final TimeUtils timeUtils = this.givenAFakeTimeUtils();
    when(timeUtils.currentTime()).thenReturn(0L);
    final Command subjectUnderTest = this.givenASubjectToTest(appConfiguration, timeUtils, shoutOutDataStore);

    final boolean result = subjectUnderTest.isTriggered(messageEvent);

    assertThat(result).isFalse();
  }

  @Test
  public final void isTriggered_casterIsDue_returnsTrue() {
    final MapAppConfiguration appConfiguration = new MapAppConfiguration(this.executionUtils);
    final ShoutOutDataStore shoutOutDataStore = this.givenAShoutOutDataStore(appConfiguration);
    final MessageEvent messageEvent = new ArbitraryMessageEvent(this.arbitraryDataGenerator);
    final CasterKey casterKey = shoutOutDataStore.createCasterKey(messageEvent.getChatUser().getUsername().value);
    final Caster caster = shoutOutDataStore.createCaster(casterKey.getName(), 0L);
    shoutOutDataStore.put(casterKey, caster);
    final TimeUtils timeUtils = this.givenAFakeTimeUtils();
    when(timeUtils.currentTime()).thenReturn(
        this.timeUtils.hoursAsMs(Integer.parseInt(COMMAND_FREQUENCY_DEFAULT) + 1));
    final Command subjectUnderTest = this.givenASubjectToTest(appConfiguration, timeUtils, shoutOutDataStore);

    final boolean result = subjectUnderTest.isTriggered(messageEvent);

    assertThat(result).isTrue();
  }

  @Test
  public final void isTriggered_casterIsNotDueCustomFrequency_returnsFalse() {
    final MapAppConfiguration appConfiguration = new MapAppConfiguration(this.executionUtils);
    appConfiguration.set(COMMAND_FREQUENCY_KEY, "2");
    final ShoutOutDataStore shoutOutDataStore = this.givenAShoutOutDataStore(appConfiguration);
    final MessageEvent messageEvent = new ArbitraryMessageEvent(this.arbitraryDataGenerator);
    final CasterKey casterKey = shoutOutDataStore.createCasterKey(messageEvent.getChatUser().getUsername().value);
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
    final MapAppConfiguration appConfiguration = new MapAppConfiguration(this.executionUtils);
    appConfiguration.set(COMMAND_FREQUENCY_KEY, customFrequency);
    final ShoutOutDataStore shoutOutDataStore = this.givenAShoutOutDataStore(appConfiguration);
    final MessageEvent messageEvent = new ArbitraryMessageEvent(this.arbitraryDataGenerator);
    final CasterKey casterKey = shoutOutDataStore.createCasterKey(messageEvent.getChatUser().getUsername().value);
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
    final MapAppConfiguration appConfiguration = new MapAppConfiguration(this.executionUtils);
    final ArbitraryMessageEvent messageEvent = new ArbitraryMessageEvent(this.arbitraryDataGenerator);
    final Command subjectUnderTest = this.givenASubjectToTest(appConfiguration, this.givenAFakeTimeUtils(),
        this.givenAShoutOutDataStore(appConfiguration));

    subjectUnderTest.handleMessageEvent(messageEvent);

    final ChatUsername chatUsername = this.commandTestUtils.givenACommandUtils(appConfiguration)
        .sanitizeChatUsername(messageEvent.getChatUser().getUsername());
    assertThat(messageEvent.getResponseMessage()).isNotNull();
    assertThat(messageEvent.getResponseMessage().value).isEqualTo(String.format(COMMAND_MESSAGE_FORMAT_DEFAULT,
        chatUsername));
  }

  @Test
  public final void handleMessageEvent_isCalledUsesCustomMessage_sendsResponse() {
    final String customMessageFormat = this.arbitraryDataGenerator.getString() + " %s";
    final MapAppConfiguration appConfiguration = new MapAppConfiguration(this.executionUtils);
    appConfiguration.set(COMMAND_MESSAGE_FORMAT_KEY, customMessageFormat);
    final ArbitraryMessageEvent messageEvent = new ArbitraryMessageEvent(this.arbitraryDataGenerator);
    final Command subjectUnderTest = this.givenASubjectToTest(appConfiguration, this.givenAFakeTimeUtils(),
        this.givenAShoutOutDataStore(appConfiguration));

    subjectUnderTest.handleMessageEvent(messageEvent);

    final ChatUsername chatUsername = this.commandTestUtils.givenACommandUtils(appConfiguration)
        .sanitizeChatUsername(messageEvent.getChatUser().getUsername());
    assertThat(messageEvent.getResponseMessage()).isNotNull();
    assertThat(messageEvent.getResponseMessage().value).isEqualTo(String.format(customMessageFormat, chatUsername));
  }

  @Test
  public final void handleMessageEvent_isCalled_updateDataStore() {
    final MapAppConfiguration appConfiguration = new MapAppConfiguration(this.executionUtils);
    final ArbitraryMessageEvent messageEvent = new ArbitraryMessageEvent(this.arbitraryDataGenerator);
    final CasterKey casterKey = CasterKey.newBuilder()
        .setName(messageEvent.getChatUser().getUsername().value.toLowerCase()).build();
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
