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

public final class DefaultChatCommandUtils implements ChatCommandUtils {

  private final Configuration configuration;
  private final ExecutionUtils executionUtils;
  private final TimeUtils timeUtils;
  private final Map<ChatCommandTrigger, Pattern> commandTriggerPatterns;
  private final Map<ChatCommandTrigger, LastTrigger> commandLastTrigger;
  private final Map<TwitchLogin, LastTrigger> viewerLastTrigger;

  @Inject
  DefaultChatCommandUtils(final Configuration configuration, final ExecutionUtils executionUtils,
      final TimeUtils timeUtils) {
    this.configuration = configuration;
    this.executionUtils = executionUtils;
    this.timeUtils = timeUtils;
    this.commandTriggerPatterns = new HashMap<>();
    this.commandLastTrigger = new HashMap<>();
    this.viewerLastTrigger = new HashMap<>();
  }

  @Override
  public boolean hasUserRole(final ChatCommand chatCommand, final ChatMessageEvent chatMessageEvent) {
    Objects.requireNonNull(chatCommand, this.executionUtils.nullMessageForName("chatCommand"));
    Objects.requireNonNull(chatMessageEvent, this.executionUtils.nullMessageForName("chatMessageEvent"));
    return this.executionUtils.softenException(() -> {
      final Method method = chatCommand.getClass().getMethod("handleMessageEvent", ChatMessageEvent.class);
      final RequiresUserRole roles = method.getAnnotation(RequiresUserRole.class);

      if (Objects.isNull(roles)) {
        return true;
      }
      return this.hasUserRole(chatMessageEvent, roles.value());
    }, ChatCommandException.class);
  }

  private boolean hasUserRole(final ChatMessageEvent chatMessageEvent, final UserRole userRole) {
    return chatMessageEvent.getChatUser().hasUserRole(userRole);
  }

  @Override
  public final boolean isCooledDown(final ChatCommand chatCommand, final ChatMessageEvent chatMessageEvent) {
    Objects.requireNonNull(chatCommand, this.executionUtils.nullMessageForName("chatCommand"));
    Objects.requireNonNull(chatMessageEvent, this.executionUtils.nullMessageForName("chatMessageEvent"));
    return this.executionUtils.softenException(() -> {
      final long now = this.timeUtils.currentTime();
      final Method method = chatCommand.getClass().getMethod("handleMessageEvent", ChatMessageEvent.class);
      final NoCoolDown noCoolDown = method.getAnnotation(NoCoolDown.class);

      if (Objects.nonNull(noCoolDown)
          || this.hasUserRole(chatMessageEvent, UserRole.MODERATOR)) {
        return true;
      } else if (this.isCooledDown(chatCommand.getTrigger(), chatMessageEvent.getChatUser().getTwitchLogin(), now)) {
        this.commandLastTrigger.put(chatCommand.getTrigger(), LastTrigger.of(now));
        this.viewerLastTrigger.put(chatMessageEvent.getChatUser().getTwitchLogin(), LastTrigger.of(now));
        return true;
      }
      return false;
    }, ChatCommandException.class);
  }

  private boolean isCooledDown(final ChatCommandTrigger chatCommandTrigger, final TwitchLogin twitchLog,
      final long now) {
    return this.isCooledDown(() -> this.commandLastTrigger.get(chatCommandTrigger), this::getCommandCoolDown, now)
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
  public final boolean isTriggered(final ChatCommand chatCommand, final ChatMessageEvent chatMessageEvent) {
    Objects.requireNonNull(chatCommand, this.executionUtils.nullMessageForName("chatCommand"));
    Objects.requireNonNull(chatMessageEvent, this.executionUtils.nullMessageForName("chatMessageEvent"));

    final ChatMessage chatMessage = chatMessageEvent.getChatMessage();
    final Pattern commandTriggerPattern = this.getCommandTriggerPattern(chatCommand.getTrigger());
    final Matcher commandTriggerMatcher = commandTriggerPattern.matcher(chatMessage.value);

    return commandTriggerMatcher.matches();
  }

  private Pattern getCommandTriggerPattern(final ChatCommandTrigger chatCommandTrigger) {
    if (this.commandTriggerPatterns.containsKey(chatCommandTrigger)) {
      // can't test because fully encapsulated
      return this.commandTriggerPatterns.get(chatCommandTrigger);
    }

    final String commandTriggerRegex = chatCommandTrigger + "(\\s+.+)?";
    final Pattern commandTriggerPattern = Pattern.compile(commandTriggerRegex, Pattern.CASE_INSENSITIVE);

    this.commandTriggerPatterns.put(chatCommandTrigger, commandTriggerPattern);
    return commandTriggerPattern;
  }

  @Override
  public final ChatMessage createUsageMessage(final ChatCommand chatCommand, final ChatMessageEvent chatMessageEvent) {
    Objects.requireNonNull(chatCommand, this.executionUtils.nullMessageForName("chatCommand"));
    Objects.requireNonNull(chatMessageEvent, this.executionUtils.nullMessageForName("chatMessageEvent"));

    final String messageFormat = this.configuration.get(KEY_USAGE_MESSAGE_FORMAT,
        DEFAULT_USAGE_MESSAGE_FORMAT);

    return ChatMessage.of(String.format(messageFormat, chatMessageEvent.getChatUser().getTwitchLogin(),
        chatCommand.getTrigger(), chatCommand.getUsage()));
  }

  @Override
  public ChatMessage stripTriggerFromMessage(final ChatCommand chatCommand, final ChatMessageEvent chatMessageEvent) {
    Objects.requireNonNull(chatCommand, this.executionUtils.nullMessageForName("chatCommand"));
    Objects.requireNonNull(chatMessageEvent, this.executionUtils.nullMessageForName("chatMessageEvent"));

    final ChatCommandTrigger chatCommandTrigger = chatCommand.getTrigger();
    final ChatMessage chatMessage = chatMessageEvent.getChatMessage();

    return ChatMessage.of(chatMessage.value.substring(chatCommandTrigger.value.length()).trim());
  }

}
