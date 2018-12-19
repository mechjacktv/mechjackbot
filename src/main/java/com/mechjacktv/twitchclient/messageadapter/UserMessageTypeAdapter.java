package com.mechjacktv.twitchclient.messageadapter;

import com.google.protobuf.Message;

import com.mechjacktv.proto.twitchclient.TwitchClientMessage.User;

public final class UserMessageTypeAdapter extends BaseMessageTypeAdapter<User> {

  @Override
  Message.Builder getBuilder() {
    return User.newBuilder();
  }

}
