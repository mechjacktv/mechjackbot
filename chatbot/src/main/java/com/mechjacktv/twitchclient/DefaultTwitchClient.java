package com.mechjacktv.twitchclient;

import com.google.common.collect.Sets;
import com.google.gson.Gson;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

final class DefaultTwitchClient implements TwitchClient {

    private final GetUsersEndpoint getUsersEndpoint;

    DefaultTwitchClient(final Gson gson, final TwitchClientUtils twitchClientUtils) {
        this.getUsersEndpoint = new DefaultGetUsersEndpoint(gson, twitchClientUtils);
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
    public TwitchClientMessage.Users getUsers(Set<String> logins, Set<String> ids) {
        return this.getUsersEndpoint.getUsers(logins, ids);
    }

}
