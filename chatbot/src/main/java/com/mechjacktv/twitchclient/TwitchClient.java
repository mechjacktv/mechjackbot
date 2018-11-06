package com.mechjacktv.twitchclient;

import java.util.Optional;

public interface TwitchClient extends TwitchUsersEndpoint, TwitchUsersFollowsEndpoint {

    Optional<TwitchUserId> getUserId(TwitchLogin login);

}
