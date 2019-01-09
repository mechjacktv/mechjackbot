package tv.mechjack.twitchclient;

import java.util.Set;

import tv.mechjack.twitchclient.ProtoMessage.Users;

public interface TwitchUsersEndpoint {

  Users getUsers(Set<TwitchLogin> twitchLogins, Set<TwitchUserId> twitchUserIds);

}
