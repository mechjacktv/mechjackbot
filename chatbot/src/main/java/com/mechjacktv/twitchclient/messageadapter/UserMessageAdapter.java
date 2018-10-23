package com.mechjacktv.twitchclient.messageadapter;

import com.google.gson.stream.JsonReader;
import com.mechjacktv.twitchclient.TwitchClientMessage;

import java.io.IOException;

public final class UserMessageAdapter extends AbstractMessageAdapter<TwitchClientMessage.User> {

    @Override
    public TwitchClientMessage.User read(final JsonReader jsonReader) throws IOException {
        return (TwitchClientMessage.User) read(jsonReader, TwitchClientMessage.User.newBuilder());
    }

}
