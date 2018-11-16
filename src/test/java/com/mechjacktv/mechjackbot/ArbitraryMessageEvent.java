package com.mechjacktv.mechjackbot;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.mechjacktv.util.ArbitraryDataGenerator;

public class ArbitraryMessageEvent implements MessageEvent {

  private final ChatBot chatBot;
  private final ChatUser chatUser;
  private Message message;
  private Message responseMessage;

  public ArbitraryMessageEvent(final ArbitraryDataGenerator arbitraryDataGenerator) {
    this.chatBot = mock(ChatBot.class);
    this.chatUser = mock(ChatUser.class);
    when(this.chatUser.getUsername()).thenReturn(ChatUsername.of(arbitraryDataGenerator.getString()));
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

  @Override
  public void sendResponse(final Message message) {
    this.responseMessage = message;
  }

  public void setMessage(final Message message) {
    this.message = message;
  }

  public Message getResponseMessage() {
    return this.responseMessage;
  }

}
