package com.mechjacktv.twitchclient.messageadapter;

import com.google.gson.GsonBuilder;
import com.mechjacktv.gson.TypeAdapterRegistrar;
import com.mechjacktv.twitchclient.TwitchClientMessage.User;
import com.mechjacktv.twitchclient.TwitchClientMessage.UserFollow;

public final class MessageAdapterRegistrar implements TypeAdapterRegistrar {

  @Override
  public void registerTypeAdapters(final GsonBuilder gsonBuilder) {
    gsonBuilder.registerTypeAdapter(User.class, new UserMessageAdapter());
    gsonBuilder.registerTypeAdapter(UserFollow.class, new UserFollowMessageAdapter());
  }
}
