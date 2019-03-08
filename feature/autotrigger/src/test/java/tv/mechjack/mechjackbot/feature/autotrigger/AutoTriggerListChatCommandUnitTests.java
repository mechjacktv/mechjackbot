package tv.mechjack.mechjackbot.feature.autotrigger;

import static tv.mechjack.mechjackbot.api.BaseChatCommand.KEY_DESCRIPTION;
import static tv.mechjack.mechjackbot.api.BaseChatCommand.KEY_TRIGGER;

import com.google.common.collect.Lists;

import org.assertj.core.api.SoftAssertions;
import org.junit.Test;

import tv.mechjack.mechjackbot.api.BaseChatCommandContractTests;
import tv.mechjack.mechjackbot.api.ChatCommandDescription;
import tv.mechjack.mechjackbot.api.ChatCommandTrigger;
import tv.mechjack.mechjackbot.api.ChatMessage;
import tv.mechjack.mechjackbot.api.CommandConfigurationBuilder;
import tv.mechjack.mechjackbot.api.TestChatMessageEvent;
import tv.mechjack.platform.configuration.ConfigurationKey;
import tv.mechjack.platform.keyvaluestore.TestKeyValueStoreModule;
import tv.mechjack.platform.protobuf.TestProtobufModule;
import tv.mechjack.testframework.ArbitraryData;

public class AutoTriggerListChatCommandUnitTests extends
    BaseChatCommandContractTests {

  @Override
  protected void registerModules() {
    super.registerModules();
    this.testFramework.registerModule(new TestAutoTriggerFeatureModule());
    this.testFramework.registerModule(new TestKeyValueStoreModule());
    this.testFramework.registerModule(new TestProtobufModule());
  }

  @Override
  protected ChatCommandDescription getDescriptionDefault() {
    return ChatCommandDescription.of(
        AutoTriggerListChatCommand.DEFAULT_DESCRIPTION);
  }

  @Override
  protected ConfigurationKey getDescriptionKey() {
    return ConfigurationKey.of(
        KEY_DESCRIPTION, AutoTriggerListChatCommand.class);
  }

  @Override
  protected ChatCommandTrigger getTriggerDefault() {
    return ChatCommandTrigger.of(
        AutoTriggerListChatCommand.DEFAULT_TRIGGER);
  }

  @Override
  protected ConfigurationKey getTriggerKey() {
    return ConfigurationKey.of(KEY_TRIGGER, AutoTriggerListChatCommand.class);
  }

  @Override
  protected AutoTriggerListChatCommand givenASubjectToTest() {
    return new AutoTriggerListChatCommand(this.testFramework.getInstance(
        CommandConfigurationBuilder.class),
        this.testFramework.getInstance(AutoTriggerService.class));
  }

  @Test
  public final void handleMessageEvent_withExistingAutoTriggerLists_resultIsMessageSentWithListNamesIncluded() {
    this.registerModules();
    final AutoTriggerService autoTriggerService = this.testFramework
        .getInstance(AutoTriggerService.class);
    for (int i = 0; i < ArbitraryData.ARBITRARY_COLLECTION_SIZE; i++) {
      this.createAutoTriggerList(autoTriggerService);
    }
    final TestChatMessageEvent messageEvent = this.testFramework
        .getInstance(TestChatMessageEvent.class);
    final AutoTriggerListChatCommand subjectUnderTest = this
        .givenASubjectToTest();

    messageEvent.setChatMessage(ChatMessage.of(
        subjectUnderTest.getTrigger().value));
    subjectUnderTest.handleMessageEvent(messageEvent);

    final String result = messageEvent.getResponseChatMessage().value;
    final SoftAssertions softly = new SoftAssertions();
    for (final ListName listName : autoTriggerService.getAutoTriggerListNames()) {
      softly.assertThat(result).contains(listName.value);
    }
    softly.assertAll();
  }

  private void createAutoTriggerList(
      final AutoTriggerService autoTriggerService) {
    autoTriggerService.createAutoTriggerList(
        ListName.of(this.testFramework.arbitraryData().getString()),
        null, null, null, null,
        Lists.newArrayList(ChatCommandTrigger.of(
            this.testFramework.arbitraryData().getString())));
  }

}
