package com.mechjacktv.twitchclient.messageadapter;

import com.google.gson.GsonBuilder;
import com.mechjacktv.gson.TypeAdapterRegistrar;
import com.mechjacktv.twitchclient.TwitchClientMessage;

public final class MessageAdapterRegistrar implements TypeAdapterRegistrar {

  @Override
  public void registerTypeAdapters(final GsonBuilder gsonBuilder) {
    gsonBuilder.registerTypeAdapter(TwitchClientMessage.User.class, new UserMessageAdapter());
    gsonBuilder.registerTypeAdapter(TwitchClientMessage.UserFollow.class, new UserFollowMessageAdapter());
  }
}
