package com.mechjacktv.twitchclient;

import com.mechjacktv.proto.twitchclient.TwitchClientMessage.UserFollows;

public interface TwitchUsersFollowsEndpoint {

  UserFollows getUserFollowsFromId(TwitchUserId fromId);

  UserFollows getUserFollowsFromId(TwitchUserId fromId, TwitchUserFollowsCursor cursor);

}
