package com.mechjacktv.twitchclient.endpoint;

import com.google.common.base.Preconditions;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.mechjacktv.twitchclient.TwitchClientMessage.User;
import com.mechjacktv.twitchclient.TwitchClientMessage.Users;
import com.mechjacktv.twitchclient.TwitchClientUtils;
import com.mechjacktv.twitchclient.UsersEndpoint;

import java.util.Objects;
import java.util.Set;

public final class DefaultUsersEndpoint implements UsersEndpoint {

  private final Gson gson;
  private final TwitchClientUtils twitchClientUtils;
  private final TypeAdapter<User> userTypeAdapter;

  public DefaultUsersEndpoint(final Gson gson, final TwitchClientUtils twitchClientUtils) {
    this.gson = gson;
    this.twitchClientUtils = twitchClientUtils;
    this.userTypeAdapter = this.gson.getAdapter(User.class);
    Objects.requireNonNull(this.twitchClientUtils,
        "User adapter **MUST** be registered.");
  }

  @Override
  public Users getUsers(final Set<String> logins, final Set<String> ids) {
    Objects.requireNonNull(logins,
        "Logins set **MUST** not be `null.");
    Objects.requireNonNull(ids,
        "Ids set **MUST** not be `null.");
    Preconditions.checkArgument(logins.size() + ids.size() > 0,
        "Minimum number of combined Twitch logins and ids is 1.");
    // NOTE the documentation makes it look like maybe 100 each?
    Preconditions.checkArgument(logins.size() + ids.size() <= 100,
        "Maximum number of combined Twitch logins and ids is 100.");

    final String url = String.format("users/?%s", this.buildQuery(logins, ids));
    final Users.Builder usersBuilder = Users.newBuilder();

    this.twitchClientUtils.handleResponse(url, (responseReader) -> {
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
            this.twitchClientUtils.handleInvalidName(name);
            break;
        }
      }
      jsonReader.endObject();
    });
    return usersBuilder.build();
  }

  private String buildQuery(final Set<String> logins, final Set<String> ids) {
    final StringBuilder queryBuilder = new StringBuilder();

    for (final String login : logins) {
      queryBuilder.append(String.format("&login=%s", login));
    }
    for (final String id : ids) {
      queryBuilder.append(String.format("&id=%s", id));
    }
    return queryBuilder.toString().substring(1);
  }

}
