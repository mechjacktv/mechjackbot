package tv.mechjack.mechjackbot.command.custom;

import tv.mechjack.mechjackbot.api.ChatCommandTrigger;
import tv.mechjack.mechjackbot.api.UserRole;

public interface CustomChatCommandService {

  boolean isExistingCustomChatCommand(ChatCommandTrigger trigger);

  void createCustomChatCommand(ChatCommandTrigger trigger, CommandBody commandBody, UserRole userRole);

  void updateCustomChatCommand(ChatCommandTrigger trigger, CommandBody commandBody, UserRole userRole);

  void removeCustomChatCommand(ChatCommandTrigger trigger);

}
