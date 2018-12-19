package com.mechjacktv.twitchclient.messageadapter;

import com.google.protobuf.Message;

import com.mechjacktv.proto.twitchclient.TwitchClientMessage.UserFollow;

public final class UserFollowMessageTypeAdapter extends BaseMessageTypeAdapter<UserFollow> {

  @Override
  Message.Builder getBuilder() {
    return UserFollow.newBuilder();
  }

}
