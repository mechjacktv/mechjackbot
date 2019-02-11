package tv.mechjack.mechjackbot.feature.shoutout;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static tv.mechjack.twitchclient.ProtoMessage.UserFollows;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.assertj.core.api.SoftAssertions;
import org.junit.Test;

import tv.mechjack.mechjackbot.api.ChatBotConfiguration;
import tv.mechjack.mechjackbot.api.TestCommandModule;
import tv.mechjack.mechjackbot.chatbot.TestChatBotModule;
import tv.mechjack.mechjackbot.feature.shoutout.ProtoMessage.Caster;
import tv.mechjack.mechjackbot.feature.shoutout.ProtoMessage.CasterKey;
import tv.mechjack.platform.configuration.Configuration;
import tv.mechjack.platform.configuration.ConfigurationKey;
import tv.mechjack.platform.configuration.MapConfiguration;
import tv.mechjack.platform.configuration.TestConfigurationModule;
import tv.mechjack.platform.keyvaluestore.MapKeyValueStore;
import tv.mechjack.platform.utils.ExecutionUtils;
import tv.mechjack.platform.utils.scheduleservice.ScheduleService;
import tv.mechjack.platform.utils.scheduleservice.TestScheduleService;
import tv.mechjack.platform.utils.scheduleservice.TestScheduleServiceModule;
import tv.mechjack.protobuf.BaseMessageStoreContractTests;
import tv.mechjack.protobuf.ProtobufUtils;
import tv.mechjack.twitchclient.ProtoMessage.UserFollow;
import tv.mechjack.twitchclient.TestTwitchClient;
import tv.mechjack.twitchclient.TestTwitchClientModule;
import tv.mechjack.twitchclient.TwitchClient;
import tv.mechjack.twitchclient.TwitchUserId;

public class DefaultShoutOutDataStoreUnitTests extends
    BaseMessageStoreContractTests<CasterKey, Caster> {

  @Test
  public final void createCasterKey_nullName_throwsNullPointerException() {
    this.installModules();
    final DefaultShoutOutDataStore subjectUnderTest = this.givenASubjectToTest(new HashMap<>());

    final Throwable thrown = catchThrowable(() -> subjectUnderTest.createCasterKey(null));

    assertThat(thrown).isInstanceOf(NullPointerException.class)
        .hasMessage(this.testFrameworkRule.getInstance(ExecutionUtils.class)
            .nullMessageForName("casterName"));
  }

  @Override
  protected void installModules() {
    super.installModules();
    this.testFrameworkRule.registerModule(new TestCommandModule());
    this.testFrameworkRule.registerModule(new TestConfigurationModule());
    this.testFrameworkRule.registerModule(new TestChatBotModule());
    this.testFrameworkRule.registerModule(new TestScheduleServiceModule());
    this.testFrameworkRule.registerModule(new TestTwitchClientModule());
  }

  @Override
  protected final DefaultShoutOutDataStore givenASubjectToTest() {
    return this.givenASubjectToTest(new HashMap<>());
  }

  @Override
  protected final DefaultShoutOutDataStore givenASubjectToTest(final Map<CasterKey, Caster> data) {
    final MapKeyValueStore dataStore = new MapKeyValueStore();

    for (final CasterKey key : data.keySet()) {
      dataStore.put(key.toByteArray(), data.get(key).toByteArray());
    }

    return new DefaultShoutOutDataStore(this.testFrameworkRule.getInstance(Configuration.class),
        this.testFrameworkRule.getInstance(ChatBotConfiguration.class), (name) -> dataStore,
        this.testFrameworkRule.getInstance(ExecutionUtils.class),
        this.testFrameworkRule.getInstance(ProtobufUtils.class),
        this.testFrameworkRule.getInstance(ScheduleService.class),
        this.testFrameworkRule.getInstance(TwitchClient.class));
  }

  @Override
  protected final CasterKey givenAKey() {
    return CasterKey.newBuilder().setName(this.testFrameworkRule.arbitraryData().getString()).build();
  }

  @Override
  protected final Caster givenAValue() {
    return Caster.newBuilder().setName(this.testFrameworkRule.arbitraryData().getString())
        .setLastShoutOut(this.testFrameworkRule.arbitraryData().getLong()).build();
  }

  @Test
  public final void createCasterKey_forName_returnsCasterKeyWithName() {
    this.installModules();
    final String casterName = this.testFrameworkRule.arbitraryData().getString();
    final DefaultShoutOutDataStore subjectUnderTest = this.givenASubjectToTest(new HashMap<>());

    final CasterKey result = subjectUnderTest.createCasterKey(casterName);

    assertThat(result.getName()).isEqualTo(casterName.toLowerCase());
  }

  @Test
  public final void createCaster_nullName_throwsNullPointerException() {
    this.installModules();
    final DefaultShoutOutDataStore subjectUnderTest = this.givenASubjectToTest(new HashMap<>());

    final Throwable thrown = catchThrowable(
        () -> subjectUnderTest.createCaster(null, this.testFrameworkRule.arbitraryData().getLong()));

    assertThat(thrown).isInstanceOf(NullPointerException.class)
        .hasMessage(this.testFrameworkRule.getInstance(ExecutionUtils.class)
            .nullMessageForName("casterName"));
  }

  @Test
  public final void createCaster_nullLastShoutOut_throwsNullPointerException() {
    this.installModules();
    final DefaultShoutOutDataStore subjectUnderTest = this.givenASubjectToTest(new HashMap<>());

    final Throwable thrown = catchThrowable(
        () -> subjectUnderTest.createCaster(this.testFrameworkRule.arbitraryData().getString(), null));

    assertThat(thrown).isInstanceOf(NullPointerException.class)
        .hasMessage(this.testFrameworkRule.getInstance(ExecutionUtils.class)
            .nullMessageForName("lastShoutOut"));
  }

  @Test
  public final void createCasterKey_forNameAndLastShoutOut_returnsCasterWithNameAndLastShoutOut() {
    this.installModules();
    final String casterName = this.testFrameworkRule.arbitraryData().getString();
    final Long lastShoutOut = this.testFrameworkRule.arbitraryData().getLong();
    final DefaultShoutOutDataStore subjectUnderTest = this.givenASubjectToTest(new HashMap<>());

    final Caster result = subjectUnderTest.createCaster(casterName, lastShoutOut);

    assertThat(result).isEqualTo(Caster.newBuilder().setName(casterName).setLastShoutOut(lastShoutOut).build());
  }

  @Test
  public final void new_whenCreatedWithDefaultPeriod_schedulesUpdate() {
    this.installModules();

    this.givenASubjectToTest();

    final TestScheduleService scheduleService = this.testFrameworkRule.getInstance(TestScheduleService.class);
    final SoftAssertions softly = new SoftAssertions();
    softly.assertThat(scheduleService.getRunnable()).isNotNull();
    softly.assertThat(scheduleService.getPeriod()).isEqualTo(this.getUpdatePeriodDefault());
    softly.assertThat(scheduleService.getUnit()).isEqualTo(TimeUnit.MINUTES);
    softly.assertThat(scheduleService.getDelay()).isFalse();
    softly.assertAll();
  }

  private Integer getUpdatePeriodDefault() {
    return Integer.parseInt(DefaultShoutOutDataStore.DEFAULT_UPDATE_PERIOD);
  }

  @Test
  public final void new_whenCreatedWithCustomPeriod_schedulesUpdate() {
    this.installModules();
    final Integer customPeriod = this.getUpdatePeriodDefault() + 1;
    final MapConfiguration configuration = this.testFrameworkRule.getInstance(MapConfiguration.class);
    configuration.set(this.getUpdatePeriodKey(), customPeriod.toString());

    this.givenASubjectToTest();

    final TestScheduleService scheduleService = this.testFrameworkRule.getInstance(TestScheduleService.class);
    final SoftAssertions softly = new SoftAssertions();
    softly.assertThat(scheduleService.getRunnable()).isNotNull();
    softly.assertThat(scheduleService.getPeriod()).isEqualTo(customPeriod);
    softly.assertThat(scheduleService.getUnit()).isEqualTo(TimeUnit.MINUTES);
    softly.assertThat(scheduleService.getDelay()).isFalse();
    softly.assertAll();
  }

  private ConfigurationKey getUpdatePeriodKey() {
    return ConfigurationKey.of(DefaultShoutOutDataStore.KEY_UPDATE_PERIOD);
  }

  @Test
  public final void new_nullIdForTwitchLogin_failsGracefully() {
    this.installModules();
    final TestScheduleService scheduleService = this.testFrameworkRule.getInstance(TestScheduleService.class);
    scheduleService.setRunnableHandler(Runnable::run);
    final TestTwitchClient twitchClient = this.testFrameworkRule.getInstance(TestTwitchClient.class);
    twitchClient.setGetUserIdImpl(login -> null);

    final Throwable thrown = catchThrowable(this::givenASubjectToTest);

    assertThat(thrown).isNull();
  }

  @Test
  public final void new_withIdForChannel_queriesTwitchForFollowData() {
    this.installModules();
    final TestScheduleService scheduleService = this.testFrameworkRule.getInstance(TestScheduleService.class);
    scheduleService.setRunnableHandler(Runnable::run);
    final TestTwitchClient twitchClient = this.testFrameworkRule.getInstance(TestTwitchClient.class);
    twitchClient.setGetUserIdImpl(login -> TwitchUserId.of(this.testFrameworkRule.arbitraryData().getString()));
    final AtomicBoolean methodWasCalled = new AtomicBoolean(false);
    twitchClient.setGetUserFollowsFromIdImpl(fromId -> {
      methodWasCalled.set(true);
      return null;
    });

    this.givenASubjectToTest();

    assertThat(methodWasCalled).isTrue();
  }

  @Test
  public final void new_callToTwitchThrowsException_failsGracefully() {
    this.installModules();
    final TestScheduleService scheduleService = this.testFrameworkRule.getInstance(TestScheduleService.class);
    scheduleService.setRunnableHandler(Runnable::run);
    final TestTwitchClient twitchClient = this.testFrameworkRule.getInstance(TestTwitchClient.class);
    twitchClient.setGetUserIdImpl(login -> {
      throw new IllegalArgumentException(this.testFrameworkRule.arbitraryData().getString());
    });

    final Throwable thrown = catchThrowable(this::givenASubjectToTest);

    assertThat(thrown).isNull();
  }

  @Test
  public final void new_withFollowsForId_addsCasterToDataSet() {
    this.installModules();
    final TestScheduleService scheduleService = this.testFrameworkRule.getInstance(TestScheduleService.class);
    scheduleService.setRunnableHandler(Runnable::run);
    final TestTwitchClient twitchClient = this.testFrameworkRule.getInstance(TestTwitchClient.class);
    final TwitchUserId twitchUserId = TwitchUserId.of(this.testFrameworkRule.arbitraryData().getString());
    twitchClient.setGetUserIdImpl(login -> twitchUserId);
    final UserFollows userFollows = this.givenAUserFollows(twitchUserId);
    twitchClient.setGetUserFollowsFromIdImpl(fromId -> userFollows);

    final DefaultShoutOutDataStore result = this.givenASubjectToTest();

    assertThat(result.getKeys()).containsExactlyInAnyOrderElementsOf(userFollows.getUserFollowList().stream()
        .map(userFollow -> result.createCasterKey(userFollow.getToName())).collect(Collectors.toSet()));
  }

  private UserFollows givenAUserFollows(final TwitchUserId twitchUserId) {
    return this.givenAUserFollows(twitchUserId, 3, 3);
  }

  private UserFollows givenAUserFollows(final TwitchUserId twitchUserId,
      final int followsBatchSize, final int totalFollows) {
    final UserFollows.Builder builder = UserFollows.newBuilder();

    builder.setTotalFollows(totalFollows);
    builder.setCursor(this.testFrameworkRule.arbitraryData().getString());
    for (int i = 0; i < followsBatchSize; i++) {
      builder.addUserFollow(this.givenAUserFollow(twitchUserId));
    }
    return builder.build();
  }

  private UserFollow givenAUserFollow(final TwitchUserId twitchUserId) {
    final UserFollow.Builder builder = UserFollow.newBuilder();

    builder.setFromId(twitchUserId.value);
    builder.setFromName(twitchUserId.value);
    builder.setToId(this.testFrameworkRule.arbitraryData().getString());
    builder.setToName(this.testFrameworkRule.arbitraryData().getString());
    builder.setFollowedAt(this.testFrameworkRule.arbitraryData().getString());
    return builder.build();
  }

  @Test
  public final void new_withExistingCasterData_doesNotRemoveStillFollowedCaster() {
    this.installModules();
    final TestScheduleService scheduleService = this.testFrameworkRule.getInstance(TestScheduleService.class);
    scheduleService.setRunnableHandler(Runnable::run);
    final TestTwitchClient twitchClient = this.testFrameworkRule.getInstance(TestTwitchClient.class);
    final TwitchUserId twitchUserId = TwitchUserId.of(this.testFrameworkRule.arbitraryData().getString());
    twitchClient.setGetUserIdImpl(login -> twitchUserId);
    final UserFollows userFollows = this.givenAUserFollows(twitchUserId);
    twitchClient.setGetUserFollowsFromIdImpl(fromId -> userFollows);
    final UserFollow userFollow = userFollows.getUserFollow(0);

    final DefaultShoutOutDataStore result = this.givenASubjectToTest(this.givenACasterDataMap(userFollow));

    final CasterKey key = result.createCasterKey(userFollow.getToName());
    assertThat(result.containsKey(key)).isTrue();
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
  public final void new_withExistingCasterData_doesRemoveUnfollowedCaster() {
    this.installModules();
    final TestScheduleService scheduleService = this.testFrameworkRule.getInstance(TestScheduleService.class);
    scheduleService.setRunnableHandler(Runnable::run);
    final TestTwitchClient twitchClient = this.testFrameworkRule.getInstance(TestTwitchClient.class);
    final TwitchUserId twitchUserId = TwitchUserId.of(this.testFrameworkRule.arbitraryData().getString());
    twitchClient.setGetUserIdImpl(login -> twitchUserId);
    final UserFollows userFollows = this.givenAUserFollows(twitchUserId);
    twitchClient.setGetUserFollowsFromIdImpl(fromId -> userFollows);
    final UserFollow userFollow = this.givenAUserFollow(twitchUserId);

    final DefaultShoutOutDataStore result = this.givenASubjectToTest(this.givenACasterDataMap(userFollow));

    final CasterKey key = result.createCasterKey(userFollow.getToName());
    assertThat(result.containsKey(key)).isFalse();
  }

  @Test
  public final void new_pagingData_getsAllData() {
    this.installModules();
    final TestScheduleService scheduleService = this.testFrameworkRule.getInstance(TestScheduleService.class);
    scheduleService.setRunnableHandler(Runnable::run);
    final TestTwitchClient twitchClient = this.testFrameworkRule.getInstance(TestTwitchClient.class);
    final TwitchUserId twitchUserId = TwitchUserId.of(this.testFrameworkRule.arbitraryData().getString());
    twitchClient.setGetUserIdImpl(login -> twitchUserId);
    final UserFollows userFollows1 = this.givenAUserFollows(twitchUserId, 3, 6);
    twitchClient.setGetUserFollowsFromIdImpl(fromId -> userFollows1);
    final UserFollows userFollows2 = this.givenAUserFollows(twitchUserId, 3, 6);
    twitchClient.setGetUserFollowsFromIdImpl((fromId, cursor) -> userFollows2);

    final DefaultShoutOutDataStore result = this.givenASubjectToTest();

    final Set<UserFollow> allUserFollow = Stream.of(userFollows1.getUserFollowList(), userFollows2.getUserFollowList())
        .flatMap(Collection::stream).collect(Collectors.toSet());
    assertThat(result.getKeys()).containsExactlyInAnyOrderElementsOf(allUserFollow.stream()
        .map(userFollow -> result.createCasterKey(userFollow.getToName())).collect(Collectors.toSet()));
  }

  @Test
  public final void new_pagingDataPageTwoEmpty_getsAvailableData() {
    this.installModules();
    final TestScheduleService scheduleService = this.testFrameworkRule.getInstance(TestScheduleService.class);
    scheduleService.setRunnableHandler(Runnable::run);
    final TestTwitchClient twitchClient = this.testFrameworkRule.getInstance(TestTwitchClient.class);
    final TwitchUserId twitchUserId = TwitchUserId.of(this.testFrameworkRule.arbitraryData().getString());
    twitchClient.setGetUserIdImpl(login -> twitchUserId);
    final UserFollows userFollows1 = this.givenAUserFollows(twitchUserId, 3, 6);
    twitchClient.setGetUserFollowsFromIdImpl(fromId -> userFollows1);
    final UserFollows userFollows2 = this.givenAUserFollows(twitchUserId, 0, 6);
    twitchClient.setGetUserFollowsFromIdImpl((fromId, cursor) -> userFollows2);

    final DefaultShoutOutDataStore result = this.givenASubjectToTest();

    assertThat(result.getKeys()).containsExactlyInAnyOrderElementsOf(userFollows1.getUserFollowList().stream()
        .map(userFollow -> result.createCasterKey(userFollow.getToName())).collect(Collectors.toSet()));
  }

}
