package com.mechjacktv.twitchclient;

public interface UsersFollowsEndpoint {

  TwitchClientMessage.UserFollows getUserFollowsFromId(String fromId);

  TwitchClientMessage.UserFollows getUserFollowsFromId(String fromId, String cursor);

}
