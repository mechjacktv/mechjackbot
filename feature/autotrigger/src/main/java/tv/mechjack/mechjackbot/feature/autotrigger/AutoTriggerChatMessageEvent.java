package tv.mechjack.mechjackbot.feature.autotrigger;

import tv.mechjack.mechjackbot.api.ChatBot;
import tv.mechjack.mechjackbot.api.ChatMessage;
import tv.mechjack.mechjackbot.api.ChatMessageEvent;
import tv.mechjack.mechjackbot.api.ChatUser;

class AutoTriggerChatMessageEvent implements ChatMessageEvent {

  private final ChatMessageEvent messageEvent;
  private final ChatMessage chatMessage;

  AutoTriggerChatMessageEvent(final ChatMessageEvent messageEvent,
      final ChatMessage chatMessage) {
    this.messageEvent = messageEvent;
    this.chatMessage = chatMessage;
  }

  @Override
  public ChatBot getChatBot() {
    return this.messageEvent.getChatBot();
  }

  @Override
  public ChatUser getChatUser() {
    return this.messageEvent.getChatUser();
  }

  @Override
  public ChatMessage getChatMessage() {
    return this.chatMessage;
  }

  @Override
  public void sendResponse(final ChatMessage chatMessage) {
    this.messageEvent.sendResponse(chatMessage);
  }

  @Override
  public void sendRawResponse(final ChatMessage chatMessage) {
    this.messageEvent.sendRawResponse(chatMessage);
  }

}
