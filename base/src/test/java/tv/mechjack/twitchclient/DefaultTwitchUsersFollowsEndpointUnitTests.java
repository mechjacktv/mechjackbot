package tv.mechjack.twitchclient;

import com.google.gson.Gson;

public class DefaultTwitchUsersFollowsEndpointUnitTests extends TwitchUsersFollowsEndpointContractTests {

  @Override
  protected TwitchUsersFollowsEndpoint givenASubjectToTest(final Gson gson, final TwitchClientUtils twitchClientUtils) {
    return new DefaultTwitchUsersFollowsEndpoint(gson, twitchClientUtils);
  }

}
