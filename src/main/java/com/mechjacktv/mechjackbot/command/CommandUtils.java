package com.mechjacktv.mechjackbot.command;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;

import com.mechjacktv.mechjackbot.*;
import com.mechjacktv.util.TimeUtils;

public final class CommandUtils {

  private static final String COMMAND_COOL_DOWN_PERIOD_SECONDS_KEY = "command.cool_down_period.seconds";
  private static final String COMMAND_COOL_DOWN_PERIOD_SECONDS_DEFAULT = "5";
  private static final String COMMAND_USAGE_MESSAGE_FORMAT_KEY = "command.usage.message_format";
  private static final String COMMAND_USAGE_MESSAGE_FORMAT_DEFAULT = "@%s, usage: %s";

  private final AppConfiguration appConfiguration;
  private final ChatUsername botOwner;
  private final Map<CommandTrigger, CommandLastCalled> commandLastCalled;
  private final Map<CommandTrigger, Pattern> commandTriggerPatterns;
  private final TimeUtils timeUtils;

  @Inject
  public CommandUtils(final AppConfiguration appConfiguration, final ChatBotConfiguration botConfiguration,
      final TimeUtils timeUtils) {
    this.appConfiguration = appConfiguration;
    this.botOwner = this.sanitizeViewerName(ChatUsername.of(botConfiguration.getTwitchChannel().value));
    this.commandLastCalled = new HashMap<>();
    this.commandTriggerPatterns = new HashMap<>();
    this.timeUtils = timeUtils;
  }

  private ChatUsername sanitizeViewerName(final ChatUsername username) {
    String sanitizedUsername = username.value.trim().toLowerCase();

    if (sanitizedUsername.startsWith("@")) {
      sanitizedUsername = sanitizedUsername.substring(1);
    }
    return ChatUsername.of(sanitizedUsername);
  }

  final boolean isCommandTrigger(final CommandTrigger commandTrigger, final MessageEvent messageEvent) {
    final Message message = messageEvent.getMessage();
    final Pattern commandTriggerPattern = this.getCommandTriggerPattern(commandTrigger);
    final Matcher commandTriggerMatcher = commandTriggerPattern.matcher(message.value);

    return commandTriggerMatcher.matches();
  }

  private Pattern getCommandTriggerPattern(final CommandTrigger commandTrigger) {
    if (this.commandTriggerPatterns.containsKey(commandTrigger)) {
      return this.commandTriggerPatterns.get(commandTrigger);
    }

    final String commandTriggerRegex = commandTrigger + "(\\s+.+)?";
    final Pattern commandTriggerPattern = Pattern.compile(commandTriggerRegex);

    this.commandTriggerPatterns.put(commandTrigger, commandTriggerPattern);
    return commandTriggerPattern;
  }

  public final boolean isGloballyCooledDown(final Command command) {
    return this.isGloballyCooledDown(command.getTrigger());
  }

  private boolean isGloballyCooledDown(final CommandTrigger commandTrigger) {
    final CommandLastCalled commandLastCalled = this.commandLastCalled.get(commandTrigger);
    final CommandCoolDownPeriodMs commandCoolDownPeriodMs = this.getCommandCoolDownPeriodMs();
    final Long now = System.currentTimeMillis();

    if (commandLastCalled == null || now - commandLastCalled.value > commandCoolDownPeriodMs.value) {
      this.commandLastCalled.put(commandTrigger, CommandLastCalled.of(now));
      return true;
    }
    return false;
  }

  private CommandCoolDownPeriodMs getCommandCoolDownPeriodMs() {
    return CommandCoolDownPeriodMs.of(this.timeUtils.secondsAsMs(Integer.parseInt(
        this.appConfiguration.get(COMMAND_COOL_DOWN_PERIOD_SECONDS_KEY,
            COMMAND_COOL_DOWN_PERIOD_SECONDS_DEFAULT))));
  }

  public final boolean isRegularUserViewer(final MessageEvent messageEvent) {
    return this.isPrivilegedViewer(messageEvent);
  }

  public final boolean isPrivilegedViewer(final MessageEvent messageEvent) {
    return this.isChannelOwner(messageEvent);
  }

  public final boolean isChannelOwner(final MessageEvent messageEvent) {
    final ChatUser chatUser = messageEvent.getChatUser();
    final ChatUsername chatUsername = this.sanitizeViewerName(chatUser.getUsername());

    return this.botOwner.equals(chatUsername);
  }

  final void sendUsage(final MessageEvent messageEvent, final CommandUsage usage) {
    final String messageFormat = this.appConfiguration.get(COMMAND_USAGE_MESSAGE_FORMAT_KEY,
        COMMAND_USAGE_MESSAGE_FORMAT_DEFAULT);

    messageEvent.sendResponse(Message.of(String.format(messageFormat,
        this.getSanitizedViewerName(messageEvent), usage)));
  }

  public final ChatUsername getSanitizedViewerName(final MessageEvent messageEvent) {
    return this.sanitizeViewerName(messageEvent.getChatUser().getUsername());
  }

}
