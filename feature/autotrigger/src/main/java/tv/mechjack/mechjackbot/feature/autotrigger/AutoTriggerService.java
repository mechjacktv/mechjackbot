package tv.mechjack.mechjackbot.feature.autotrigger;

import java.util.List;

import tv.mechjack.mechjackbot.api.ChatCommandTrigger;
import tv.mechjack.mechjackbot.api.ChatMessageEvent;

public interface AutoTriggerService {

  boolean isExistingAutoTriggerList(ListName listName);

  List<ListName> getAutoTriggerListNames();

  void createAutoTriggerList(ListName name,
      TimeRequired timeRequired, MessageCount messageCount,
      ChatterCount chatterCount, Order order,
      List<ChatCommandTrigger> commandTriggers);

  void updateAutoTriggerList(ListName name,
      TimeRequired timeRequired, MessageCount messageCount,
      ChatterCount chatterCount, Order order,
      List<ChatCommandTrigger> commandTriggers);

  void removeAutoTriggerList(ListName listName);

  void handleMessageEvent(ChatMessageEvent chatMessageEvent);

}
