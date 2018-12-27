package tv.mechjack.twitchclient;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;

public class TwitchClientTestModule extends AbstractModule {

  @Override
  protected void configure() {
    this.bind(TestTwitchClient.class).in(Scopes.SINGLETON);
    this.bind(TwitchClient.class).to(TestTwitchClient.class);

    this.bind(TwitchClientConfiguration.class).to(TestTwitchClientConfiguration.class).in(Scopes.SINGLETON);

    this.bind(TestTwitchUsersEndpoint.class).in(Scopes.SINGLETON);
    this.bind(TwitchUsersEndpoint.class).to(TestTwitchUsersEndpoint.class);

    this.bind(TestTwitchUsersFollowsEndpoint.class).in(Scopes.SINGLETON);
    this.bind(TwitchUsersFollowsEndpoint.class).to(TestTwitchUsersFollowsEndpoint.class);

    this.bind(TestUrlConnectionFactory.class).in(Scopes.SINGLETON);
    this.bind(UrlConnectionFactory.class).to(TestUrlConnectionFactory.class);
  }

}
