package tv.mechjack.mechjackbot.api;

public interface ChatBot {

  void sendMessage(final ChatChannel chatChannel, final ChatMessage chatMessage);

  void start();

  void stop();

}
