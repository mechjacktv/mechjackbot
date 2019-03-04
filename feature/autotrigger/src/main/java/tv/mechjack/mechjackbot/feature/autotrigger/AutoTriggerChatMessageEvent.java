package tv.mechjack.mechjackbot.feature.autotrigger;

import tv.mechjack.mechjackbot.api.ChatMessage;
import tv.mechjack.mechjackbot.api.ChatMessageEvent;
import tv.mechjack.mechjackbot.api.ChatMessageEventWrapper;

class AutoTriggerChatMessageEvent extends ChatMessageEventWrapper {

  private final ChatMessage chatMessage;

  AutoTriggerChatMessageEvent(final ChatMessageEvent messageEvent,
      final ChatMessage chatMessage) {
    super(messageEvent);
    this.chatMessage = chatMessage;
  }

  @Override
  public ChatMessage getChatMessage() {
    return this.chatMessage;
  }

}
