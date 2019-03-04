package tv.mechjack.mechjackbot.api;

public class TestChatBot implements ChatBot {

  private ChatMessage chatMessage;
  private boolean started;
  private boolean stopped;
  private ChatChannelName chatChannelName;

  public TestChatBot() {
    this.chatMessage = null;
    this.started = false;
    this.stopped = false;
    this.chatChannelName = null;
  }

  public ChatMessage getChatMessage() {
    return this.chatMessage;
  }

  public ChatChannelName getChatChannelName() {
    return this.chatChannelName;
  }

  @Override
  public void sendMessage(final ChatChannelName chatChannelName, final ChatMessage chatMessage) {
    this.chatMessage = chatMessage;
    this.chatChannelName = chatChannelName;
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
