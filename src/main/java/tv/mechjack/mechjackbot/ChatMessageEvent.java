package tv.mechjack.mechjackbot;

public interface ChatMessageEvent {

  ChatBot getChatBot();

  ChatUser getChatUser();

  ChatMessage getChatMessage();

  void sendResponse(ChatMessage chatMessage);

}
