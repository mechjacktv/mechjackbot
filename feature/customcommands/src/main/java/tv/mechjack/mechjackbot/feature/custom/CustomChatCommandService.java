package tv.mechjack.mechjackbot.feature.custom;

import tv.mechjack.mechjackbot.api.ChatCommandDescription;
import tv.mechjack.mechjackbot.api.ChatCommandTrigger;
import tv.mechjack.mechjackbot.api.UserRole;

public interface CustomChatCommandService {

  boolean isExistingCustomChatCommand(ChatCommandTrigger trigger);

  void createCustomChatCommand(ChatCommandTrigger trigger, CommandBody commandBody,
      ChatCommandDescription description, UserRole userRole);

  void updateCustomChatCommand(ChatCommandTrigger trigger, CommandBody commandBody, ChatCommandDescription description,
      UserRole userRole);

  void removeCustomChatCommand(ChatCommandTrigger trigger);

}
