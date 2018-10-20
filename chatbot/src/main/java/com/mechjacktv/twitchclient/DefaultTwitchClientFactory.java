package com.mechjacktv.twitchclient;

import com.mechjacktv.util.ExecutionUtils;

import javax.inject.Inject;

final class DefaultTwitchClientFactory implements TwitchClientFactory {

    private final ExecutionUtils executionUtils;

    @Inject
    DefaultTwitchClientFactory(final ExecutionUtils executionUtils) {
        this.executionUtils = executionUtils;
    }

    @Override
    public TwitchClientBuilder newBuilder() {
        return new DefaultTwitchClientBuilder(executionUtils);
    }

    @Override
    public TwitchClient createTwitchClient(String clientId) {
        return newBuilder().setClientId(clientId).build();
    }

}
