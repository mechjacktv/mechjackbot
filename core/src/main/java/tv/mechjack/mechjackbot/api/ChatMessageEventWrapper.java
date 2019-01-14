package tv.mechjack.mechjackbot.api;

public abstract class ChatMessageEventWrapper implements ChatMessageEvent {

  private final ChatMessageEvent chatMessageEvent;

  public ChatMessageEventWrapper(final ChatMessageEvent chatMessageEvent) {
    this.chatMessageEvent = chatMessageEvent;
  }

  @Override
  public ChatBot getChatBot() {
    return this.chatMessageEvent.getChatBot();
  }

  @Override
  public ChatUser getChatUser() {
    return this.chatMessageEvent.getChatUser();
  }

  @Override
  public ChatMessage getChatMessage() {
    return this.chatMessageEvent.getChatMessage();
  }

  @Override
  public void sendResponse(final ChatMessage chatMessage) {
    this.chatMessageEvent.sendResponse(chatMessage);
  }

}
