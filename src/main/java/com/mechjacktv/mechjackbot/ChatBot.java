package com.mechjacktv.mechjackbot;

public interface ChatBot {

  void sendMessage(final TwitchChannel channel, final Message message);

  void start();

  void stop();

}
