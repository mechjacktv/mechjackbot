package com.mechjacktv.twitchclient;

public interface UsersFollowsEndpoint {

  TwitchClientMessage.UserFollows getUserFollowsFromId(String fromId);

  TwitchClientMessage.UserFollows getUserFollowsFromId(String fromId, String cursor);

  TwitchClientMessage.UserFollows getUserFollowsToId(String toId);

  TwitchClientMessage.UserFollows getUserFollowsToId(String toId, String cursor);

  boolean isUserFollowing(String fromId, String toId);

}
