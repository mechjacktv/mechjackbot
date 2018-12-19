package com.mechjacktv.twitchclient;

import java.util.Objects;
import java.util.Set;

import javax.inject.Inject;

import com.google.common.base.Preconditions;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;

import com.mechjacktv.proto.twitchclient.TwitchClientMessage.User;
import com.mechjacktv.proto.twitchclient.TwitchClientMessage.Users;

public final class DefaultTwitchUsersEndpoint implements TwitchUsersEndpoint {

  private final Gson gson;
  private final TwitchClientUtils twitchClientUtils;
  private final TypeAdapter<User> userTypeAdapter;

  @Inject
  DefaultTwitchUsersEndpoint(final Gson gson, final TwitchClientUtils twitchClientUtils) {
    this.gson = gson;
    this.twitchClientUtils = twitchClientUtils;
    this.userTypeAdapter = this.gson.getAdapter(User.class);
    Objects.requireNonNull(this.twitchClientUtils,
        "User adapter **MUST** be registered.");
  }

  @Override
  public Users getUsers(final Set<TwitchLogin> logins, final Set<TwitchUserId> ids) {
    Objects.requireNonNull(logins,
        "Logins set **MUST** not be `null`.");
    Objects.requireNonNull(ids,
        "Ids set **MUST** not be `null`.");
    Preconditions.checkArgument(logins.size() + ids.size() > 0,
        "Minimum number of combined Twitch logins and ids is `1`.");
    // NOTE the documentation makes it look like maybe 100 each?
    Preconditions.checkArgument(logins.size() + ids.size() <= 100,
        "Maximum number of combined Twitch logins and ids is `100`.");

    final String url = String.format("users/?%s", this.buildQuery(logins, ids));
    final Users.Builder usersBuilder = Users.newBuilder();

    this.twitchClientUtils.handleResponse(TwitchUrl.of(url), (responseReader) -> {
      final JsonReader jsonReader = this.gson.newJsonReader(responseReader);

      jsonReader.beginObject();
      while (jsonReader.hasNext()) {
        final String name = jsonReader.nextName();

        switch (name) {
        case "data":
          jsonReader.beginArray();
          while (jsonReader.hasNext()) {
            usersBuilder.addUser(this.userTypeAdapter.read(jsonReader));
          }
          jsonReader.endArray();
          break;
        default:
          this.twitchClientUtils.handleUnknownObjectName(name);
          jsonReader.skipValue();
          break;
        }
      }
      jsonReader.endObject();
    });
    return usersBuilder.build();
  }

  private String buildQuery(final Set<TwitchLogin> twitchLogins, final Set<TwitchUserId> twitchUserIds) {
    final StringBuilder queryBuilder = new StringBuilder();

    for (final TwitchLogin twitchLogin : twitchLogins) {
      queryBuilder.append(String.format("&login=%s", twitchLogin.value));
    }
    for (final TwitchUserId twitchUserId : twitchUserIds) {
      queryBuilder.append(String.format("&id=%s", twitchUserId.value));
    }
    return queryBuilder.toString().substring(1);
  }

}
