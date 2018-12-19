package com.mechjacktv.mechjackbot.command.custom;

import com.mechjacktv.keyvaluestore.MessageStore;
import com.mechjacktv.mechjackbot.AccessLevel;
import com.mechjacktv.mechjackbot.CommandTrigger;
import com.mechjacktv.proto.mechjackbot.command.custom.CustomComandDataStoreMessage.CustomCommand;
import com.mechjacktv.proto.mechjackbot.command.custom.CustomComandDataStoreMessage.CustomCommandKey;

public interface CustomCommandDataStore extends MessageStore<CustomCommandKey, CustomCommand> {

  CustomCommandKey createCustomCommandKey(CommandTrigger trigger);

  CustomCommand createCustomCommand(CommandTrigger trigger, CommandBody commandBody, AccessLevel accessLevel);

}
