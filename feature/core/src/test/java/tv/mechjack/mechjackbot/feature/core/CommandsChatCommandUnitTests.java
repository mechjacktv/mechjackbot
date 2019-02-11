package tv.mechjack.mechjackbot.feature.core;

import static org.assertj.core.api.Assertions.assertThat;
import static tv.mechjack.mechjackbot.feature.core.CommandsChatCommand.KEY_DESCRIPTION;
import static tv.mechjack.mechjackbot.feature.core.CommandsChatCommand.KEY_MESSAGE_FORMAT;
import static tv.mechjack.mechjackbot.feature.core.CommandsChatCommand.KEY_TRIGGER;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.junit.Test;

import tv.mechjack.mechjackbot.api.BaseChatCommandContractTests;
import tv.mechjack.mechjackbot.api.ChatCommand;
import tv.mechjack.mechjackbot.api.ChatCommandDescription;
import tv.mechjack.mechjackbot.api.ChatCommandRegistry;
import tv.mechjack.mechjackbot.api.ChatCommandTrigger;
import tv.mechjack.mechjackbot.api.ChatMessage;
import tv.mechjack.mechjackbot.api.CommandConfigurationBuilder;
import tv.mechjack.mechjackbot.api.CommandMessageFormat;
import tv.mechjack.mechjackbot.api.TestChatCommand;
import tv.mechjack.mechjackbot.api.TestChatMessageEvent;
import tv.mechjack.mechjackbot.api.TestCommandConfigurationBuilder;
import tv.mechjack.platform.configuration.ConfigurationKey;
import tv.mechjack.platform.configuration.MapConfiguration;

public class CommandsChatCommandUnitTests extends BaseChatCommandContractTests {

  protected final CommandsChatCommand givenASubjectToTest() {
    return new CommandsChatCommand(this.testFrameworkRule.getInstance(CommandConfigurationBuilder.class),
        this.testFrameworkRule.getInstance(ChatCommandRegistry.class));
  }

  @Override
  protected ChatCommandDescription getDescriptionDefault() {
    return ChatCommandDescription.of(CommandsChatCommand.DEFAULT_DESCRIPTION);
  }

  @Override
  protected ConfigurationKey getDescriptionKey() {
    return ConfigurationKey.of(KEY_DESCRIPTION, CommandsChatCommand.class);
  }

  @Override
  protected ConfigurationKey getTriggerKey() {
    return ConfigurationKey.of(KEY_TRIGGER, CommandsChatCommand.class);
  }

  @Override
  protected ChatCommandTrigger getTriggerDefault() {
    return ChatCommandTrigger.of(CommandsChatCommand.DEFAULT_TRIGGER);
  }

  private CommandMessageFormat getMessageFormatDefault() {
    return CommandMessageFormat.of(CommandsChatCommand.DEFAULT_MESSAGE_FORMAT);
  }

  private ConfigurationKey getMessageFormatKey() {
    return ConfigurationKey.of(KEY_MESSAGE_FORMAT, CommandsChatCommand.class);
  }

  private Set<ChatCommand> givenASetOfCommands() {
    final Set<ChatCommand> chatCommands = new HashSet<>();

    for (int i = 0; i < 3; i++) {
      chatCommands.add(new TestChatCommand(
          new TestCommandConfigurationBuilder(this.testFrameworkRule.getInstance(CommandConfigurationBuilder.class))
              .setDefaultTrigger(UUID.randomUUID().toString())));
    }
    return chatCommands;
  }

  @Test
  public final void handleMessageEvent_noMessageFormatConfigured_resultIsDefaultMessage() {
    this.installModules();
    final Set<ChatCommand> chatCommands = this.givenASetOfCommands();
    final ChatCommandRegistry chatCommandRegistry = this.testFrameworkRule.getInstance(ChatCommandRegistry.class);
    chatCommands.forEach(chatCommandRegistry::addCommand);
    final TestChatMessageEvent messageEvent = this.testFrameworkRule.getInstance(TestChatMessageEvent.class);
    final CommandsChatCommand subjectUnderTest = this.givenASubjectToTest();

    subjectUnderTest.handleMessageEvent(messageEvent);
    final ChatMessage result = messageEvent.getResponseChatMessage();

    assertThat(result).isEqualTo(ChatMessage.of(String.format(this.getMessageFormatDefault().value,
        chatCommands.stream().map(command -> command.getTrigger().value).sorted().collect(Collectors.joining(" ")))));
  }

  @Test
  public final void handleMessageEvent_customMessageFormatConfigured_resultIsCustomMessage() {
    this.installModules();
    final String customMessageFormat = this.testFrameworkRule.arbitraryData().getString() + ": %s";
    final MapConfiguration configuration = this.testFrameworkRule.getInstance(MapConfiguration.class);
    configuration.set(this.getMessageFormatKey(), customMessageFormat);
    final Set<ChatCommand> chatCommands = this.givenASetOfCommands();
    final ChatCommandRegistry chatCommandRegistry = this.testFrameworkRule.getInstance(ChatCommandRegistry.class);
    chatCommands.forEach(chatCommandRegistry::addCommand);
    final TestChatMessageEvent messageEvent = this.testFrameworkRule.getInstance(TestChatMessageEvent.class);
    final CommandsChatCommand subjectUnderTest = this.givenASubjectToTest();

    subjectUnderTest.handleMessageEvent(messageEvent);
    final ChatMessage result = messageEvent.getResponseChatMessage();

    assertThat(result).isEqualTo(ChatMessage.of(String.format(customMessageFormat,
        chatCommands.stream()
            .map(command -> command.getTrigger().value)
            .sorted()
            .collect(Collectors.joining(" ")))));
  }

  @Test
  public final void handleMessageEvent_noMessageFormatConfiguredWithNonTriggerableCommand_resultIsDefaultMessageWithoutNonTriggerableCommand() {
    this.installModules();
    final TestChatCommand nonTriggerableCommand = this.testFrameworkRule.getInstance(TestChatCommand.class);
    nonTriggerableCommand.setTriggerable(false);
    final Set<ChatCommand> chatCommands = this.givenASetOfCommands();
    final ChatCommandRegistry chatCommandRegistry = this.testFrameworkRule.getInstance(ChatCommandRegistry.class);
    chatCommandRegistry.addCommand(nonTriggerableCommand);
    chatCommands.forEach(chatCommandRegistry::addCommand);
    final TestChatMessageEvent messageEvent = this.testFrameworkRule.getInstance(TestChatMessageEvent.class);
    final CommandsChatCommand subjectUnderTest = this.givenASubjectToTest();

    subjectUnderTest.handleMessageEvent(messageEvent);
    final ChatMessage result = messageEvent.getResponseChatMessage();

    assertThat(result).isEqualTo(ChatMessage.of(String.format(this.getMessageFormatDefault().value,
        chatCommands.stream()
            .map(command -> command.getTrigger().value)
            .sorted()
            .collect(Collectors.joining(" ")))));
  }

}
