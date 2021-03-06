package tv.mechjack.twitchclient;

import java.util.Optional;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;

import javax.inject.Inject;

import tv.mechjack.twitchclient.ProtoMessage.UserFollows;
import tv.mechjack.twitchclient.ProtoMessage.Users;

public class TestTwitchClient implements TwitchClient {

  private Function<TwitchLogin, TwitchUserId> getUserIdImpl;
  private BiFunction<Set<TwitchLogin>, Set<TwitchUserId>, Users> getUsersImpl;
  private Function<TwitchUserId, UserFollows> getUserFollowsFromIdImpl;
  private BiFunction<TwitchUserId, TwitchUserFollowsCursor, UserFollows> getUserFollowsFromIdWithCursorImpl;

  @Inject
  TestTwitchClient(/*
                    * final TwitchUsersEndpoint twitchUsersEndpoint, final
                    * TwitchUsersFollowsEndpoint twitchUsersFollowsEndpoint
                    */) {
    // TODO (2018-12-18 mechjack): make use of test endpoints for default handlers
    this.getUserIdImpl = login -> null;
    this.getUsersImpl = (twitchLogins, twitchUserIds) -> null;
    this.getUserFollowsFromIdImpl = fromId -> null;
    this.getUserFollowsFromIdWithCursorImpl = (fromId, cursor) -> null;
  }

  @Override
  public Optional<TwitchUserId> getUserId(final TwitchLogin login) {
    return Optional.ofNullable(this.getUserIdImpl.apply(login));
  }

  public final void setGetUserIdImpl(final Function<TwitchLogin, TwitchUserId> getUserIdImpl) {
    this.getUserIdImpl = getUserIdImpl;
  }

  @Override
  public Users getUsers(final Set<TwitchLogin> twitchLogins, final Set<TwitchUserId> twitchUserIds) {
    return this.getUsersImpl.apply(twitchLogins, twitchUserIds);
  }

  public final void setGetUsersImpl(final BiFunction<Set<TwitchLogin>, Set<TwitchUserId>, Users> getUsersImpl) {
    this.getUsersImpl = getUsersImpl;
  }

  @Override
  public UserFollows getUserFollowsFromId(final TwitchUserId fromId) {
    return this.getUserFollowsFromIdImpl.apply(fromId);
  }

  public final void setGetUserFollowsFromIdImpl(final Function<TwitchUserId, UserFollows> getUserFollowsFromIdImpl) {
    this.getUserFollowsFromIdImpl = getUserFollowsFromIdImpl;
  }

  @Override
  public UserFollows getUserFollowsFromId(final TwitchUserId fromId, final TwitchUserFollowsCursor cursor) {
    return this.getUserFollowsFromIdWithCursorImpl.apply(fromId, cursor);
  }

  public final void setGetUserFollowsFromIdImpl(
      final BiFunction<TwitchUserId, TwitchUserFollowsCursor, UserFollows> getUserFollowsFromIdWithCursorImpl) {
    this.getUserFollowsFromIdWithCursorImpl = getUserFollowsFromIdWithCursorImpl;
  }

}
