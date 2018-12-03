package com.mechjacktv.mechjackbot;

public class TestChatBot implements ChatBot {

  private Message message;
  private boolean started;
  private boolean stopped;
  private TwitchChannel twitchChannel;

  public TestChatBot() {
    this.message = null;
    this.started = false;
    this.stopped = false;
    this.twitchChannel = null;
  }

  public Message getMessage() {
    return this.message;
  }

  public TwitchChannel getTwitchChannel() {
    return this.twitchChannel;
  }

  @Override
  public void sendMessage(final TwitchChannel twitchChannel, final Message message) {
    this.message = message;
    this.twitchChannel = twitchChannel;
  }

  @Override
  public void start() {
    this.started = true;
  }

  @Override
  public void stop() {
    this.stopped = true;
  }

  public boolean wasStarted() {
    return this.started;
  }

  public boolean wasStopped() {
    return this.stopped;
  }

}
