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

  public static final Pattern PATTERN_ARGUMENT = Pattern.compile("\\$\\{([\\w\\d-_]+)}");

  private final ChatCommandUtils chatCommandUtils;
  private final ChatCommandTrigger trigger;
  private final ChatCommandDescription description;
  private final ChatCommandUsage usage;
  private final CommandBody commandBody;
  private final List<String> argumentNames;
  private final UserRole userRole;

  protected CustomChatCommand(final ChatCommandUtils chatCommandUtils, final ChatCommandTrigger trigger,
      final CommandBody commandBody, final ChatCommandDescription description, final UserRole userRole) {
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
    return ChatCommandName.of(String.format("%s#%s", CustomChatCommand.class.getCanonicalName(), this.getTrigger()));
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
    final ChatMessage cleanChatMessage = this.chatCommandUtils.stripTriggerFromMessage(this, chatMessageEvent);
    final String[] arguments = cleanChatMessage.value.split("\\s+");
    final Map<String, String> argReplacements = new HashMap<>();

    if (this.argumentNames.size() > 0 && ("".equals(cleanChatMessage.value.trim())
        || this.argumentNames.size() > arguments.length)) {
      final ChatMessage usageMessage = this.chatCommandUtils.createUsageMessage(this, chatMessageEvent);

      chatMessageEvent.sendResponse(this.chatCommandUtils.replaceChatMessageVariables(this, chatMessageEvent,
          usageMessage));
      return;
    }
    for (int i = 0; i < this.argumentNames.size(); i++) {
      argReplacements.put(String.format("${%s}", this.argumentNames.get(i)), arguments[i]);
    }
    chatMessageEvent.sendResponse(this.chatCommandUtils.replaceChatMessageVariables(this, chatMessageEvent,
        ChatMessage.of(this.replaceArguments(this.commandBody, argReplacements))));
  }

  private String replaceArguments(final CommandBody commandBody, final Map<String, String> replacements) {
    String messageBody = commandBody.value;

    for (final String key : replacements.keySet()) {
      messageBody = messageBody.replace(key, replacements.get(key));
    }
    return messageBody;
  }

}
