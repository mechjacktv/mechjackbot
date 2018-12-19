package com.mechjacktv.mechjackbot;

import javax.inject.Inject;

import com.mechjacktv.testframework.ArbitraryDataGenerator;

public class TestMessageEvent implements MessageEvent {

  private ChatBot chatBot;
  private ChatUser chatUser;
  private Message message;
  private Message responseMessage;

  @Inject
  public TestMessageEvent(final ArbitraryDataGenerator arbitraryDataGenerator) {
    this.chatBot = new TestChatBot();
    this.chatUser = new TestChatUser(arbitraryDataGenerator);
    this.message = Message.of(arbitraryDataGenerator.getString());
    this.responseMessage = null;
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
  public Message getMessage() {
    return this.message;
  }

  public Message getResponseMessage() {
    return this.responseMessage;
  }

  @Override
  public void sendResponse(final Message message) {
    this.responseMessage = message;
  }

  public void setChatBot(final ChatBot chatBot) {
    this.chatBot = chatBot;
  }

  public void setChatUser(final ChatUser chatUser) {
    this.chatUser = chatUser;
  }

  public void setMessage(final Message message) {
    this.message = message;
  }

}
