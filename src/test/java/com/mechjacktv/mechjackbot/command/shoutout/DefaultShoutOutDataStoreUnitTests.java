package com.mechjacktv.mechjackbot.command.shoutout;

import static com.mechjacktv.mechjackbot.command.shoutout.DefaultShoutOutDataStore.UPDATE_PERIOD_DEFAULT;
import static com.mechjacktv.mechjackbot.command.shoutout.DefaultShoutOutDataStore.UPDATE_PERIOD_KEY;
import static com.mechjacktv.proto.twitchclient.TwitchClientMessage.UserFollows;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.*;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Test;

import com.mechjacktv.configuration.Configuration;
import com.mechjacktv.configuration.MapConfiguration;
import com.mechjacktv.keyvaluestore.MapKeyValueStore;
import com.mechjacktv.keyvaluestore.MessageStoreContractTests;
import com.mechjacktv.mechjackbot.chatbot.ArbitraryChatBotConfiguration;
import com.mechjacktv.proto.mechjackbot.command.shoutout.ShoutOutServiceMessage.Caster;
import com.mechjacktv.proto.mechjackbot.command.shoutout.ShoutOutServiceMessage.CasterKey;
import com.mechjacktv.proto.twitchclient.TwitchClientMessage.UserFollow;
import com.mechjacktv.testframework.ArbitraryDataGenerator;
import com.mechjacktv.twitchclient.TwitchClient;
import com.mechjacktv.twitchclient.TwitchLogin;
import com.mechjacktv.twitchclient.TwitchUserFollowsCursor;
import com.mechjacktv.twitchclient.TwitchUserId;
import com.mechjacktv.util.DefaultProtobufUtils;
import com.mechjacktv.util.IORuntimeException;
import com.mechjacktv.util.scheduleservice.ScheduleService;

public class DefaultShoutOutDataStoreUnitTests extends MessageStoreContractTests<CasterKey, Caster> {

  private final ArbitraryDataGenerator arbitraryDataGenerator = new ArbitraryDataGenerator();

  protected final CasterKey givenAKey() {
    return CasterKey.newBuilder().setName(this.arbitraryDataGenerator.getString()).build();
  }

  @Override
  protected final Caster givenAValue() {
    return Caster.newBuilder().setName(this.arbitraryDataGenerator.getString())
        .setLastShoutOut(this.arbitraryDataGenerator.getLong()).build();
  }

  @Override
  protected final DefaultShoutOutDataStore givenASubjectToTest(final Map<CasterKey, Caster> data) {
    return this.givenASubjectToTest(data, mock(ScheduleService.class));
  }

  private DefaultShoutOutDataStore givenASubjectToTest(final Map<CasterKey, Caster> data,
      final ScheduleService scheduleService) {
    return this.givenASubjectToTest(data, scheduleService, new MapConfiguration(this.executionUtils));
  }

  private DefaultShoutOutDataStore givenASubjectToTest(final Map<CasterKey, Caster> data,
      final ScheduleService scheduleService, final Configuration configuration) {
    return this.givenASubjectToTest(data, scheduleService, configuration, mock(TwitchClient.class));
  }

  private DefaultShoutOutDataStore givenASubjectToTest(final Map<CasterKey, Caster> data,
      final ScheduleService scheduleService, final TwitchClient twitchClient) {
    return this.givenASubjectToTest(data, scheduleService, new MapConfiguration(this.executionUtils), twitchClient);
  }

  private DefaultShoutOutDataStore givenASubjectToTest(final Map<CasterKey, Caster> data,
      final ScheduleService scheduleService, final Configuration configuration, final TwitchClient twitchClient) {
    final MapKeyValueStore dataStore = new MapKeyValueStore();

    for (final CasterKey key : data.keySet()) {
      dataStore.put(key.toByteArray(), data.get(key).toByteArray());
    }

    return new DefaultShoutOutDataStore(configuration,
        new ArbitraryChatBotConfiguration(this.arbitraryDataGenerator), (name) -> dataStore, this.executionUtils,
        new DefaultProtobufUtils(this.executionUtils), scheduleService, twitchClient);
  }

  private ScheduleService givenAFakeScheduleService() {
    final ScheduleService scheduleService = mock(ScheduleService.class);

    doAnswer((invocation) -> {
      final Runnable runnable = invocation.getArgument(0);

      runnable.run();
      return null;
    }).when(scheduleService).schedule(isA(Runnable.class), isA(Integer.class), isA(TimeUnit.class));
    return scheduleService;
  }

  private UserFollows givenAUserFollows(final TwitchUserId twitchUserId) {
    return this.givenAUserFollows(twitchUserId, 3, 3);
  }

  private UserFollows givenAUserFollows(final TwitchUserId twitchUserId,
      final int followsBatchSize, final int totalFollows) {
    final UserFollows.Builder builder = UserFollows.newBuilder();

    builder.setTotalFollows(totalFollows);
    builder.setCursor(this.arbitraryDataGenerator.getString());
    for (int i = 0; i < followsBatchSize; i++) {
      builder.addUserFollow(this.givenAUserFollow(twitchUserId));
    }
    return builder.build();
  }

  private UserFollow givenAUserFollow(final TwitchUserId twitchUserId) {
    final UserFollow.Builder builder = UserFollow.newBuilder();

    builder.setFromId(twitchUserId.value);
    builder.setFromName(twitchUserId.value);
    builder.setToId(this.arbitraryDataGenerator.getString());
    builder.setToName(this.arbitraryDataGenerator.getString());
    builder.setFollowedAt(this.arbitraryDataGenerator.getString());
    return builder.build();
  }

  private Map<CasterKey, Caster> givenACasterDataMap(final UserFollow... userFollows) {
    final Map<CasterKey, Caster> data = new HashMap<>();

    for (final UserFollow userFollow : userFollows) {
      final CasterKey key = CasterKey.newBuilder().setName(userFollow.getToName().toLowerCase()).build();
      final Caster caster = Caster.newBuilder().setName(userFollow.getToName()).setLastShoutOut(0L).build();
      data.put(key, caster);
    }
    return data;
  }

  @Test
  public final void createCasterKey_nullName_throwsNullPointerException() {
    final DefaultShoutOutDataStore subjectUnderTest = this.givenASubjectToTest(new HashMap<>());

    final Throwable thrown = catchThrowable(() -> subjectUnderTest.createCasterKey(null));

    assertThat(thrown).isInstanceOf(NullPointerException.class).hasMessage(this.executionUtils.nullMessageForName(
        "casterName"));
  }

  @Test
  public final void createCasterKey_forName_returnsCasterKeyWithName() {
    final String casterName = this.arbitraryDataGenerator.getString();
    final DefaultShoutOutDataStore subjectUnderTest = this.givenASubjectToTest(new HashMap<>());

    final CasterKey result = subjectUnderTest.createCasterKey(casterName);

    assertThat(result.getName()).isEqualTo(casterName.toLowerCase());
  }

  @Test
  public final void createCaster_nullName_throwsNullPointerException() {
    final DefaultShoutOutDataStore subjectUnderTest = this.givenASubjectToTest(new HashMap<>());

    final Throwable thrown = catchThrowable(
        () -> subjectUnderTest.createCaster(null, this.arbitraryDataGenerator.getLong()));

    assertThat(thrown).isInstanceOf(NullPointerException.class).hasMessage(this.executionUtils.nullMessageForName(
        "casterName"));
  }

  @Test
  public final void createCaster_nullLastShoutOut_throwsNullPointerException() {
    final DefaultShoutOutDataStore subjectUnderTest = this.givenASubjectToTest(new HashMap<>());

    final Throwable thrown = catchThrowable(
        () -> subjectUnderTest.createCaster(this.arbitraryDataGenerator.getString(), null));

    assertThat(thrown).isInstanceOf(NullPointerException.class).hasMessage(this.executionUtils.nullMessageForName(
        "lastShoutOut"));
  }

  @Test
  public final void createCasterKey_forNameAndLastShoutOut_returnsCasterWithNameAndLastShoutOut() {
    final String casterName = this.arbitraryDataGenerator.getString();
    final Long lastShoutOut = this.arbitraryDataGenerator.getLong();
    final DefaultShoutOutDataStore subjectUnderTest = this.givenASubjectToTest(new HashMap<>());

    final Caster result = subjectUnderTest.createCaster(casterName, lastShoutOut);

    assertThat(result).isEqualTo(Caster.newBuilder().setName(casterName).setLastShoutOut(lastShoutOut).build());
  }

  @Test
  public final void new_whenCreatedWithDefaultPeriod_schedulesUpdate() {
    final Integer period = Integer.parseInt(UPDATE_PERIOD_DEFAULT);
    final ScheduleService scheduleService = mock(ScheduleService.class);

    this.givenASubjectToTest(new HashMap<>(), scheduleService);

    verify(scheduleService).schedule(isA(Runnable.class), eq(period), eq(TimeUnit.MINUTES));
  }

  @Test
  public final void new_whenCreatedWithCustomPeriod_schedulesUpdate() {
    final int period = this.arbitraryDataGenerator.getInteger();
    final MapConfiguration appConfiguration = new MapConfiguration(this.executionUtils);
    final ScheduleService scheduleService = mock(ScheduleService.class);
    appConfiguration.set(UPDATE_PERIOD_KEY, Integer.toString(period));

    this.givenASubjectToTest(new HashMap<>(), scheduleService, appConfiguration);

    verify(scheduleService).schedule(isA(Runnable.class), eq(period), eq(TimeUnit.MINUTES));
  }

  @Test
  public final void new_noIdForChannel_failsGracefully() {
    final TwitchClient twitchClient = mock(TwitchClient.class);
    final ScheduleService scheduleService = this.givenAFakeScheduleService();

    this.givenASubjectToTest(new HashMap<>(), scheduleService, twitchClient);

    verify(twitchClient, never()).getUserFollowsFromId(isA(TwitchUserId.class));
  }

  @Test
  public final void new_withIdForChannel_queriesTwitchForFollowData() {
    final TwitchUserId twitchUserId = TwitchUserId.of(this.arbitraryDataGenerator.getString());
    final TwitchClient twitchClient = mock(TwitchClient.class);
    final ScheduleService scheduleService = this.givenAFakeScheduleService();
    when(twitchClient.getUserId(isA(TwitchLogin.class))).thenReturn(Optional.of(twitchUserId));

    this.givenASubjectToTest(new HashMap<>(), scheduleService, twitchClient);

    verify(twitchClient).getUserFollowsFromId(eq(twitchUserId));
  }

  @Test
  public final void new_callToTwitchThrowsException_failsGracefully() {
    final TwitchClient twitchClient = mock(TwitchClient.class);
    final ScheduleService scheduleService = this.givenAFakeScheduleService();
    when(twitchClient.getUserId(isA(TwitchLogin.class))).thenThrow(IORuntimeException.class);

    final Throwable thrown = catchThrowable(() -> this.givenASubjectToTest(new HashMap<>(), scheduleService,
        twitchClient));

    assertThat(thrown).isNull();
  }

  @Test
  public final void new_withFollowsForId_addsCasterToDataSet() {
    final TwitchUserId twitchUserId = TwitchUserId.of(this.arbitraryDataGenerator.getString());
    final TwitchClient twitchClient = mock(TwitchClient.class);
    final ScheduleService scheduleService = this.givenAFakeScheduleService();
    final UserFollows userFollows = this.givenAUserFollows(twitchUserId);
    when(twitchClient.getUserId(isA(TwitchLogin.class))).thenReturn(Optional.of(twitchUserId));
    when(twitchClient.getUserFollowsFromId(eq(twitchUserId))).thenReturn(userFollows);

    final DefaultShoutOutDataStore result = this.givenASubjectToTest(new HashMap<>(), scheduleService, twitchClient);

    assertThat(result.getKeys()).containsExactlyInAnyOrderElementsOf(userFollows.getUserFollowList().stream()
        .map(userFollow -> result.createCasterKey(userFollow.getToName())).collect(Collectors.toSet()));
  }

  @Test
  public final void new_withExistingCasterData_doesNotRemoveStillFollowedCaster() {
    final TwitchUserId twitchUserId = TwitchUserId.of(this.arbitraryDataGenerator.getString());
    final TwitchClient twitchClient = mock(TwitchClient.class);
    final UserFollows userFollows = this.givenAUserFollows(twitchUserId);
    final UserFollow userFollow = userFollows.getUserFollow(0);
    when(twitchClient.getUserId(isA(TwitchLogin.class))).thenReturn(Optional.of(twitchUserId));
    when(twitchClient.getUserFollowsFromId(eq(twitchUserId))).thenReturn(userFollows);

    final DefaultShoutOutDataStore result = this.givenASubjectToTest(this.givenACasterDataMap(userFollow),
        this.givenAFakeScheduleService(), twitchClient);

    final CasterKey key = result.createCasterKey(userFollow.getToName());
    assertThat(result.containsKey(key)).isTrue();
  }

  @Test
  public final void new_withExistingCasterData_doesRemoveUnfollowedCaster() {
    final TwitchUserId twitchUserId = TwitchUserId.of(this.arbitraryDataGenerator.getString());
    final UserFollows userFollows = this.givenAUserFollows(twitchUserId);
    final UserFollow userFollow = this.givenAUserFollow(twitchUserId);
    final TwitchClient twitchClient = mock(TwitchClient.class);
    when(twitchClient.getUserId(isA(TwitchLogin.class))).thenReturn(Optional.of(twitchUserId));
    when(twitchClient.getUserFollowsFromId(eq(twitchUserId))).thenReturn(userFollows);

    final DefaultShoutOutDataStore result = this.givenASubjectToTest(this.givenACasterDataMap(userFollow),
        this.givenAFakeScheduleService(), twitchClient);

    final CasterKey key = result.createCasterKey(userFollow.getToName());
    assertThat(result.containsKey(key)).isFalse();
  }

  @Test
  public final void new_pagingData_getsAllData() {
    final TwitchUserId twitchUserId = TwitchUserId.of(this.arbitraryDataGenerator.getString());
    final UserFollows userFollows1 = this.givenAUserFollows(twitchUserId, 3, 6);
    final UserFollows userFollows2 = this.givenAUserFollows(twitchUserId, 3, 6);
    final TwitchClient twitchClient = mock(TwitchClient.class);
    when(twitchClient.getUserId(isA(TwitchLogin.class))).thenReturn(Optional.of(twitchUserId));
    when(twitchClient.getUserFollowsFromId(eq(twitchUserId))).thenReturn(userFollows1);
    when(twitchClient.getUserFollowsFromId(eq(twitchUserId),
        eq(TwitchUserFollowsCursor.of(userFollows1.getCursor())))).thenReturn(userFollows2);

    final DefaultShoutOutDataStore result = this.givenASubjectToTest(new HashMap<>(),
        this.givenAFakeScheduleService(), twitchClient);

    final Set<UserFollow> allUserFollow = Stream.of(userFollows1.getUserFollowList(), userFollows2.getUserFollowList())
        .flatMap(Collection::stream).collect(Collectors.toSet());
    assertThat(result.getKeys()).containsExactlyInAnyOrderElementsOf(allUserFollow.stream()
        .map(userFollow -> result.createCasterKey(userFollow.getToName())).collect(Collectors.toSet()));
  }

  @Test
  public final void new_pagingDataPageTwoEmpty_getsAvailableData() {
    final TwitchUserId twitchUserId = TwitchUserId.of(this.arbitraryDataGenerator.getString());
    final UserFollows userFollows1 = this.givenAUserFollows(twitchUserId, 3, 6);
    final UserFollows userFollows2 = this.givenAUserFollows(twitchUserId, 0, 6);
    final TwitchClient twitchClient = mock(TwitchClient.class);
    when(twitchClient.getUserId(isA(TwitchLogin.class))).thenReturn(Optional.of(twitchUserId));
    when(twitchClient.getUserFollowsFromId(eq(twitchUserId))).thenReturn(userFollows1);
    when(twitchClient.getUserFollowsFromId(eq(twitchUserId),
        eq(TwitchUserFollowsCursor.of(userFollows1.getCursor())))).thenReturn(userFollows2);

    final DefaultShoutOutDataStore result = this.givenASubjectToTest(new HashMap<>(),
        this.givenAFakeScheduleService(), twitchClient);

    assertThat(result.getKeys()).containsExactlyInAnyOrderElementsOf(userFollows1.getUserFollowList().stream()
        .map(userFollow -> result.createCasterKey(userFollow.getToName())).collect(Collectors.toSet()));
  }

}
