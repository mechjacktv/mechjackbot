package tv.mechjack.mechjackbot.command.custom;

import java.util.Objects;
import java.util.Optional;

import javax.inject.Inject;

import com.google.common.base.Strings;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tv.mechjack.mechjackbot.api.ChatCommandDescription;
import tv.mechjack.mechjackbot.api.ChatCommandRegistry;
import tv.mechjack.mechjackbot.api.ChatCommandTrigger;
import tv.mechjack.mechjackbot.api.ChatCommandUtils;
import tv.mechjack.mechjackbot.api.UserRole;
import tv.mechjack.mechjackbot.command.custom.ProtoMessage.CustomCommand;
import tv.mechjack.mechjackbot.command.custom.ProtoMessage.CustomCommandKey;
import tv.mechjack.util.ExecutionUtils;

public final class DefaultCustomChatCommandService implements CustomChatCommandService {

  private static final Logger LOGGER = LoggerFactory.getLogger(DefaultCustomChatCommandService.class);

  public static final String DEFAULT_COMMAND_DESCRIPTION = "This is a custom command.";

  private final ChatCommandUtils chatCommandUtils;
  private final ChatCommandRegistry chatCommandRegistry;
  private final CustomCommandDataStore customCommandDataStore;
  private final ExecutionUtils executionUtils;

  @Inject
  DefaultCustomChatCommandService(final ChatCommandUtils chatCommandUtils,
      final ChatCommandRegistry chatCommandRegistry, final CustomCommandDataStore customCommandDataStore,
      final ExecutionUtils executionUtils) {
    this.chatCommandUtils = chatCommandUtils;
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
        final ChatCommandDescription description = ChatCommandDescription.of(this.getCustomDescription(customCommand));
        final UserRole userRole = UserRole.valueOf(customCommand.getAccessLevel());

        this.addCommand(trigger, commandBody, description, userRole);
      });
    }
  }

  private String getCustomDescription(final CustomCommand customCommand) {
    final String configuredDescription = customCommand.getDescription();

    if (Strings.isNullOrEmpty(configuredDescription)) {
      return DEFAULT_COMMAND_DESCRIPTION;
    }
    return configuredDescription;
  }

  private void addCommand(final ChatCommandTrigger trigger, final CommandBody commandBody,
      final ChatCommandDescription description, final UserRole userRole) {
    this.chatCommandRegistry.addCommand(new CustomChatCommand(this.chatCommandUtils, trigger, commandBody,
        description, userRole));
  }

  @Override
  public boolean isExistingCustomChatCommand(final ChatCommandTrigger trigger) {
    Objects.requireNonNull(trigger, this.executionUtils.nullMessageForName("trigger"));

    final CustomCommandKey key = this.customCommandDataStore.createCustomCommandKey(trigger);

    return this.customCommandDataStore.containsKey(key);
  }

  @Override
  public final void createCustomChatCommand(final ChatCommandTrigger trigger, final CommandBody commandBody,
      final ChatCommandDescription description, final UserRole userRole) {
    Objects.requireNonNull(trigger, this.executionUtils.nullMessageForName("trigger"));
    Objects.requireNonNull(commandBody, this.executionUtils.nullMessageForName("commandBody"));

    if (this.chatCommandRegistry.hasCommand(trigger)) {
      throw new IllegalStateException("Cannot create a custom command for an existing trigger, " + trigger);
    }

    final CustomCommandKey key = this.customCommandDataStore.createCustomCommandKey(trigger);
    final UserRole actualUserRole = userRole == null ? UserRole.VIEWER : userRole;
    final ChatCommandDescription actualDescription = description == null
        ? ChatCommandDescription.of(DEFAULT_COMMAND_DESCRIPTION)
        : description;

    this.customCommandDataStore.put(key,
        this.customCommandDataStore.createCustomCommand(trigger, commandBody, actualDescription, actualUserRole));
    this.addCommand(trigger, commandBody, actualDescription, actualUserRole);
    LOGGER.info("Created custom command, " + trigger);
  }

  @Override
  public final void updateCustomChatCommand(final ChatCommandTrigger trigger, final CommandBody commandBody,
      final ChatCommandDescription description, final UserRole userRole) {
    Objects.requireNonNull(trigger, this.executionUtils.nullMessageForName("trigger"));

    if (Objects.isNull(description) && Objects.isNull(userRole) && Objects.isNull(commandBody)) {
      LOGGER.debug("No updates needed for custom chat command, " + trigger);
      return;
    }

    final CustomCommandKey key = this.customCommandDataStore.createCustomCommandKey(trigger);
    final Optional<CustomCommand> optionalCustomCommand = this.customCommandDataStore.get(key);

    if (optionalCustomCommand.isPresent()) {
      final CustomCommand customCommand = optionalCustomCommand.get();
      final CommandBody actualCommandBody = commandBody == null ? CommandBody.of(customCommand.getCommandBody())
          : commandBody;
      final ChatCommandDescription actualDescription = description == null
          ? ChatCommandDescription.of(customCommand.getDescription())
          : description;
      final UserRole actualUserRole = userRole == null ? UserRole.valueOf(customCommand.getAccessLevel()) : userRole;

      this.customCommandDataStore.put(key,
          this.customCommandDataStore.createCustomCommand(trigger, actualCommandBody,
              actualDescription, actualUserRole));
      this.chatCommandRegistry.removeCommand(trigger);
      this.addCommand(trigger, actualCommandBody, actualDescription, actualUserRole);
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
