package com.mechjacktv.mechjackbot.command.shoutout;

import static com.mechjacktv.mechjackbot.command.shoutout.ShoutOutListenerCommand.COMMAND_TRIGGER_DEFAULT;
import static com.mechjacktv.mechjackbot.command.shoutout.ShoutOutListenerCommand.COMMAND_TRIGGER_KEY;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashMap;

import com.mechjacktv.keyvaluestore.MapKeyValueStore;
import com.mechjacktv.mechjackbot.*;
import com.mechjacktv.mechjackbot.command.AbstractCommandTestUtils;
import com.mechjacktv.mechjackbot.configuration.ArbitraryChatBotConfiguration;
import com.mechjacktv.mechjackbot.configuration.MapAppConfiguration;
import com.mechjacktv.twitchclient.TwitchClient;
import com.mechjacktv.util.*;
import com.mechjacktv.util.scheduleservice.ScheduleService;

public final class ShoutOutListenerCommandUnitTests extends CommandContractTests {

  private final ArbitraryDataGenerator arbitraryDataGenerator = new ArbitraryDataGenerator();

  private final AbstractCommandTestUtils commandTestUtils = new AbstractCommandTestUtils(this.arbitraryDataGenerator);

  private final ExecutionUtils executionUtils = new DefaultExecutionUtils();

  private final TimeUtils timeUtils = new DefaultTimeUtils();

  @Override
  protected final ShoutOutListenerCommand givenASubjectToTest(final AppConfiguration appConfiguration) {
    return this.givenASubjectToTest(appConfiguration, this.commandTestUtils.givenACommandUtils(appConfiguration),
        new DefaultTimeUtils(), this.givenAShoutOutDataStore(appConfiguration));
  }

  private ShoutOutListenerCommand givenASubjectToTest(final TimeUtils timeUtils) {
    final AppConfiguration appConfiguration = new MapAppConfiguration(this.executionUtils);

    return this.givenASubjectToTest(appConfiguration, this.commandTestUtils.givenACommandUtils(appConfiguration),
        timeUtils, this.givenAShoutOutDataStore(appConfiguration));
  }

  private ShoutOutListenerCommand givenASubjectToTest(final AppConfiguration appConfiguration,
      final CommandUtils commandUtils, final TimeUtils timeUtils, final ShoutOutDataStore shoutOutDataStore) {
    return new ShoutOutListenerCommand(appConfiguration, commandUtils, timeUtils, shoutOutDataStore);
  }

  private ShoutOutDataStore givenAShoutOutDataStore(final AppConfiguration appConfiguration) {
    return new DefaultShoutOutDataStore(appConfiguration,
        new ArbitraryChatBotConfiguration(this.arbitraryDataGenerator),
        (name) -> new MapKeyValueStore(new HashMap<>()), this.executionUtils,
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
//
//  @Test
//  public final void isTriggered_casterIsDue_returnsTrue() {
//    final MessageEvent messageEvent = new ArbitraryMessageEvent(this.arbitraryDataGenerator);
//    final TimeUtils timeUtils = this.givenAFakeTimeUtils();
//    final Command command = this.givenASubjectToTest(timeUtils);
//
//    final boolean result = command.isTriggered(messageEvent);
//
//    assertThat(result).isTrue();
//  }

}
