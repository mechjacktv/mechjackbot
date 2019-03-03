package tv.mechjack.mechjackbot.feature.autotrigger;

import static tv.mechjack.mechjackbot.api.BaseChatCommand.KEY_DESCRIPTION;
import static tv.mechjack.mechjackbot.api.BaseChatCommand.KEY_TRIGGER;

import java.util.UUID;

import tv.mechjack.mechjackbot.api.BaseChatCommandContractTests;
import tv.mechjack.mechjackbot.api.ChatCommandDescription;
import tv.mechjack.mechjackbot.api.ChatCommandTrigger;
import tv.mechjack.mechjackbot.api.CommandConfigurationBuilder;
import tv.mechjack.platform.configuration.ConfigurationKey;
import tv.mechjack.platform.keyvaluestore.TestKeyValueStoreModule;
import tv.mechjack.protobuf.TestProtobufModule;

public class AutoTriggerListenerChatCommandUnitTests extends
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
        AutoTriggerListenerChatCommand.DEFAULT_DESCRIPTION);
  }

  @Override
  protected ConfigurationKey getDescriptionKey() {
    return ConfigurationKey.of(
        KEY_DESCRIPTION, AutoTriggerListenerChatCommand.class);
  }

  @Override
  protected ChatCommandTrigger getTriggerDefault() {
    return ChatCommandTrigger.of(UUID.randomUUID().toString());
  }

  @Override
  protected ConfigurationKey getTriggerKey() {
    return ConfigurationKey.of(
        KEY_TRIGGER, AutoTriggerListenerChatCommand.class);
  }

  @Override
  protected AutoTriggerListenerChatCommand givenASubjectToTest() {
    return new AutoTriggerListenerChatCommand(this.testFramework.getInstance(
        CommandConfigurationBuilder.class),
        this.testFramework.getInstance(AutoTriggerService.class));
  }

}
