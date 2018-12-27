package tv.mechjack.twitchclient;

import tv.mechjack.proto.twitchclient.TwitchClientMessage.UserFollows;

public interface TwitchUsersFollowsEndpoint {

  UserFollows getUserFollowsFromId(TwitchUserId fromId);

  UserFollows getUserFollowsFromId(TwitchUserId fromId, TwitchUserFollowsCursor cursor);

}
