package tv.mechjack.twitchclient;

import java.util.Objects;
import java.util.Set;
import java.util.function.BiFunction;

import javax.inject.Inject;

import tv.mechjack.platform.utils.ExecutionUtils;
import tv.mechjack.twitchclient.ProtoMessage.Users;

public class TestTwitchUsersEndpoint implements TwitchUsersEndpoint {

  private final ExecutionUtils executionUtils;
  private BiFunction<Set<TwitchLogin>, Set<TwitchUserId>, Users> getUsersHandler;

  @Inject
  TestTwitchUsersEndpoint(final ExecutionUtils executionUtils) {
    this.executionUtils = executionUtils;
    this.getUsersHandler = (twitchLogins, twitchUserIds) -> Users.getDefaultInstance();
  }

  @Override
  public Users getUsers(final Set<TwitchLogin> twitchLogins, final Set<TwitchUserId> twitchUserIds) {
    return this.getUsersHandler.apply(twitchLogins, twitchUserIds);
  }

  public final void setGetUsersHandler(final BiFunction<Set<TwitchLogin>, Set<TwitchUserId>, Users> getUsersHandler) {
    Objects.requireNonNull(getUsersHandler, this.executionUtils.nullMessageForName("getUsersHandler"));
    this.getUsersHandler = getUsersHandler;
  }

}
