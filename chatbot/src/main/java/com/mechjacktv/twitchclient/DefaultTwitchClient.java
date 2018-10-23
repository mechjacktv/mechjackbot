package com.mechjacktv.twitchclient;

import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.mechjacktv.twitchclient.TwitchClientMessage.User;
import com.mechjacktv.twitchclient.TwitchClientMessage.UserFollows;
import com.mechjacktv.twitchclient.TwitchClientMessage.Users;
import com.mechjacktv.twitchclient.endpoint.DefaultUsersEndpoint;
import com.mechjacktv.twitchclient.endpoint.DefaultUsersFollowsEndpoint;

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

    final Users users = this.getUsers(Sets.newHashSet(login), Sets.newHashSet());
    final List<User> userList = users.getUserList();

    if (userList.isEmpty()) {
      return Optional.empty();
    } else {
      return Optional.of(userList.get(0).getId());
    }
  }

  @Override
  public Users getUsers(final Set<String> logins, final Set<String> ids) {
    return this.usersEndpoint.getUsers(logins, ids);
  }

  @Override
  public UserFollows getUserFollowsFromId(final String fromId) {
    return this.usersFollowsEndpoint.getUserFollowsFromId(fromId);
  }

  @Override
  public UserFollows getUserFollowsFromId(final String fromId, final String cursor) {
    return this.usersFollowsEndpoint.getUserFollowsFromId(fromId, cursor);
  }

}
