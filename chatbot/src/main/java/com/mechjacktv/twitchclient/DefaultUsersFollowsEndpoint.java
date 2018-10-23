package com.mechjacktv.twitchclient;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;

import java.util.Objects;

final class DefaultUsersFollowsEndpoint implements UsersFollowsEndpoint {

  private final Gson gson;
  private final TwitchClientUtils twitchClientUtils;
  private final TypeAdapter<TwitchClientMessage.UserFollow> userTypeAdapter;

  DefaultUsersFollowsEndpoint(final Gson gson, final TwitchClientUtils twitchClientUtils) {
    this.gson = gson;
    this.twitchClientUtils = twitchClientUtils;
    this.userTypeAdapter = this.gson.getAdapter(TwitchClientMessage.UserFollow.class);
    Objects.requireNonNull(this.twitchClientUtils,
        "TwitchClientMessage.UserFollow adapter **MUST** be registered.");
  }

  @Override
  public final TwitchClientMessage.UserFollows getUserFollowsFromId(final String fromId) {
    Objects.requireNonNull(fromId, "fromId **MUST** not be `null.");
    return getUsersFollows(String.format("from_id=%s", fromId));
  }

  @Override
  public final TwitchClientMessage.UserFollows getUserFollowsFromId(final String fromId, final String cursor) {
    Objects.requireNonNull(fromId, "fromId **MUST** not be `null.");
    Objects.requireNonNull(cursor, "cursor **MUST** not be `null.");
    return getUsersFollows(String.format("from_id=%s&after=%s", fromId, cursor));
  }

  @Override
  public final TwitchClientMessage.UserFollows getUserFollowsToId(final String toId) {
    Objects.requireNonNull(toId, "toId **MUST** not be `null.");
    return getUsersFollows(String.format("to_id=%s", toId));
  }

  @Override
  public final TwitchClientMessage.UserFollows getUserFollowsToId(final String toId, final String cursor) {
    Objects.requireNonNull(toId, "toId **MUST** not be `null.");
    Objects.requireNonNull(cursor, "cursor **MUST** not be `null.");
    return getUsersFollows(String.format("to_id=%s&after=%s", toId, cursor));
  }

  @Override
  public final boolean isUserFollowing(final String fromId, final String toId) {
    Objects.requireNonNull(fromId, "fromId **MUST** not be `null.");
    Objects.requireNonNull(toId, "toId **MUST** not be `null.");

    final TwitchClientMessage.UserFollows userFollows =
        getUsersFollows(String.format("from_id=%s&to_id=%s", fromId, toId));

    return userFollows.getUserFollowList().size() > 0;
  }

  private TwitchClientMessage.UserFollows getUsersFollows(final String queryString) {
    final String url = String.format("users/follows/?first=100&%s", queryString);
    final TwitchClientMessage.UserFollows.Builder userFollowsBuilder = TwitchClientMessage.UserFollows.newBuilder();

    this.twitchClientUtils.handleResponse(url, (responseReader) -> {
      final JsonReader jsonReader = gson.newJsonReader(responseReader);

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
                  this.twitchClientUtils.handleInvalidName(name);
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
            this.twitchClientUtils.handleInvalidName(name);
            break;
        }
      }
      jsonReader.endObject();
    });
    return userFollowsBuilder.build();
  }

}

