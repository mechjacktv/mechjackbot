package tv.mechjack.twitchclient.messageadapter;

import com.google.gson.GsonBuilder;

import tv.mechjack.platform.gson.TypeAdapterRegistrar;
import tv.mechjack.twitchclient.ProtoMessage.User;
import tv.mechjack.twitchclient.ProtoMessage.UserFollow;

public final class MessageTypeAdapterRegistrar implements TypeAdapterRegistrar {

  @Override
  public void registerTypeAdapters(final GsonBuilder gsonBuilder) {
    gsonBuilder.registerTypeAdapter(User.class, new UserMessageTypeAdapter());
    gsonBuilder.registerTypeAdapter(UserFollow.class, new UserFollowMessageTypeAdapter());
  }

}
