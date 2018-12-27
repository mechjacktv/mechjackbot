package tv.mechjack.twitchclient.messageadapter;

import com.google.gson.GsonBuilder;

import tv.mechjack.gson.TypeAdapterRegistrar;
import tv.mechjack.proto.twitchclient.TwitchClientMessage.User;
import tv.mechjack.proto.twitchclient.TwitchClientMessage.UserFollow;

public final class MessageTypeAdapterRegistrar implements TypeAdapterRegistrar {

  @Override
  public void registerTypeAdapters(final GsonBuilder gsonBuilder) {
    gsonBuilder.registerTypeAdapter(User.class, new UserMessageTypeAdapter());
    gsonBuilder.registerTypeAdapter(UserFollow.class, new UserFollowMessageTypeAdapter());
  }

}
