package com.mechjacktv.mechjackbot;

public interface ChatBot {

  void sendMessage(final TwitchChannel twitchChannel, final Message message);

  void start();

  void stop();

}
