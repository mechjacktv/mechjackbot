package com.mechjacktv.twitchclient.messageadapter;

import com.google.protobuf.Message;
import com.mechjacktv.twitchclient.TwitchClientMessage;

class UserFollowMessageAdapter extends AbstractMessageAdapter<TwitchClientMessage.UserFollow> {

    @Override
    Message.Builder getBuilder() {
        return TwitchClientMessage.UserFollow.newBuilder();
    }

}