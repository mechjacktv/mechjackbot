package com.mechjacktv.mechjackbot.command;

import com.mechjacktv.mechjackbot.*;
import com.mechjacktv.util.TimeUtils;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class CommandUtils {

  private static final String COMMAND_DEFAULT_COOL_DOWN_PERIOD_SECONDS = "command.default_cool_down_period.seconds";
  private static final String COMMAND_DEFAULT_COOL_DOWN_PERIOD_SECONDS_DEFAULT = "5";

  private final AppConfiguration appConfiguration;
  private final String botOwner;
  private final Map<String, Long> commandLastCalled;
  private final Map<String, Pattern> commandTriggerPatterns;
  private final TimeUtils timeUtils;

  @Inject
  public CommandUtils(final AppConfiguration appConfiguration, final ChatBotConfiguration botConfiguration,
                      final TimeUtils timeUtils) {
    this.appConfiguration = appConfiguration;
    this.botOwner = sanitizeViewerName(botConfiguration.getTwitchChannel());
    this.commandLastCalled = new HashMap<>();
    this.commandTriggerPatterns = new HashMap<>();
    this.timeUtils = timeUtils;
  }

  public final String sanitizeViewerName(final String username) {
    String sanitizedUsername = username.trim().toLowerCase();

    if (sanitizedUsername.startsWith("@")) {
      sanitizedUsername = sanitizedUsername.substring(1);
    }
    return sanitizedUsername;
  }

  final boolean isCommandTrigger(final String commandTrigger, final MessageEvent messageEvent) {
    final String message = messageEvent.getMessage();
    final Pattern commandTriggerPattern = getCommandTriggerPattern(commandTrigger);
    final Matcher commandTriggerMatcher = commandTriggerPattern.matcher(message);

    return commandTriggerMatcher.matches();
  }

  private Pattern getCommandTriggerPattern(final String commandTrigger) {
    if (this.commandTriggerPatterns.containsKey(commandTrigger)) {
      return this.commandTriggerPatterns.get(commandTrigger);
    }

    final String commandTriggerRegex = commandTrigger + "(\\s+.+)?";
    final Pattern commandTriggerPattern = Pattern.compile(commandTriggerRegex);

    this.commandTriggerPatterns.put(commandTrigger, commandTriggerPattern);
    return commandTriggerPattern;
  }

  public final boolean isGloballyCooledDown(final Command command) {
    return isGloballyCooledDown(command.getTrigger());
  }

  final boolean isGloballyCooledDown(final String commandTrigger) {
    final Long lastCalled = this.commandLastCalled.get(commandTrigger);
    final Integer commandCoolDownPeriodMs = getCommandCoolDownPeriodMs();
    final Long now = System.currentTimeMillis();

    if (lastCalled == null || now - lastCalled > commandCoolDownPeriodMs) {
      commandLastCalled.put(commandTrigger, now);
      return true;
    }
    return false;
  }

  private Integer getCommandCoolDownPeriodMs() {
    return timeUtils.secondsAsMs(Integer.parseInt(
        this.appConfiguration.get(COMMAND_DEFAULT_COOL_DOWN_PERIOD_SECONDS,
            COMMAND_DEFAULT_COOL_DOWN_PERIOD_SECONDS_DEFAULT)));
  }

  public final boolean isRegularUserViewer(final MessageEvent messageEvent) {
    // TODO implement regular check
    return isPrivilegedViewer(messageEvent);
  }

  public final boolean isPrivilegedViewer(final MessageEvent messageEvent) {
    // TODO implement mod check
    return isChannelOwner(messageEvent);
  }

  public final boolean isChannelOwner(final MessageEvent messageEvent) {
    final ChatUser chatUser = messageEvent.getChatUser();
    final String chatUsername = chatUser.getUsername().toLowerCase();

    return botOwner.equals(chatUsername);
  }

  public final void sendUsage(final MessageEvent messageEvent, final String usage) {
    messageEvent.sendResponse(String.format("@%s, usage: %s", getSanitizedViewerName(messageEvent), usage));
  }

  public final String getSanitizedViewerName(final MessageEvent messageEvent) {
    return sanitizeViewerName(messageEvent.getChatUser().getUsername());
  }

}
