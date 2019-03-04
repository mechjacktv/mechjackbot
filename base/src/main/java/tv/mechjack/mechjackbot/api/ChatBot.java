package tv.mechjack.mechjackbot.api;

public interface ChatBot {

  void sendMessage(final ChatChannelName chatChannelName, final ChatMessage chatMessage);

  void start();

  void stop();

}
