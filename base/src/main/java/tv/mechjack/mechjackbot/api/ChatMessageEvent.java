package tv.mechjack.mechjackbot.api;

public interface ChatMessageEvent {

  ChatBot getChatBot();

  ChatUser getChatUser();

  ChatMessage getChatMessage();

  ChatChannel getChatChannel();

  void sendResponse(ChatMessage chatMessage);

  void sendRawResponse(ChatMessage chatMessage);

}
