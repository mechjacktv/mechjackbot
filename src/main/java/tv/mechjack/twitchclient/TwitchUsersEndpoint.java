package tv.mechjack.twitchclient;

import java.util.Set;

import tv.mechjack.proto.twitchclient.TwitchClientMessage.Users;

public interface TwitchUsersEndpoint {

  Users getUsers(Set<TwitchLogin> twitchLogins, Set<TwitchUserId> twitchUserIds);

}
