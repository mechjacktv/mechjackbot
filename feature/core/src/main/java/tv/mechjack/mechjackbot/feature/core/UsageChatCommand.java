package tv.mechjack.mechjackbot.feature.core;

import java.util.Optional;

import javax.inject.Inject;

import com.google.common.base.Strings;

import tv.mechjack.mechjackbot.api.BaseChatCommand;
import tv.mechjack.mechjackbot.api.ChatCommand;
import tv.mechjack.mechjackbot.api.ChatCommandRegistry;
import tv.mechjack.mechjackbot.api.ChatCommandTrigger;
import tv.mechjack.mechjackbot.api.ChatCommandUtils;
import tv.mechjack.mechjackbot.api.ChatMessage;
import tv.mechjack.mechjackbot.api.ChatMessageEvent;
import tv.mechjack.mechjackbot.api.CommandConfigurationBuilder;
import tv.mechjack.mechjackbot.api.CommandMessageFormat;
import tv.mechjack.mechjackbot.api.RequiresAccessLevel;
import tv.mechjack.mechjackbot.api.UserRole;
import tv.mechjack.platform.configuration.Configuration;
import tv.mechjack.platform.configuration.ConfigurationKey;

public class UsageChatCommand extends BaseChatCommand {

  public static final String DEFAULT_DESCRIPTION = "Returns the usage for a command.";
  public static final String DEFAULT_MESSAGE_FORMAT = "@$(user), usage: %s %s";
  public static final String DEFAULT_MISSING_MESSAGE_FORMAT = "@$(user), I don't see a command triggered by %s.";
  public static final String DEFAULT_TRIGGER = "!usage";
  public static final String KEY_MISSING_MESSAGE_FORMAT = "missing_message_format";
  public static final String USAGE = "<commandTrigger>";

  private final ChatCommandRegistry chatCommandRegistry;
  private final ChatCommandUtils chatCommandUtils;
  private final Configuration configuration;
  private final CommandMessageFormat missingMessageFormatDefault;
  private final ConfigurationKey missingMessageFormatKey;

  @Inject
  protected UsageChatCommand(final CommandConfigurationBuilder commandConfigurationBuilder,
      final ChatCommandRegistry chatCommandRegistry, final ChatCommandUtils chatCommandUtils,
      final Configuration configuration) {
    super(commandConfigurationBuilder.setTrigger(DEFAULT_TRIGGER)
        .setDescription(DEFAULT_DESCRIPTION)
        .setMessageFormat(DEFAULT_MESSAGE_FORMAT)
        .setUsage(USAGE));
    this.chatCommandRegistry = chatCommandRegistry;
    this.chatCommandUtils = chatCommandUtils;
    this.configuration = configuration;
    this.missingMessageFormatDefault = CommandMessageFormat.of(DEFAULT_MISSING_MESSAGE_FORMAT);
    this.missingMessageFormatKey = ConfigurationKey.of(KEY_MISSING_MESSAGE_FORMAT, this.getClass());
  }

  @Override
  @RequiresAccessLevel(UserRole.VIEWER)
  public void handleMessageEvent(final ChatMessageEvent chatMessageEvent) {
    final ChatMessage chatMessage = this.chatCommandUtils.stripTriggerFromMessage(this, chatMessageEvent);

    if (!Strings.isNullOrEmpty(chatMessage.value)) {
      final ChatCommandTrigger chatCommandTrigger = ChatCommandTrigger.of(chatMessage.value);
      final Optional<ChatCommand> optionalCommand = this.chatCommandRegistry.getCommand(chatCommandTrigger);

      if (optionalCommand.isPresent() && optionalCommand.get().isTriggerable()) {
        final ChatCommand chatCommand = optionalCommand.get();

        this.sendResponse(chatMessageEvent, chatCommand.getTrigger(), chatCommand.getUsage());
      } else {
        this.sendResponse(chatMessageEvent, this.getMissingMessageFormat(), chatCommandTrigger);
      }
    } else {
      this.sendUsage(chatMessageEvent);
    }
  }

  private CommandMessageFormat getMissingMessageFormat() {
    return CommandMessageFormat.of(this.configuration.get(this.missingMessageFormatKey.value,
        this.missingMessageFormatDefault.value));
  }

}
