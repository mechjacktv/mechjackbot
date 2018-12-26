package com.mechjacktv.mechjackbot.command.custom;

import com.mechjacktv.keyvaluestore.MessageStore;
import com.mechjacktv.mechjackbot.ChatCommandTrigger;
import com.mechjacktv.mechjackbot.UserRole;
import com.mechjacktv.proto.mechjackbot.command.custom.CustomComandDataStoreMessage.CustomCommand;
import com.mechjacktv.proto.mechjackbot.command.custom.CustomComandDataStoreMessage.CustomCommandKey;

public interface CustomCommandDataStore extends MessageStore<CustomCommandKey, CustomCommand> {

  CustomCommandKey createCustomCommandKey(ChatCommandTrigger trigger);

  CustomCommand createCustomCommand(ChatCommandTrigger trigger, CommandBody commandBody, UserRole userRole);

}
