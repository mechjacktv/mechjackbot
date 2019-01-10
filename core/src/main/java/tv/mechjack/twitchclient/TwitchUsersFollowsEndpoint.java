package tv.mechjack.twitchclient;

import tv.mechjack.twitchclient.ProtoMessage.UserFollows;

public interface TwitchUsersFollowsEndpoint {

  UserFollows getUserFollowsFromId(TwitchUserId fromId);

  UserFollows getUserFollowsFromId(TwitchUserId fromId, TwitchUserFollowsCursor cursor);

}
