package com.mechjacktv.mechjackbot;

public interface ChatUser {

  ChatUsername getUsername();

  boolean hasRole(ViewerRole viewerRole);

}
