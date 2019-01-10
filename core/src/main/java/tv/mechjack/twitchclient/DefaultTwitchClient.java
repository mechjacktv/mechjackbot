package tv.mechjack.twitchclient;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import javax.inject.Inject;

import com.google.common.collect.Sets;

import tv.mechjack.twitchclient.ProtoMessage.User;
import tv.mechjack.twitchclient.ProtoMessage.UserFollows;
import tv.mechjack.twitchclient.ProtoMessage.Users;

final class DefaultTwitchClient implements TwitchClient {

  private final TwitchUsersEndpoint twitchUsersEndpoint;
  private final TwitchUsersFollowsEndpoint twitchUsersFollowsEndpoint;

  @Inject
  DefaultTwitchClient(final TwitchUsersEndpoint twitchUsersEndpoint,
      final TwitchUsersFollowsEndpoint twitchUsersFollowsEndpoint) {
    this.twitchUsersEndpoint = twitchUsersEndpoint;
    this.twitchUsersFollowsEndpoint = twitchUsersFollowsEndpoint;
  }

  @Override
  public Optional<TwitchUserId> getUserId(final TwitchLogin login) {
    Objects.requireNonNull(login, "`login` **MUST** not be `null`.");

    final Users users = this.getUsers(Sets.newHashSet(login), Sets.newHashSet());
    final List<User> userList = users.getUserList();

    if (userList.isEmpty()) {
      return Optional.empty();
    } else {
      return Optional.of(TwitchUserId.of(userList.get(0).getId()));
    }
  }

  @Override
  public Users getUsers(final Set<TwitchLogin> twitchLogins, final Set<TwitchUserId> twitchUserIds) {
    return this.twitchUsersEndpoint.getUsers(twitchLogins, twitchUserIds);
  }

  @Override
  public UserFollows getUserFollowsFromId(final TwitchUserId fromId) {
    return this.twitchUsersFollowsEndpoint.getUserFollowsFromId(fromId);
  }

  @Override
  public UserFollows getUserFollowsFromId(final TwitchUserId fromId, final TwitchUserFollowsCursor cursor) {
    return this.twitchUsersFollowsEndpoint.getUserFollowsFromId(fromId, cursor);
  }

}
