package com.mechjacktv.mechjackbot.command.core;

import static com.mechjacktv.mechjackbot.command.core.CommandsChatCommand.DEFAULT_DESCRIPTION;
import static com.mechjacktv.mechjackbot.command.core.CommandsChatCommand.DEFAULT_TRIGGER;
import static com.mechjacktv.mechjackbot.command.core.CommandsChatCommand.KEY_DESCRIPTION;
import static com.mechjacktv.mechjackbot.command.core.CommandsChatCommand.KEY_MESSAGE_FORMAT;
import static com.mechjacktv.mechjackbot.command.core.CommandsChatCommand.KEY_TRIGGER;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.junit.Test;

import com.mechjacktv.configuration.ConfigurationKey;
import com.mechjacktv.configuration.MapConfiguration;
import com.mechjacktv.mechjackbot.ChatCommand;
import com.mechjacktv.mechjackbot.ChatCommandDescription;
import com.mechjacktv.mechjackbot.ChatCommandRegistry;
import com.mechjacktv.mechjackbot.ChatCommandTrigger;
import com.mechjacktv.mechjackbot.ChatMessage;
import com.mechjacktv.mechjackbot.TestChatCommand;
import com.mechjacktv.mechjackbot.TestChatMessageEvent;
import com.mechjacktv.mechjackbot.TestCommandConfigurationBuilder;
import com.mechjacktv.mechjackbot.command.BaseChatCommandContractTests;
import com.mechjacktv.mechjackbot.command.CommandConfigurationBuilder;
import com.mechjacktv.mechjackbot.command.CommandMessageFormat;

public class CommandsChatCommandUnitTests extends BaseChatCommandContractTests {

  protected final CommandsChatCommand givenASubjectToTest() {
    return new CommandsChatCommand(this.testFrameworkRule.getInstance(CommandConfigurationBuilder.class),
        this.testFrameworkRule.getInstance(ChatCommandRegistry.class));
  }

  @Override
  protected ChatCommandDescription getDescriptionDefault() {
    return ChatCommandDescription.of(DEFAULT_DESCRIPTION);
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
    return ChatCommandTrigger.of(DEFAULT_TRIGGER);
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
    final String customMessageFormat = this.testFrameworkRule.getArbitraryString() + ": %s";
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
