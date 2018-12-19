package com.mechjacktv.mechjackbot.command;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;

import com.mechjacktv.configuration.Configuration;
import com.mechjacktv.mechjackbot.*;
import com.mechjacktv.twitchclient.TwitchLogin;
import com.mechjacktv.util.ExecutionUtils;
import com.mechjacktv.util.TimeUtils;

public final class DefaultCommandUtils implements CommandUtils {

  private final Configuration configuration;
  private final ExecutionUtils executionUtils;
  private final TimeUtils timeUtils;
  private final Map<CommandTrigger, Pattern> commandTriggerPatterns;
  private final Map<CommandTrigger, LastTrigger> commandLastTrigger;
  private final Map<TwitchLogin, LastTrigger> viewerLastTrigger;

  @Inject
  DefaultCommandUtils(final Configuration configuration, final ExecutionUtils executionUtils,
      final TimeUtils timeUtils) {
    this.configuration = configuration;
    this.executionUtils = executionUtils;
    this.timeUtils = timeUtils;
    this.commandTriggerPatterns = new HashMap<>();
    this.commandLastTrigger = new HashMap<>();
    this.viewerLastTrigger = new HashMap<>();
  }

  @Override
  public boolean hasAccessLevel(final Command command, final MessageEvent messageEvent) {
    Objects.requireNonNull(command, this.executionUtils.nullMessageForName("command"));
    Objects.requireNonNull(messageEvent, this.executionUtils.nullMessageForName("messageEvent"));
    return this.executionUtils.softenException(() -> {
      final Method method = command.getClass().getMethod("handleMessageEvent", MessageEvent.class);
      final RequiresAccessLevel roles = method.getAnnotation(RequiresAccessLevel.class);

      if (Objects.isNull(roles)) {
        return true;
      }
      return this.hasAccessLevel(messageEvent, roles.value());
    }, CommandException.class);
  }

  private boolean hasAccessLevel(final MessageEvent messageEvent, final AccessLevel accessLevel) {
    return messageEvent.getChatUser().hasAccessLevel(accessLevel);
  }

  @Override
  public final boolean isCooledDown(final Command command, final MessageEvent messageEvent) {
    Objects.requireNonNull(command, this.executionUtils.nullMessageForName("command"));
    Objects.requireNonNull(messageEvent, this.executionUtils.nullMessageForName("messageEvent"));
    return this.executionUtils.softenException(() -> {
      final long now = this.timeUtils.currentTime();
      final Method method = command.getClass().getMethod("handleMessageEvent", MessageEvent.class);
      final NoCoolDown noCoolDown = method.getAnnotation(NoCoolDown.class);

      if (Objects.nonNull(noCoolDown)
          || this.hasAccessLevel(messageEvent, AccessLevel.MODERATOR)) {
        return true;
      } else if (this.isCooledDown(command.getTrigger(), messageEvent.getChatUser().getTwitchLogin(), now)) {
        this.commandLastTrigger.put(command.getTrigger(), LastTrigger.of(now));
        this.viewerLastTrigger.put(messageEvent.getChatUser().getTwitchLogin(), LastTrigger.of(now));
        return true;
      }
      return false;
    }, CommandException.class);
  }

  private boolean isCooledDown(final CommandTrigger commandTrigger, final TwitchLogin twitchLog, final long now) {
    return this.isCooledDown(() -> this.commandLastTrigger.get(commandTrigger), this::getCommandCoolDown, now)
        && this.isCooledDown(() -> this.viewerLastTrigger.get(twitchLog), this::getUserCoolDown, now);
  }

  private boolean isCooledDown(final Supplier<LastTrigger> lastTriggerSupplier,
      final Supplier<CoolDownPeriodMs> coolDownSupplier, final long now) {
    final LastTrigger lastTrigger = lastTriggerSupplier.get();

    return lastTrigger == null || (now - lastTrigger.value > coolDownSupplier.get().value);
  }

  private CoolDownPeriodMs getCommandCoolDown() {
    return CoolDownPeriodMs.of(this.timeUtils.secondsAsMs(Integer.parseInt(
        this.configuration.get(KEY_COMMAND_COOL_DOWN, DEFAULT_COMMAND_COOL_DOWN))));
  }

  private CoolDownPeriodMs getUserCoolDown() {
    return CoolDownPeriodMs.of(this.timeUtils.secondsAsMs(Integer.parseInt(
        this.configuration.get(KEY_USER_COOL_DOWN, DEFAULT_USER_COOL_DOWN))));
  }

  @Override
  public final boolean isTriggered(final Command command, final MessageEvent messageEvent) {
    Objects.requireNonNull(command, this.executionUtils.nullMessageForName("command"));
    Objects.requireNonNull(messageEvent, this.executionUtils.nullMessageForName("messageEvent"));

    final Message message = messageEvent.getMessage();
    final Pattern commandTriggerPattern = this.getCommandTriggerPattern(command.getTrigger());
    final Matcher commandTriggerMatcher = commandTriggerPattern.matcher(message.value);

    return commandTriggerMatcher.matches();
  }

  private Pattern getCommandTriggerPattern(final CommandTrigger commandTrigger) {
    if (this.commandTriggerPatterns.containsKey(commandTrigger)) {
      // can't test because fully encapsulated
      return this.commandTriggerPatterns.get(commandTrigger);
    }

    final String commandTriggerRegex = commandTrigger + "(\\s+.+)?";
    final Pattern commandTriggerPattern = Pattern.compile(commandTriggerRegex, Pattern.CASE_INSENSITIVE);

    this.commandTriggerPatterns.put(commandTrigger, commandTriggerPattern);
    return commandTriggerPattern;
  }

  @Override
  public final Message createUsageMessage(final Command command, final MessageEvent messageEvent) {
    Objects.requireNonNull(command, this.executionUtils.nullMessageForName("command"));
    Objects.requireNonNull(messageEvent, this.executionUtils.nullMessageForName("messageEvent"));

    final String messageFormat = this.configuration.get(KEY_USAGE_MESSAGE_FORMAT,
        DEFAULT_USAGE_MESSAGE_FORMAT);

    return Message.of(String.format(messageFormat, messageEvent.getChatUser().getTwitchLogin(),
        command.getTrigger(), command.getUsage()));
  }

  @Override
  public Message stripTriggerFromMessage(final Command command, final MessageEvent messageEvent) {
    Objects.requireNonNull(command, this.executionUtils.nullMessageForName("command"));
    Objects.requireNonNull(messageEvent, this.executionUtils.nullMessageForName("messageEvent"));

    final CommandTrigger commandTrigger = command.getTrigger();
    final Message message = messageEvent.getMessage();

    return Message.of(message.value.substring(commandTrigger.value.length()).trim());
  }

}
