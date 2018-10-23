package com.mechjacktv.twitchclient.messageadapter;

import com.google.protobuf.Message;
import com.mechjacktv.twitchclient.TwitchClientMessage;

final class UserMessageAdapter extends AbstractMessageAdapter<TwitchClientMessage.User> {

  @Override
  Message.Builder getBuilder() {
    return TwitchClientMessage.User.newBuilder();
  }

}
