package tv.mechjack.mechjackbot.api;

public interface RespondingChatCommand extends ChatCommand {

  void sendResponse(ChatMessageEvent chatMessageEvent, Object... args);

  void sendResponse(ChatMessageEvent chatMessageEvent, CommandMessageFormat messageFormat, Object... args);

  void sendRawResponse(ChatMessageEvent chatMessageEvent, Object... args);

  void sendRawResponse(ChatMessageEvent chatMessageEvent, CommandMessageFormat messageFormat, Object... args);

  void sendUsage(ChatMessageEvent chatMessageEvent);

}
