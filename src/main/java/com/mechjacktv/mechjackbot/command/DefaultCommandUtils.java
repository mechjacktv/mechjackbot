package com.mechjacktv.mechjackbot.command;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;

import com.mechjacktv.mechjackbot.*;
import com.mechjacktv.util.ExecutionUtils;
import com.mechjacktv.util.TimeUtils;

public final class DefaultCommandUtils implements CommandUtils {

  private final AppConfiguration appConfiguration;
  private final ExecutionUtils executionUtils;
  private final TimeUtils timeUtils;
  private final Map<CommandTrigger, Pattern> commandTriggerPatterns;
  private final Map<CommandTrigger, LastTrigger> commandLastTrigger;
  private final Map<ChatUsername, LastTrigger> viewerLastTrigger;

  @Inject
  public DefaultCommandUtils(final AppConfiguration appConfiguration, final ExecutionUtils executionUtils,
      final TimeUtils timeUtils) {
    this.appConfiguration = appConfiguration;
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
      final RestrictToAccessLevel roles = method.getAnnotation(RestrictToAccessLevel.class);

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
      } else if (this.isCooledDown(command.getTrigger(), messageEvent.getChatUser().getUsername(), now)) {
        this.commandLastTrigger.put(command.getTrigger(), LastTrigger.of(now));
        this.viewerLastTrigger.put(messageEvent.getChatUser().getUsername(), LastTrigger.of(now));
        return true;
      }
      return false;
    }, CommandException.class);
  }

  private boolean isCooledDown(final CommandTrigger commandTrigger, final ChatUsername chatUsername, final long now) {
    return this.isCooledDown(() -> this.commandLastTrigger.get(commandTrigger), this::getCommandCoolDown, now)
        && this.isCooledDown(() -> this.viewerLastTrigger.get(chatUsername), this::getViewerCoolDown, now);
  }

  private boolean isCooledDown(final Supplier<LastTrigger> lastTriggerSupplier,
      final Supplier<CoolDownPeriodMs> coolDownSupplier, final long now) {
    final LastTrigger lastTrigger = lastTriggerSupplier.get();

    return lastTrigger == null || (now - lastTrigger.value > coolDownSupplier.get().value);
  }

  private CoolDownPeriodMs getCommandCoolDown() {
    return CoolDownPeriodMs.of(this.timeUtils.secondsAsMs(Integer.parseInt(
        this.appConfiguration.get(COMMAND_COMMAND_COOL_DOWN_KEY,
            COMMAND_COMMAND_COOL_DOWN_DEFAULT))));
  }

  private CoolDownPeriodMs getViewerCoolDown() {
    return CoolDownPeriodMs.of(this.timeUtils.secondsAsMs(Integer.parseInt(
        this.appConfiguration.get(COMMAND_VIEWER_COOL_DOWN_KEY,
            COMMAND_VIEWER_COOL_DOWN_DEFAULT))));
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
  public final void sendUsage(final Command command, final MessageEvent messageEvent) {
    Objects.requireNonNull(command, this.executionUtils.nullMessageForName("command"));
    Objects.requireNonNull(messageEvent, this.executionUtils.nullMessageForName("messageEvent"));

    final String messageFormat = this.appConfiguration.get(COMMAND_USAGE_MESSAGE_FORMAT_KEY,
        COMMAND_USAGE_MESSAGE_FORMAT_DEFAULT);

    messageEvent.sendResponse(Message.of(String.format(messageFormat,
        this.sanitizeChatUsername(messageEvent.getChatUser().getUsername()), command.getTrigger(),
        command.getUsage())));
  }

  @Override
  public Message messageWithoutTrigger(final Command command, final MessageEvent messageEvent) {
    Objects.requireNonNull(command, this.executionUtils.nullMessageForName("command"));
    Objects.requireNonNull(messageEvent, this.executionUtils.nullMessageForName("messageEvent"));

    final CommandTrigger commandTrigger = command.getTrigger();
    final Message message = messageEvent.getMessage();

    return Message.of(message.value.substring(commandTrigger.value.length()).trim());
  }

  @Override
  public final ChatUsername sanitizeChatUsername(ChatUsername chatUsername) {
    Objects.requireNonNull(chatUsername, this.executionUtils.nullMessageForName("chatUsername"));

    String sanitizedValue = chatUsername.value.trim().toLowerCase();

    if (sanitizedValue.startsWith("@")) {
      sanitizedValue = sanitizedValue.substring(1);
    }
    return ChatUsername.of(sanitizedValue);
  }

}
