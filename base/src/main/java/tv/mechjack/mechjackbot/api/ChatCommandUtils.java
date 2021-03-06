package tv.mechjack.mechjackbot.api;

import java.util.List;

public interface ChatCommandUtils {

  String KEY_COMMAND_COOL_DOWN = "command.command_cool_down.seconds";
  String DEFAULT_COMMAND_COOL_DOWN = "5";

  String KEY_USER_COOL_DOWN = "command.viewer_cool_down.seconds";
  String DEFAULT_USER_COOL_DOWN = "15";

  String KEY_USAGE_MESSAGE_FORMAT = "command.usage_message_format";
  String DEFAULT_USAGE_MESSAGE_FORMAT = "@$(user), usage: $(trigger) %s";

  boolean hasAccessLevel(ChatCommand chatCommand,
      ChatMessageEvent chatMessageEvent);

  boolean isCooledDown(ChatCommand chatCommand,
      ChatMessageEvent chatMessageEvent);

  boolean isTriggered(ChatCommand chatCommand,
      ChatMessageEvent chatMessageEvent);

  ChatMessage createUsageMessage(ChatCommand chatCommand,
      ChatMessageEvent chatMessageEvent);

  List<String> parseMessageIntoArguments(ChatCommand chatCommand,
      ChatMessageEvent chatMessageEvent,
      ChatMessage chatMessage);

  ChatMessage replaceChatMessageVariables(ChatCommand chatCommand,
      ChatMessageEvent chatMessageEvent,
      ChatMessage responseChatMessage);

  ChatMessage stripTriggerFromMessage(ChatCommand chatCommand,
      ChatMessageEvent chatMessageEvent);

}
