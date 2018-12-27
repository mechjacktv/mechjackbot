package tv.mechjack.twitchclient;

import com.google.gson.Gson;

public class DefaultTwitchUsersEndpointUnitTests extends TwitchUsersEndpointContractTests {

  @Override
  protected TwitchUsersEndpoint givenASubjectToTest(final Gson gson, final TwitchClientUtils twitchClientUtils) {
    return new DefaultTwitchUsersEndpoint(gson, twitchClientUtils);
  }

}
