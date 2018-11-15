package com.mechjacktv.mechjackbot.command;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;

import com.mechjacktv.mechjackbot.*;
import com.mechjacktv.util.TimeUtils;

public final class DefaultCommandUtils implements CommandUtils {

  private static final String COMMAND_COMMAND_COOL_DOWN_KEY = "command.command_cool_down.seconds";
  private static final String COMMAND_COMMAND_COOL_DOWN_DEFAULT = "5";
  private static final String COMMAND_VIEWER_COOL_DOWN_KEY = "command.viewer_cool_down.seconds";
  private static final String COMMAND_VIEWER_COOL_DOWN_DEFAULT = "15";
  private static final String COMMAND_USAGE_MESSAGE_FORMAT_KEY = "command.usage_message_format";
  private static final String COMMAND_USAGE_MESSAGE_FORMAT_DEFAULT = "@%s, usage: %s %s";

  private final AppConfiguration appConfiguration;
  private final TimeUtils timeUtils;
  private final ChatUsername botOwner;
  private final Map<CommandTrigger, Pattern> commandTriggerPatterns;
  private final Map<CommandTrigger, LastTrigger> commandLastTrigger;
  private final Map<ChatUsername, LastTrigger> viewerLastTrigger;

  @Inject
  public DefaultCommandUtils(final AppConfiguration appConfiguration, final ChatBotConfiguration botConfiguration,
      final TimeUtils timeUtils) {
    this.appConfiguration = appConfiguration;
    this.timeUtils = timeUtils;
    this.botOwner = this.sanitizeChatUsername(ChatUsername.of(botConfiguration.getTwitchChannel().value));
    this.commandTriggerPatterns = new HashMap<>();
    this.commandLastTrigger = new HashMap<>();
    this.viewerLastTrigger = new HashMap<>();
  }

  @Override
  public boolean hasRole(final Command command, final MessageEvent messageEvent, final ViewerRole viewerRole) {
    return this.hasRole(command, messageEvent, new ViewerRole[] { viewerRole });
  }

  @Override
  public boolean hasRole(final Command command, final MessageEvent messageEvent, final ViewerRole[] viewerRoles) {
    final ChatUser chatUser = messageEvent.getChatUser();
    final ChatUsername chatUsername = this.sanitizeChatUsername(chatUser.getUsername());

    return this.botOwner.equals(chatUsername);
  }

  @Override
  public final boolean isCooledDown(final Command command, final MessageEvent messageEvent) {
    if (this.hasRole(command, messageEvent, new ViewerRole[] { ViewerRole.OWNER, ViewerRole.MODERATOR })
        || this.isCooledDown(command.getTrigger(), messageEvent.getChatUser().getUsername())) {
      this.commandLastTrigger.put(command.getTrigger(), LastTrigger.of(this.timeUtils.currentTime()));
      this.viewerLastTrigger.put(messageEvent.getChatUser().getUsername(),
          LastTrigger.of(this.timeUtils.currentTime()));
      return true;
    }
    return false;
  }

  private boolean isCooledDown(final CommandTrigger commandTrigger, final ChatUsername chatUsername) {
    return this.isCooledDown(() -> this.commandLastTrigger.get(commandTrigger), this::getCommandCoolDown)
        && this.isCooledDown(() -> this.viewerLastTrigger.get(chatUsername), this::getViewerCoolDown);
  }

  private boolean isCooledDown(final Supplier<LastTrigger> lastTriggerSupplier,
      final Supplier<CoolDownPeriodMs> coolDownSupplier) {
    final LastTrigger lastTrigger = lastTriggerSupplier.get();

    return lastTrigger == null || (this.timeUtils.currentTime() - lastTrigger.value > coolDownSupplier.get().value);
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
    final Message message = messageEvent.getMessage();
    final Pattern commandTriggerPattern = this.getCommandTriggerPattern(command.getTrigger());
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

  @Override
  public final void sendUsage(final Command command, final MessageEvent messageEvent) {
    final String messageFormat = this.appConfiguration.get(COMMAND_USAGE_MESSAGE_FORMAT_KEY,
        COMMAND_USAGE_MESSAGE_FORMAT_DEFAULT);

    messageEvent.sendResponse(Message.of(String.format(messageFormat,
        this.sanitizedChatUsername(command, messageEvent), command.getTrigger(), command.getUsage())));
  }

  @Override
  public Message messageWithoutTrigger(final Command command, final MessageEvent messageEvent) {
    final CommandTrigger commandTrigger = command.getTrigger();
    final Message message = messageEvent.getMessage();

    return Message.of(message.value.substring(commandTrigger.value.length()).trim());
  }

  @Override
  public final ChatUsername sanitizedChatUsername(final Command command, final MessageEvent messageEvent) {
    return this.sanitizeChatUsername(messageEvent.getChatUser().getUsername());
  }

  private ChatUsername sanitizeChatUsername(final ChatUsername username) {
    String sanitizedValue = username.value.trim().toLowerCase();

    if (sanitizedValue.startsWith("@")) {
      sanitizedValue = sanitizedValue.substring(1);
    }
    return ChatUsername.of(sanitizedValue);
  }

}
