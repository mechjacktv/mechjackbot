package com.mechjacktv.twitchclient;

import java.util.Set;

public interface GetUsersEndpoint {

    TwitchClientMessage.Users getUsers(Set<String> logins, Set<String> ids);

}
