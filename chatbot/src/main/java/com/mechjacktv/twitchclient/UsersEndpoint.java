package com.mechjacktv.twitchclient;

import java.util.Set;

interface UsersEndpoint {

  TwitchClientMessage.Users getUsers(Set<String> logins, Set<String> ids);

}
