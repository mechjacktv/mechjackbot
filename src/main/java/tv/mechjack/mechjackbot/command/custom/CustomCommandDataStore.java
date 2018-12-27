package tv.mechjack.mechjackbot.command.custom;

import tv.mechjack.keyvaluestore.MessageStore;
import tv.mechjack.mechjackbot.ChatCommandTrigger;
import tv.mechjack.mechjackbot.UserRole;
import tv.mechjack.proto.mechjackbot.command.custom.CustomComandDataStoreMessage.CustomCommand;
import tv.mechjack.proto.mechjackbot.command.custom.CustomComandDataStoreMessage.CustomCommandKey;

public interface CustomCommandDataStore extends MessageStore<CustomCommandKey, CustomCommand> {

  CustomCommandKey createCustomCommandKey(ChatCommandTrigger trigger);

  CustomCommand createCustomCommand(ChatCommandTrigger trigger, CommandBody commandBody, UserRole userRole);

}
