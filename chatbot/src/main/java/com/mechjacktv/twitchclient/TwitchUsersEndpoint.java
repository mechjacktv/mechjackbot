package com.mechjacktv.twitchclient;

import com.mechjacktv.twitchclient.TwitchClientMessage.Users;

import java.util.Set;

public interface TwitchUsersEndpoint {

  Users getUsers(Set<TwitchLogin> twitchLogins, Set<TwitchUserId> twitchUserIds);

}
