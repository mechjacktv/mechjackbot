package tv.mechjack.mechjackbot.command;

import tv.mechjack.mechjackbot.ChatCommand;
import tv.mechjack.mechjackbot.ChatMessageEvent;

public interface RespondingChatCommand extends ChatCommand {

  void sendResponse(ChatMessageEvent chatMessageEvent, Object... args);

  void sendResponse(ChatMessageEvent chatMessageEvent, CommandMessageFormat messageFormat, Object... args);

  void sendUsage(ChatMessageEvent chatMessageEvent);

}
