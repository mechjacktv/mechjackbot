package com.mechjacktv.twitchclient;

import com.mechjacktv.twitchclient.TwitchClientMessage.Users;

import java.util.Set;

public interface UsersEndpoint {

  Users getUsers(Set<String> logins, Set<String> ids);

}
