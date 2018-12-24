package com.mechjacktv.mechjackbot.command.custom;

import java.util.Objects;
import java.util.Optional;

import javax.inject.Inject;
import javax.inject.Provider;

import com.mechjacktv.mechjackbot.ChatCommandRegistry;
import com.mechjacktv.mechjackbot.ChatCommandTrigger;
import com.mechjacktv.mechjackbot.UserRole;
import com.mechjacktv.mechjackbot.command.CommandConfigurationBuilder;
import com.mechjacktv.proto.mechjackbot.command.custom.CustomComandDataStoreMessage.CustomCommand;
import com.mechjacktv.proto.mechjackbot.command.custom.CustomComandDataStoreMessage.CustomCommandKey;
import com.mechjacktv.util.ExecutionUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class DefaultCustomChatCommandService implements CustomChatCommandService {

  private static final Logger LOGGER = LoggerFactory.getLogger(DefaultCustomChatCommandService.class);

  private final Provider<CommandConfigurationBuilder> chatCommandConfigurationBuilderProvider;
  private final ChatCommandRegistry chatCommandRegistry;
  private final CustomCommandDataStore customCommandDataStore;
  private final ExecutionUtils executionUtils;

  @Inject
  DefaultCustomChatCommandService(final Provider<CommandConfigurationBuilder> chatCommandConfigurationBuilderProvider,
      final ChatCommandRegistry chatCommandRegistry, final CustomCommandDataStore customCommandDataStore,
      final ExecutionUtils executionUtils) {
    this.chatCommandConfigurationBuilderProvider = chatCommandConfigurationBuilderProvider;
    this.chatCommandRegistry = chatCommandRegistry;
    this.customCommandDataStore = customCommandDataStore;
    this.executionUtils = executionUtils;
    this.registerExistingCustomChatCommands();
  }

  private void registerExistingCustomChatCommands() {
    for (final CustomCommandKey key : this.customCommandDataStore.getKeys()) {
      this.customCommandDataStore.get(key).ifPresent(customCommand -> {
        final ChatCommandTrigger trigger = ChatCommandTrigger.of(customCommand.getTrigger());
        final CommandBody commandBody = CommandBody.of(customCommand.getCommandBody());
        final UserRole userRole = UserRole.valueOf(customCommand.getAccessLevel());

        this.addCommand(trigger, commandBody, userRole);
      });
    }
  }

  private void addCommand(final ChatCommandTrigger trigger, CommandBody commandBody, UserRole userRole) {
    this.chatCommandRegistry.addCommand(new CustomChatCommand(this.chatCommandConfigurationBuilderProvider.get(),
        trigger, commandBody, userRole));
  }

  @Override
  public boolean isExistingCustomChatCommand(final ChatCommandTrigger trigger) {
    Objects.requireNonNull(trigger, this.executionUtils.nullMessageForName("trigger"));

    final CustomCommandKey key = this.customCommandDataStore.createCustomCommandKey(trigger);

    return this.customCommandDataStore.containsKey(key);
  }

  @Override
  public final void createCustomChatCommand(final ChatCommandTrigger trigger, final CommandBody commandBody,
      final UserRole userRole) {
    Objects.requireNonNull(trigger, this.executionUtils.nullMessageForName("trigger"));
    Objects.requireNonNull(commandBody, this.executionUtils.nullMessageForName("commandBody"));

    if (this.chatCommandRegistry.hasCommand(trigger)) {
      throw new IllegalStateException("Cannot create a custom command for an existing trigger, " + trigger);
    }

    final CustomCommandKey key = this.customCommandDataStore.createCustomCommandKey(trigger);
    final UserRole actualUserRole = userRole == null ? UserRole.VIEWER : userRole;

    this.customCommandDataStore.put(key,
        this.customCommandDataStore.createCustomCommand(trigger, commandBody, actualUserRole));
    this.addCommand(trigger, commandBody, actualUserRole);
    LOGGER.info("Created custom command, " + trigger);
  }

  @Override
  public final void updateCustomChatCommand(final ChatCommandTrigger trigger, final CommandBody commandBody,
      final UserRole userRole) {
    Objects.requireNonNull(trigger, this.executionUtils.nullMessageForName("trigger"));

    if (Objects.isNull(userRole) && Objects.isNull(commandBody)) {
      LOGGER.info("No updates needed for custom chat command, " + trigger);
      return;
    }

    final CustomCommandKey key = this.customCommandDataStore.createCustomCommandKey(trigger);
    final Optional<CustomCommand> optionalCustomCommand = this.customCommandDataStore.get(key);

    if (optionalCustomCommand.isPresent()) {
      final CustomCommand customCommand = optionalCustomCommand.get();
      final CommandBody actualCommandBody = commandBody == null ? CommandBody.of(customCommand.getCommandBody()) :
          commandBody;
      final UserRole actualUserRole = userRole == null ? UserRole.valueOf(customCommand.getAccessLevel()) : userRole;

      this.customCommandDataStore.put(key,
          this.customCommandDataStore.createCustomCommand(trigger, actualCommandBody, actualUserRole));
      this.chatCommandRegistry.removeCommand(trigger);
      this.addCommand(trigger, actualCommandBody, actualUserRole);
      LOGGER.info("Updated custom chat command, " + trigger);
    } else {
      throw new IllegalStateException("No existing custom command to update, " + trigger);
    }
  }

  @Override
  public void removeCustomChatCommand(final ChatCommandTrigger trigger) {
    Objects.requireNonNull(trigger, this.executionUtils.nullMessageForName("trigger"));

    final CustomCommandKey key = this.customCommandDataStore.createCustomCommandKey(trigger);
    final Optional<CustomCommand> optionalCustomCommand = this.customCommandDataStore.get(key);

    if (optionalCustomCommand.isPresent()) {
      this.customCommandDataStore.remove(key);
      this.chatCommandRegistry.removeCommand(trigger);
      LOGGER.info("Removed custom chat command, " + trigger);
    } else {
      throw new IllegalStateException("No existing custom command to remove, " + trigger);
    }
  }

}
