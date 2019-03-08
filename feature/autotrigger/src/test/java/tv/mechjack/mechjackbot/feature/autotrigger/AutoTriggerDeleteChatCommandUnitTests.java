package tv.mechjack.mechjackbot.feature.autotrigger;

import static tv.mechjack.mechjackbot.api.BaseChatCommand.KEY_DESCRIPTION;
import static tv.mechjack.mechjackbot.api.BaseChatCommand.KEY_TRIGGER;

import tv.mechjack.mechjackbot.api.BaseChatCommandContractTests;
import tv.mechjack.mechjackbot.api.ChatCommandDescription;
import tv.mechjack.mechjackbot.api.ChatCommandTrigger;
import tv.mechjack.mechjackbot.api.ChatCommandUtils;
import tv.mechjack.mechjackbot.api.CommandConfigurationBuilder;
import tv.mechjack.platform.configuration.Configuration;
import tv.mechjack.platform.configuration.ConfigurationKey;
import tv.mechjack.platform.keyvaluestore.TestKeyValueStoreModule;
import tv.mechjack.platform.protobuf.TestProtobufModule;

public class AutoTriggerDeleteChatCommandUnitTests extends
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
        AutoTriggerDeleteChatCommand.DEFAULT_DESCRIPTION);
  }

  @Override
  protected ConfigurationKey getDescriptionKey() {
    return ConfigurationKey.of(
        KEY_DESCRIPTION, AutoTriggerDeleteChatCommand.class);
  }

  @Override
  protected ChatCommandTrigger getTriggerDefault() {
    return ChatCommandTrigger.of(
        AutoTriggerDeleteChatCommand.DEFAULT_TRIGGER);
  }

  @Override
  protected ConfigurationKey getTriggerKey() {
    return ConfigurationKey.of(KEY_TRIGGER, AutoTriggerDeleteChatCommand.class);
  }

  @Override
  protected AutoTriggerDeleteChatCommand givenASubjectToTest() {
    return new AutoTriggerDeleteChatCommand(
        this.testFramework.getInstance(CommandConfigurationBuilder.class),
        this.testFramework.getInstance(AutoTriggerService.class),
        this.testFramework.getInstance(ChatCommandUtils.class),
        this.testFramework.getInstance(Configuration.class));
  }

}
