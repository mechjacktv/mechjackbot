package com.mechjacktv.mechjackbot.command;

import com.mechjacktv.mechjackbot.ChatCommand;
import com.mechjacktv.mechjackbot.ChatMessageEvent;

public interface RespondingChatCommand extends ChatCommand {

  void sendResponse(ChatMessageEvent chatMessageEvent, Object... args);

  void sendResponse(ChatMessageEvent chatMessageEvent, CommandMessageFormat messageFormat, Object... args);

  void sendUsage(ChatMessageEvent chatMessageEvent);

}
