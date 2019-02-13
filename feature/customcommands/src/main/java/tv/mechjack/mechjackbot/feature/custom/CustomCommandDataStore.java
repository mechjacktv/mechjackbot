package tv.mechjack.mechjackbot.feature.custom;

import tv.mechjack.mechjackbot.api.ChatCommandDescription;
import tv.mechjack.mechjackbot.api.ChatCommandTrigger;
import tv.mechjack.mechjackbot.api.UserRole;
import tv.mechjack.mechjackbot.feature.custom.ProtoMessage.CustomCommand;
import tv.mechjack.mechjackbot.feature.custom.ProtoMessage.CustomCommandKey;
import tv.mechjack.platform.protobuf.MessageStore;

public interface CustomCommandDataStore extends MessageStore<CustomCommandKey, CustomCommand> {

  CustomCommandKey createCustomCommandKey(ChatCommandTrigger trigger);

  CustomCommand createCustomCommand(ChatCommandTrigger trigger, CommandBody commandBody,
      ChatCommandDescription description, UserRole userRole);

}
