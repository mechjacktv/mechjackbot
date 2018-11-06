package com.mechjacktv.twitchclient;

import com.google.gson.Gson;

import com.mechjacktv.twitchclient.endpoint.DefaultTwitchUsersEndpoint;

public class DefaultTwitchUsersEndpointUnitTests extends TwitchUsersEndpointContractTests {

    @Override
    TwitchUsersEndpoint givenASubjectToTest(final Gson gson, final TwitchClientUtils twitchClientUtils) {
        return new DefaultTwitchUsersEndpoint(gson, twitchClientUtils);
    }

}
