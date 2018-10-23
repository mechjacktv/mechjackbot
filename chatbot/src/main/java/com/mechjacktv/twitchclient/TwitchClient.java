package com.mechjacktv.twitchclient;

import java.util.Optional;

public interface TwitchClient extends GetUsersEndpoint {

    Optional<String> getUserId(String login);

}
