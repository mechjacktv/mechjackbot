package tv.mechjack.mechjackbot.feature.autotrigger;

import java.util.List;

import tv.mechjack.mechjackbot.api.ChatCommandTrigger;
import tv.mechjack.mechjackbot.feature.autotrigger.ProtoMessage.AutoTriggerList;
import tv.mechjack.mechjackbot.feature.autotrigger.ProtoMessage.AutoTriggerListKey;
import tv.mechjack.mechjackbot.feature.autotrigger.ProtoMessage.CommandTrigger;
import tv.mechjack.protobuf.MessageStore;

public interface AutoTriggerDataStore
    extends MessageStore<AutoTriggerListKey, AutoTriggerList> {

  AutoTriggerListKey createAutoTriggerListKey(
      ListName name);

  AutoTriggerList createAutoTriggerList(ListName name,
      TimeRequired timeRequired, MessageCount messageCount,
      ChatterCount chatterCount, Order order,
      List<CommandTrigger> commandTriggers);

  CommandTrigger createCommandTrigger(ChatCommandTrigger trigger);

}
