package tv.mechjack.mechjackbot.feature.custom;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import tv.mechjack.mechjackbot.api.ChatCommand;
import tv.mechjack.mechjackbot.api.ChatCommandDescription;
import tv.mechjack.mechjackbot.api.ChatCommandName;
import tv.mechjack.mechjackbot.api.ChatCommandTrigger;
import tv.mechjack.mechjackbot.api.ChatCommandUsage;
import tv.mechjack.mechjackbot.api.ChatCommandUtils;
import tv.mechjack.mechjackbot.api.ChatMessage;
import tv.mechjack.mechjackbot.api.ChatMessageEvent;
import tv.mechjack.mechjackbot.api.RequiresAccessLevel;
import tv.mechjack.mechjackbot.api.UserRole;

public class CustomChatCommand implements ChatCommand {

  public static final Pattern PATTERN_ARGUMENT = Pattern
      .compile("\\$\\{([\\w\\d-_]+)}");

  private final ChatCommandUtils chatCommandUtils;
  private final ChatCommandTrigger trigger;
  private final ChatCommandDescription description;
  private final ChatCommandUsage usage;
  private final CommandBody commandBody;
  private final List<String> argumentNames;
  private final UserRole userRole;

  protected CustomChatCommand(final ChatCommandUtils chatCommandUtils,
      final ChatCommandTrigger trigger,
      final CommandBody commandBody, final ChatCommandDescription description,
      final UserRole userRole) {
    this.chatCommandUtils = chatCommandUtils;
    this.trigger = trigger;
    this.description = description;
    this.commandBody = commandBody;
    this.argumentNames = this.getArgumentNames(this.commandBody);
    this.usage = this.createUsage(this.commandBody);
    this.userRole = userRole;
  }

  private List<String> getArgumentNames(final CommandBody commandBody) {
    final List<String> arguments = new ArrayList<>();
    final Matcher matcher = PATTERN_ARGUMENT.matcher(commandBody.value);

    while (matcher.find()) {
      final String match = matcher.group(1);

      if (!arguments.contains(match)) {
        arguments.add(match);
      }
    }
    return arguments;
  }

  private ChatCommandUsage createUsage(final CommandBody commandBody) {
    final StringBuilder builder = new StringBuilder();

    for (final String name : this.getArgumentNames(commandBody)) {
      builder.append(String.format(" <%s>", name));
    }
    return ChatCommandUsage.of(builder.toString().trim());
  }

  @Override
  public ChatCommandName getName() {
    return ChatCommandName.of(String
        .format("%s#%s", CustomChatCommand.class.getCanonicalName(),
            this.getTrigger()));
  }

  @Override
  public ChatCommandDescription getDescription() {
    return this.description;
  }

  @Override
  public ChatCommandUsage getUsage() {
    return this.usage;
  }

  @Override
  public boolean isTriggerable() {
    return true;
  }

  @Override
  public ChatCommandTrigger getTrigger() {
    return this.trigger;
  }

  @Override
  public boolean isTriggered(final ChatMessageEvent chatMessageEvent) {
    if (this.chatCommandUtils.isTriggered(this, chatMessageEvent)) {
      return chatMessageEvent.getChatUser().hasAccessLevel(this.userRole);
    }
    return false;
  }

  @Override
  @RequiresAccessLevel(UserRole.VIEWER)
  public void handleMessageEvent(final ChatMessageEvent chatMessageEvent) {
    final List<String> arguments = this.parseMessage(chatMessageEvent);

    if (argumentsProvided(arguments)) {
      final Map<String, String> argumentReplacements = new HashMap<>();

      for (int i = 0; i < this.argumentNames.size(); i++) {
        final String argKey = String.format("${%s}", this.argumentNames.get(i));

        argumentReplacements.put(argKey, arguments.get(i));
      }
      chatMessageEvent.sendResponse(
          getCommandMessage(chatMessageEvent, argumentReplacements));
    } else {
      chatMessageEvent.sendResponse(getUsageMessage(chatMessageEvent));
    }
  }

  private List<String> parseMessage(final ChatMessageEvent chatMessageEvent) {
    final ChatMessage cleanChatMessage = this.chatCommandUtils
        .stripTriggerFromMessage(this, chatMessageEvent);

    return this.chatCommandUtils.parseMessageIntoArguments(
        this, chatMessageEvent, cleanChatMessage);
  }

  private boolean argumentsProvided(final List<String> arguments) {
    if (!argumentsExpected()) {
      return true;
    }

    switch (arguments.size()) {
    case 0:
      return false;
    case 1:
      return !"".equals(arguments.get(0).trim())
          && this.argumentNames.size() == 1;
    default:
      return arguments.size() >= this.argumentNames.size();
    }
  }

  private boolean argumentsExpected() {
    return this.argumentNames.size() > 0;
  }

  private ChatMessage getUsageMessage(final ChatMessageEvent chatMessageEvent) {
    final ChatMessage usageMessage = this.chatCommandUtils
        .createUsageMessage(this, chatMessageEvent);

    return this.chatCommandUtils.replaceChatMessageVariables(
        this, chatMessageEvent, usageMessage);
  }

  private ChatMessage getCommandMessage(final ChatMessageEvent chatMessageEvent,
      final Map<String, String> argReplacements) {
    final ChatMessage commandMessage = ChatMessage.of(
        this.replaceArguments(this.commandBody, argReplacements));

    return this.chatCommandUtils.replaceChatMessageVariables(
        this, chatMessageEvent, commandMessage);
  }

  private String replaceArguments(final CommandBody commandBody,
      final Map<String, String> replacements) {
    String messageBody = commandBody.value;

    for (final String key : replacements.keySet()) {
      messageBody = messageBody.replace(key, replacements.get(key));
    }
    return messageBody;
  }

}
