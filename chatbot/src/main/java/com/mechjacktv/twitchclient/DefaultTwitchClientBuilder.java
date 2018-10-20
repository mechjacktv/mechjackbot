package com.mechjacktv.twitchclient;

import com.mechjacktv.util.ExecutionUtils;

final class DefaultTwitchClientBuilder implements TwitchClientBuilder {

    private String clientId;
    private final ExecutionUtils executionUtils;

    DefaultTwitchClientBuilder(final ExecutionUtils executionUtils) {
        this.executionUtils = executionUtils;
    }

    @Override
    public TwitchClientBuilder setClientId(final String clientId) {
        this.clientId = clientId;
        return this;
    }

    @Override
    public TwitchClient build() {
        return new DefaultTwitchClient(this.clientId, executionUtils);
    }

}
