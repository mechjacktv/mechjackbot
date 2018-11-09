package com.mechjacktv.twitchclient.endpoint;

import java.util.Objects;

import javax.inject.Inject;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;

import com.mechjacktv.twitchclient.*;
import com.mechjacktv.twitchclient.TwitchClientMessage.UserFollow;
import com.mechjacktv.twitchclient.TwitchClientMessage.UserFollows;
import com.mechjacktv.twitchclient.TwitchClientMessage.UserFollows.Builder;

public final class DefaultTwitchUsersFollowsEndpoint implements TwitchUsersFollowsEndpoint {

  private final Gson gson;
  private final TwitchClientUtils twitchClientUtils;
  private final TypeAdapter<UserFollow> userTypeAdapter;

  @Inject
  public DefaultTwitchUsersFollowsEndpoint(final Gson gson, final TwitchClientUtils twitchClientUtils) {
    this.gson = gson;
    this.twitchClientUtils = twitchClientUtils;
    this.userTypeAdapter = this.gson.getAdapter(UserFollow.class);
    Objects.requireNonNull(this.twitchClientUtils,
        "UserFollow adapter **MUST** be registered.");
  }

  @Override
  public final UserFollows getUserFollowsFromId(final TwitchUserId fromId) {
    Objects.requireNonNull(fromId, "fromId **MUST** not be `null`.");
    return this.getUsersFollows(String.format("from_id=%s", fromId));
  }

  @Override
  public final UserFollows getUserFollowsFromId(final TwitchUserId fromId, final TwitchUserFollowsCursor cursor) {
    Objects.requireNonNull(fromId, "fromId **MUST** not be `null`.");
    Objects.requireNonNull(cursor, "cursor **MUST** not be `null`.");
    return this.getUsersFollows(String.format("from_id=%s&after=%s", fromId, cursor));
  }

  private UserFollows getUsersFollows(final String queryString) {
    final String url = String.format("users/follows/?first=100&%s", queryString);
    final Builder userFollowsBuilder = UserFollows.newBuilder();

    this.twitchClientUtils.handleResponse(TwitchUrl.of(url), (responseReader) -> {
      final JsonReader jsonReader = this.gson.newJsonReader(responseReader);

      jsonReader.beginObject();
      while (jsonReader.hasNext()) {
        final String name = jsonReader.nextName();

        switch (name) {
        case "total":
          userFollowsBuilder.setTotalFollows(jsonReader.nextInt());
          break;
        case "pagination":
          jsonReader.beginObject();
          while (jsonReader.hasNext()) {
            switch (jsonReader.nextName()) {
            case "cursor":
              userFollowsBuilder.setCursor(jsonReader.nextString());
              break;
            default:
              this.twitchClientUtils.handleInvalidObjectName(name);
              break;
            }
          }
          jsonReader.endObject();
          break;
        case "data":
          jsonReader.beginArray();
          while (jsonReader.hasNext()) {
            userFollowsBuilder.addUserFollow(this.userTypeAdapter.read(jsonReader));
          }
          jsonReader.endArray();
          break;
        default:
          this.twitchClientUtils.handleInvalidObjectName(name);
          break;
        }
      }
      jsonReader.endObject();
    });
    return userFollowsBuilder.build();
  }

}
