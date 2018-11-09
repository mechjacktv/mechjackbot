package com.mechjacktv.twitchclient.messageadapter;

import com.google.gson.GsonBuilder;

import com.mechjacktv.gson.TypeAdapterRegistrar;
import com.mechjacktv.proto.twitchclient.TwitchClientMessage.User;
import com.mechjacktv.proto.twitchclient.TwitchClientMessage.UserFollow;

public final class MessageTypeAdapterRegistrar implements TypeAdapterRegistrar {

  @Override
  public void registerTypeAdapters(final GsonBuilder gsonBuilder) {
    gsonBuilder.registerTypeAdapter(User.class, new UserMessageTypeAdapter());
    gsonBuilder.registerTypeAdapter(UserFollow.class, new UserFollowMessageTypeAdapter());
  }
}
