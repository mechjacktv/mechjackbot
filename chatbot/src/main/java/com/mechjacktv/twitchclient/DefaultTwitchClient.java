package com.mechjacktv.twitchclient;

import com.google.common.collect.Sets;
import com.google.gson.Gson;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

final class DefaultTwitchClient implements TwitchClient {

  private final UsersEndpoint usersEndpoint;
  private final UsersFollowsEndpoint usersFollowsEndpoint;

  DefaultTwitchClient(final Gson gson, final TwitchClientUtils twitchClientUtils) {
    this.usersEndpoint = new DefaultUsersEndpoint(gson, twitchClientUtils);
    this.usersFollowsEndpoint = new DefaultUsersFollowsEndpoint(gson, twitchClientUtils);
  }


  @Override
  public Optional<String> getUserId(final String login) {
    Objects.requireNonNull(login, "Twitch login **MUST** not be `null`.");

    final TwitchClientMessage.Users users = this.getUsers(Sets.newHashSet(login), Sets.newHashSet());
    final List<TwitchClientMessage.User> userList = users.getUserList();

    if (userList.isEmpty()) {
      return Optional.empty();
    } else {
      return Optional.of(userList.get(0).getId());
    }
  }

  @Override
  public TwitchClientMessage.Users getUsers(final Set<String> logins, final Set<String> ids) {
    return this.usersEndpoint.getUsers(logins, ids);
  }

  @Override
  public TwitchClientMessage.UserFollows getUserFollowsFromId(final String fromId) {
    return this.usersFollowsEndpoint.getUserFollowsFromId(fromId);
  }

  @Override
  public TwitchClientMessage.UserFollows getUserFollowsFromId(final String fromId, final String cursor) {
    return this.usersFollowsEndpoint.getUserFollowsFromId(fromId, cursor);
  }

  @Override
  public TwitchClientMessage.UserFollows getUserFollowsToId(final String toId) {
    return this.usersFollowsEndpoint.getUserFollowsToId(toId);
  }

  @Override
  public TwitchClientMessage.UserFollows getUserFollowsToId(final String toId, final String cursor) {
    return this.usersFollowsEndpoint.getUserFollowsToId(toId, cursor);
  }

  @Override
  public boolean isUserFollowing(final String fromId, final String toId) {
    return this.usersFollowsEndpoint.isUserFollowing(fromId, toId);
  }
}
