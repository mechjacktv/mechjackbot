package com.mechjacktv.twitchclient;

import com.mechjacktv.twitchclient.TwitchClientMessage.UserFollows;

public interface UsersFollowsEndpoint {

  UserFollows getUserFollowsFromId(String fromId);

  UserFollows getUserFollowsFromId(String fromId, String cursor);

}
