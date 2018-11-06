package com.mechjacktv.twitchclient;

import java.util.Set;

import com.mechjacktv.twitchclient.TwitchClientMessage.Users;

public interface TwitchUsersEndpoint {

  Users getUsers(Set<TwitchLogin> twitchLogins, Set<TwitchUserId> twitchUserIds);

}
