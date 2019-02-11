package tv.mechjack.twitchclient.messageadapter;

import com.google.protobuf.Message;

import tv.mechjack.protobuf.BaseMessageTypeAdapter;
import tv.mechjack.twitchclient.ProtoMessage.User;

public final class UserMessageTypeAdapter extends BaseMessageTypeAdapter<User> {

  @Override
  public Message.Builder getBuilder() {
    return User.newBuilder();
  }

}
