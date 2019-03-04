package tv.mechjack.mechjackbot.feature.shoutout;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.assertj.core.api.Condition;
import org.assertj.core.api.SoftAssertions;
import org.junit.Ignore;
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
import tv.mechjack.mechjackbot.feature.shoutout.ProtoMessage.Caster;
import tv.mechjack.mechjackbot.feature.shoutout.ProtoMessage.CasterKey;
import tv.mechjack.platform.configuration.Configuration;
import tv.mechjack.platform.configuration.ConfigurationKey;
import tv.mechjack.platform.configuration.MapConfiguration;
import tv.mechjack.platform.keyvaluestore.TestKeyValueStoreModule;
import tv.mechjack.platform.protobuf.TestProtobufModule;
import tv.mechjack.platform.utils.TestTimeUtils;
import tv.mechjack.platform.utils.TimeUtils;
import tv.mechjack.platform.utils.scheduleservice.TestScheduleServiceModule;
import tv.mechjack.testframework.fake.FakeBuilder;
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
    this.registerModules();
    final ChatMessageEvent chatMessageEvent = this.testFramework.getInstance(ChatMessageEvent.class);
    final ShoutOutListenerChatCommand subjectUnderTest = this.givenASubjectToTest();

    final boolean result = subjectUnderTest.isTriggered(chatMessageEvent);

    assertThat(result).isFalse();
  }

  protected final void registerModules() {
    super.registerModules();
    this.testFramework.registerModule(new TestChatBotModule());
    this.testFramework.registerModule(new TestKeyValueStoreModule());
    this.testFramework.registerModule(new TestScheduleServiceModule());
    this.testFramework.registerModule(new TestShoutOutCommandModule());
    this.testFramework.registerModule(new TestTwitchClientModule());
    this.testFramework.registerModule(new TestProtobufModule());
  }

  @Override
  protected final ShoutOutListenerChatCommand givenASubjectToTest() {
    return this.givenASubjectToTest(this.testFramework.getInstance(ChatCommandRegistry.class));
  }

  private ShoutOutListenerChatCommand givenASubjectToTest(final ChatCommandRegistry chatCommandRegistry) {
    return new ShoutOutListenerChatCommand(this.testFramework.getInstance(CommandConfigurationBuilder.class),
        chatCommandRegistry, this.testFramework.getInstance(Configuration.class),
        this.testFramework.getInstance(ShoutOutDataStore.class),
        this.testFramework.getInstance(TimeUtils.class));
  }

  @Test
  public final void isTriggered_casterIsNotDue_resultIsFalse() {
    this.registerModules();
    this.testFramework
        .testClock().updateTime(this.getFrequencyDefault(), TimeUnit.HOURS);
    final ChatMessageEvent chatMessageEvent = this.testFramework.getInstance(ChatMessageEvent.class);
    final ShoutOutDataStore shoutOutDataStore = this.testFramework.getInstance(ShoutOutDataStore.class);
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
    this.registerModules();
    this.testFramework
        .testClock().updateTime(this.getFrequencyDefault(), TimeUnit.HOURS, 1);
    final ChatMessageEvent chatMessageEvent = this.testFramework.getInstance(ChatMessageEvent.class);
    final ShoutOutDataStore shoutOutDataStore = this.testFramework.getInstance(ShoutOutDataStore.class);
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
    this.registerModules();
    final int customFrequency = this.getFrequencyDefault() + 1;
    final MapConfiguration configuration = this.testFramework.getInstance(MapConfiguration.class);
    configuration.set(ConfigurationKey.of(ShoutOutListenerChatCommand.KEY_FREQUENCY, ShoutOutListenerChatCommand.class),
        Integer.toString(customFrequency));
    this.testFramework
        .testClock().updateTime(this.getFrequencyDefault(), TimeUnit.HOURS);
    final ChatMessageEvent chatMessageEvent = this.testFramework.getInstance(ChatMessageEvent.class);
    final ShoutOutDataStore shoutOutDataStore = this.testFramework.getInstance(ShoutOutDataStore.class);
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
    this.registerModules();
    final int customFrequency = this.getFrequencyDefault() + 1;
    final MapConfiguration configuration = this.testFramework.getInstance(MapConfiguration.class);
    configuration.set(ConfigurationKey.of(ShoutOutListenerChatCommand.KEY_FREQUENCY, ShoutOutListenerChatCommand.class),
        Integer.toString(customFrequency));
    this.testFramework.testClock().updateTime(customFrequency, TimeUnit.HOURS, 1);
    final ChatMessageEvent chatMessageEvent = this.testFramework.getInstance(ChatMessageEvent.class);
    final ShoutOutDataStore shoutOutDataStore = this.testFramework.getInstance(ShoutOutDataStore.class);
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
    this.registerModules();
    final FakeBuilder<ChatCommandRegistry> fakeBuilder = this.testFramework.fakeBuilder(ChatCommandRegistry.class);
    final TestChatCommand chatCommand = this.testFramework.getInstance(TestChatCommand.class);
    fakeBuilder.forMethod("getCommand", new Class[] { Class.class }).setHandler(invocation -> {
      if (ShoutOutChatCommand.class.equals(invocation.getArgument(0))) {
        return Optional.of(chatCommand);
      }
      return Optional.empty();
    });
    final ChatCommandRegistry fakeChatCommandRegistry = fakeBuilder.build();
    final TestChatMessageEvent messageEvent = this.testFramework.getInstance(TestChatMessageEvent.class);
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
  @Ignore
  // TODO (2019-02-05 mechjack): make this test pass
  public final void handleMessageEvent_isCalled_resultIsUpdatedDataStore() {

    this.registerModules();
    final TestTimeUtils timeUtils = this.testFramework.getInstance(TestTimeUtils.class);
    final Long lastShoutOut = timeUtils.hoursAsMs(this.getFrequencyDefault());
    this.testFramework.testClock().updateTime(lastShoutOut, TimeUnit.MILLISECONDS);
    final TestChatMessageEvent messageEvent = this.testFramework.getInstance(TestChatMessageEvent.class);
    final ShoutOutDataStore shoutOutDataStore = this.testFramework.getInstance(ShoutOutDataStore.class);
    final CasterKey casterKey = shoutOutDataStore.createCasterKey(messageEvent.getChatUser().getTwitchLogin().value);
    final Caster caster = shoutOutDataStore.createCaster(casterKey.getName(), 0L);
    shoutOutDataStore.put(casterKey, caster);
    final FakeBuilder<ChatCommandRegistry> fakeBuilder = this.testFramework.fakeBuilder(ChatCommandRegistry.class);
    final TestChatCommand chatCommand = this.testFramework.getInstance(TestChatCommand.class);
    fakeBuilder.forMethod("getCommand", new Class[] { Class.class }).setHandler(invocation -> {
      if (ShoutOutChatCommand.class.equals(invocation.getArgument(0))) {
        Optional.of(chatCommand);
      }
      return Optional.empty();
    });
    final ChatCommandRegistry fakeChatCommandRegistry = fakeBuilder.build();
    final ShoutOutListenerChatCommand subjectUnderTest = this.givenASubjectToTest(fakeChatCommandRegistry);

    subjectUnderTest.handleMessageEvent(messageEvent);

    assertThat(shoutOutDataStore).is(new Condition<ShoutOutDataStore>() {

      @Override
      public boolean matches(final ShoutOutDataStore actual) {
        if (actual.get(casterKey).isPresent()) {
          final Caster caster = actual.get(casterKey).get();
          final long actualLastShoutOut = caster.getLastShoutOut();

          return actualLastShoutOut == lastShoutOut;
        }
        return false;
      }

    });

  }

}
