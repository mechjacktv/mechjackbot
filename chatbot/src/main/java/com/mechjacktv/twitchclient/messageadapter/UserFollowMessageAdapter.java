package com.mechjacktv.twitchclient.messageadapter;

import com.google.protobuf.Message;
import com.mechjacktv.twitchclient.TwitchClientMessage.UserFollow;

final class UserFollowMessageAdapter extends AbstractMessageAdapter<UserFollow> {

  @Override
  Message.Builder getBuilder() {
    return UserFollow.newBuilder();
  }

}
