package tv.mechjack.mechjackbot.api;

public class TestChatBot implements ChatBot {

  private ChatMessage chatMessage;
  private boolean started;
  private boolean stopped;
  private ChatChannel chatChannel;

  public TestChatBot() {
    this.chatMessage = null;
    this.started = false;
    this.stopped = false;
    this.chatChannel = null;
  }

  public ChatMessage getChatMessage() {
    return this.chatMessage;
  }

  public ChatChannel getChatChannel() {
    return this.chatChannel;
  }

  @Override
  public void sendMessage(final ChatChannel chatChannel, final ChatMessage chatMessage) {
    this.chatMessage = chatMessage;
    this.chatChannel = chatChannel;
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
