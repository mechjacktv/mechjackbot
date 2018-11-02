package com.mechjacktv.twitchclient;

import com.google.gson.Gson;
import com.mechjacktv.twitchclient.endpoint.DefaultTwitchUsersFollowsEndpoint;

public class DefaultTwitchUsersFollowsEndpointUnitTests extends TwitchUsersFollowsEndpointContractTests {

  @Override
  TwitchUsersFollowsEndpoint givenASubjectToTest(final Gson gson, final TwitchClientUtils twitchClientUtils) {
    return new DefaultTwitchUsersFollowsEndpoint(gson, twitchClientUtils);
  }

}
