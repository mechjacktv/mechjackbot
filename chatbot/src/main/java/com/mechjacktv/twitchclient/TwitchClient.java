package com.mechjacktv.twitchclient;

import java.util.Optional;

public interface TwitchClient extends UsersEndpoint, UsersFollowsEndpoint {

  Optional<String> getUserId(String login);

}
