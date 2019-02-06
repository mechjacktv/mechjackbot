package tv.mechjack.mechjackbot.api;

public interface ChatMessageEvent {

  ChatBot getChatBot();

  ChatUser getChatUser();

  ChatMessage getChatMessage();

  void sendResponse(ChatMessage chatMessage);

}
