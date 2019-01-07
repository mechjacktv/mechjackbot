package tv.mechjack.twitchclient.messageadapter;

import com.google.protobuf.Message;

import tv.mechjack.twitchclient.ProtoMessage.UserFollow;

public final class UserFollowMessageTypeAdapter extends BaseMessageTypeAdapter<UserFollow> {

  @Override
  Message.Builder getBuilder() {
    return UserFollow.newBuilder();
  }

}
