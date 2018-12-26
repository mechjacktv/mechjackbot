package com.mechjacktv.mechjackbot.command.custom;

import com.mechjacktv.mechjackbot.ChatCommandTrigger;
import com.mechjacktv.mechjackbot.UserRole;

public interface CustomChatCommandService {

  boolean isExistingCustomChatCommand(ChatCommandTrigger trigger);

  void createCustomChatCommand(ChatCommandTrigger trigger, CommandBody commandBody, UserRole userRole);

  void updateCustomChatCommand(ChatCommandTrigger trigger, CommandBody commandBody, UserRole userRole);

  void removeCustomChatCommand(ChatCommandTrigger trigger);

}
