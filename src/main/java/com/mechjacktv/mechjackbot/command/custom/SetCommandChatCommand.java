package com.mechjacktv.mechjackbot.command.custom;

import javax.inject.Inject;

import picocli.CommandLine;

import com.mechjacktv.configuration.Configuration;
import com.mechjacktv.configuration.ConfigurationKey;
import com.mechjacktv.mechjackbot.ChatCommandUtils;
import com.mechjacktv.mechjackbot.ChatMessage;
import com.mechjacktv.mechjackbot.ChatMessageEvent;
import com.mechjacktv.mechjackbot.command.BaseChatCommand;
import com.mechjacktv.mechjackbot.command.CommandConfigurationBuilder;
import com.mechjacktv.mechjackbot.command.CommandMessageFormat;
import com.mechjacktv.proto.mechjackbot.command.custom.CustomComandDataStoreMessage.CustomCommandKey;

public class SetCommandChatCommand extends BaseChatCommand {

  public static final String DEFAULT_DESCRIPTION = "Set a custom command body and/or access level.";
  public static final String DEFAULT_MESSAGE_FORMAT = "Command set for trigger, %2$s";
  public static final String DEFAULT_BODY_REQUIRED_MESSAGE_FORMAT = "%2$s failed: body required";
  public static final String DEFAULT_TRIGGER = "!setcommand";
  public static final String KEY_BODY_REQUIRED_MESSAGE_FORMAT = "body_required_message_format";
  public static final String USAGE = "<trigger> [(-a|--access-level) "
      + "(owner|moderator|vip|subscriber|follower|everyone)] [<body>]";

  private final ChatCommandUtils commandUtils;
  private final Configuration configuration;
  private final CustomCommandDataStore customCommandDataStore;
  private final CommandMessageFormat bodyRequiredMessageFormatDefault;
  private final ConfigurationKey bodyRequiredMessageFormatKey;

  @Inject
  protected SetCommandChatCommand(
      final CommandConfigurationBuilder commandConfigurationBuilder, final ChatCommandUtils commandUtils,
      final Configuration configuration, final CustomCommandDataStore customCommandDataStore) {
    super(commandConfigurationBuilder.setTrigger(DEFAULT_TRIGGER)
        .setDescription(DEFAULT_DESCRIPTION)
        .setMessageFormat(DEFAULT_MESSAGE_FORMAT)
        .setUsage(USAGE));
    this.commandUtils = commandUtils;
    this.configuration = configuration;
    this.customCommandDataStore = customCommandDataStore;
    this.bodyRequiredMessageFormatDefault = CommandMessageFormat.of(DEFAULT_BODY_REQUIRED_MESSAGE_FORMAT);
    this.bodyRequiredMessageFormatKey = ConfigurationKey.of(KEY_BODY_REQUIRED_MESSAGE_FORMAT, this.getClass());
  }

  @Override
  public void handleMessageEvent(final ChatMessageEvent messageEvent) {
    final ChatMessage rawMessage = this.commandUtils.stripTriggerFromMessage(this, messageEvent);

    if ("".equals(rawMessage.value.trim())) {
      this.sendUsage(messageEvent);
      return;
    }

    final String[] arguments = rawMessage.value.split("\\s+");
    final SetCommandMessage setCommandMessage = new SetCommandMessage();

    try {
      CommandLine.populateCommand(setCommandMessage, arguments);
      setCommandMessage.validate();
    } catch (final RuntimeException e) {
      this.sendUsage(messageEvent);
      return;
    }

    final CustomCommandKey key = this.customCommandDataStore
        .createCustomCommandKey(setCommandMessage.getTrigger());

    if (!this.customCommandDataStore.containsKey(key) && !setCommandMessage.getBody().isPresent()) {
      this.sendResponse(messageEvent, this.getBodyRequiredMessageFormat(), this.getTrigger());
    } else {
      this.sendResponse(messageEvent, setCommandMessage.getTrigger());
    }
  }

  private CommandMessageFormat getBodyRequiredMessageFormat() {
    return CommandMessageFormat.of(this.configuration.get(this.bodyRequiredMessageFormatKey.value,
        this.bodyRequiredMessageFormatDefault.value));
  }

}
