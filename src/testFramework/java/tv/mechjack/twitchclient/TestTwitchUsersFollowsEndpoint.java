package tv.mechjack.twitchclient;

import java.util.function.BiFunction;
import java.util.function.Function;

import tv.mechjack.twitchclient.ProtoMessage.UserFollows;

public class TestTwitchUsersFollowsEndpoint implements TwitchUsersFollowsEndpoint {

  private Function<TwitchUserId, UserFollows> getUserFollowsFromIdHandler;
  private BiFunction<TwitchUserId, TwitchUserFollowsCursor, UserFollows> getUserFollowsFromIdWithCursorHandler;

  TestTwitchUsersFollowsEndpoint() {
    this.getUserFollowsFromIdHandler = twitchUserId -> UserFollows.getDefaultInstance();
    this.getUserFollowsFromIdWithCursorHandler = (twitchUserId, twitchUserFollowsCursor) -> UserFollows
        .getDefaultInstance();
  }

  @Override
  public UserFollows getUserFollowsFromId(final TwitchUserId fromId) {
    return this.getUserFollowsFromIdHandler.apply(fromId);
  }

  public void setGetUserFollowsFromIdHandler(final Function<TwitchUserId, UserFollows> getUserFollowsFromIdHandler) {
    this.getUserFollowsFromIdHandler = getUserFollowsFromIdHandler;
  }

  @Override
  public UserFollows getUserFollowsFromId(final TwitchUserId fromId, final TwitchUserFollowsCursor cursor) {
    return this.getUserFollowsFromIdWithCursorHandler.apply(fromId, cursor);
  }

  public void setGetUserFollowsFromIdWithCursorHandler(
      final BiFunction<TwitchUserId, TwitchUserFollowsCursor, UserFollows> getUserFollowsFromIdWithCursorHandler) {
    this.getUserFollowsFromIdWithCursorHandler = getUserFollowsFromIdWithCursorHandler;
  }

}
