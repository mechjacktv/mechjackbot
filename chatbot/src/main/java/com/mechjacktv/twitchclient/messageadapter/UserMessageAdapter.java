package com.mechjacktv.twitchclient.messageadapter;

import com.google.protobuf.Message;
import com.mechjacktv.twitchclient.TwitchClientMessage.User;

final class UserMessageAdapter extends AbstractMessageAdapter<User> {

  @Override
  Message.Builder getBuilder() {
    return User.newBuilder();
  }

}
