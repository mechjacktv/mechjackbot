package com.mechjacktv.twitchclient;

import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.mechjacktv.twitchclient.TwitchClientMessage.User;
import com.mechjacktv.twitchclient.TwitchClientMessage.UserFollows;
import com.mechjacktv.twitchclient.TwitchClientMessage.Users;
import com.mechjacktv.twitchclient.endpoint.DefaultTwitchUsersEndpoint;
import com.mechjacktv.twitchclient.endpoint.DefaultTwitchUsersFollowsEndpoint;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

final class DefaultTwitchClient implements TwitchClient {

  private final TwitchUsersEndpoint usersEndpoint;
  private final TwitchUsersFollowsEndpoint usersFollowsEndpoint;

  DefaultTwitchClient(final Gson gson, final TwitchClientUtils twitchClientUtils) {
    this.usersEndpoint = new DefaultTwitchUsersEndpoint(gson, twitchClientUtils);
    this.usersFollowsEndpoint = new DefaultTwitchUsersFollowsEndpoint(gson, twitchClientUtils);
  }


  @Override
  public Optional<TwitchUserId> getUserId(final TwitchLogin login) {
    Objects.requireNonNull(login, "Twitch login **MUST** not be `null`.");

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
    return this.usersEndpoint.getUsers(twitchLogins, twitchUserIds);
  }

  @Override
  public UserFollows getUserFollowsFromId(final TwitchUserId fromId) {
    return this.usersFollowsEndpoint.getUserFollowsFromId(fromId);
  }

  @Override
  public UserFollows getUserFollowsFromId(final TwitchUserId fromId, final TwitchUserFollowsCursor cursor) {
    return this.usersFollowsEndpoint.getUserFollowsFromId(fromId, cursor);
  }

}
