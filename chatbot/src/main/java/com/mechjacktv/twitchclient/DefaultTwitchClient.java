package com.mechjacktv.twitchclient;

import com.mechjacktv.util.ExecutionUtils;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

final class DefaultTwitchClient implements TwitchClient {

    private static final String TWITCH_API_URL = "https://api.twitch.tv/helix";

    private final String clientId;
    private final ExecutionUtils executionUtils;

    DefaultTwitchClient(final String clientId, final ExecutionUtils executionUtils) {
        this.clientId = clientId;
        this.executionUtils = executionUtils;
    }

    private URLConnection createUrlConnection(final String serviceUrl) {
        return executionUtils.softenException(() -> {
            final URL url = new URL(String.format("%s/%s", TWITCH_API_URL, serviceUrl));
            final URLConnection urlConnection = url.openConnection();

            urlConnection.setRequestProperty("Client-ID", this.clientId);
            return urlConnection;
        }, TwitchClientConnectException.class);
    }

    @Override
    public String getUserId(final String userLogin) {
        final InputStream inputStream = executionUtils.softenException(() -> {
            final URLConnection urlConnection = createUrlConnection(String.format("users?login=%s", userLogin));
            return urlConnection.getInputStream();
        }, TwitchClientConnectException.class);

        return null;
    }

}
