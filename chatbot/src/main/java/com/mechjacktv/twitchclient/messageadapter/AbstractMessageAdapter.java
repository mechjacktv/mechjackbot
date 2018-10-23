package com.mechjacktv.twitchclient.messageadapter;

import com.google.gson.JsonParser;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.google.protobuf.Message;
import com.google.protobuf.util.JsonFormat;

import java.io.IOException;

public abstract class AbstractMessageAdapter<M extends Message> extends TypeAdapter<M> {

    final Message read(final JsonReader jsonReader, final Message.Builder builder) throws IOException {
        final JsonParser jsonParser = new JsonParser();

        JsonFormat.parser().merge(jsonParser.parse(jsonReader).toString(), builder);
        return builder.build();
    }

    @Override
    public final void write(final JsonWriter jsonWriter, final Message message) throws IOException {
        jsonWriter.jsonValue(JsonFormat.printer().print(message));
    }

}
