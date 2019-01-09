package tv.mechjack.mechjackbot;

public interface ChatBot {

  void sendMessage(final ChatChannel chatChannel, final ChatMessage chatMessage);

  void start();

  void stop();

}