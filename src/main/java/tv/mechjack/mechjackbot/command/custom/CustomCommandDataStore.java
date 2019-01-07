package tv.mechjack.mechjackbot.command.custom;

import tv.mechjack.keyvaluestore.MessageStore;
import tv.mechjack.mechjackbot.ChatCommandTrigger;
import tv.mechjack.mechjackbot.UserRole;
import tv.mechjack.mechjackbot.command.custom.ProtoMessage.CustomCommand;
import tv.mechjack.mechjackbot.command.custom.ProtoMessage.CustomCommandKey;

public interface CustomCommandDataStore extends MessageStore<CustomCommandKey, CustomCommand> {

  CustomCommandKey createCustomCommandKey(ChatCommandTrigger trigger);

  CustomCommand createCustomCommand(ChatCommandTrigger trigger, CommandBody commandBody, UserRole userRole);

}
