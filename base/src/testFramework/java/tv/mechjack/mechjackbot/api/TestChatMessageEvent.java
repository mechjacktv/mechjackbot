package tv.mechjack.mechjackbot.api;

import javax.inject.Inject;

import tv.mechjack.testframework.ArbitraryData;

public class TestChatMessageEvent implements ChatMessageEvent {

  private ChatBot chatBot;
  private ChatUser chatUser;
  private ChatMessage chatMessage;
  private ChatMessage responseChatMessage;

  @Inject
  public TestChatMessageEvent(final ArbitraryData arbitraryDataGenerator) {
    this.chatBot = new TestChatBot();
    this.chatUser = new TestChatUser(arbitraryDataGenerator);
    this.chatMessage = ChatMessage.of(arbitraryDataGenerator.getString());
    this.responseChatMessage = null;
  }

  @Override
  public ChatBot getChatBot() {
    return this.chatBot;
  }

  @Override
  public ChatUser getChatUser() {
    return this.chatUser;
  }

  @Override
  public ChatMessage getChatMessage() {
    return this.chatMessage;
  }

  public ChatMessage getResponseChatMessage() {
    return this.responseChatMessage;
  }

  @Override
  public void sendResponse(final ChatMessage chatMessage) {
    this.responseChatMessage = chatMessage;
  }

  @Override
  public void sendRawResponse(final ChatMessage chatMessage) {
    this.responseChatMessage = chatMessage;
  }

  public void setChatBot(final ChatBot chatBot) {
    this.chatBot = chatBot;
  }

  public void setChatUser(final ChatUser chatUser) {
    this.chatUser = chatUser;
  }

  public void setChatMessage(final ChatMessage chatMessage) {
    this.chatMessage = chatMessage;
  }

}
