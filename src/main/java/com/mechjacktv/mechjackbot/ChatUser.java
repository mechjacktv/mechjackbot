package com.mechjacktv.mechjackbot;

public interface ChatUser {

  ChatUsername getUsername();

  boolean hasAccessLevel(AccessLevel accessLevel);

}
