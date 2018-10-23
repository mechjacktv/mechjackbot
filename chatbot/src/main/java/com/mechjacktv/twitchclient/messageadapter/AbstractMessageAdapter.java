package com.mechjacktv.twitchclient.messageadapter;

import com.google.gson.JsonParser;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.google.protobuf.Message;
import com.google.protobuf.util.JsonFormat;

import java.io.IOException;

abstract class AbstractMessageAdapter<M extends Message> extends TypeAdapter<M> {

    @Override
    @SuppressWarnings("unchecked")
    public  M read(final JsonReader jsonReader) throws IOException {
        final Message.Builder builder = getBuilder();
        final JsonParser jsonParser = new JsonParser();

        JsonFormat.parser().merge(jsonParser.parse(jsonReader).toString(), builder);
        return (M) builder.build();
    }

    abstract Message.Builder getBuilder();

    @Override
    public final void write(final JsonWriter jsonWriter, final Message message) throws IOException {
        jsonWriter.jsonValue(JsonFormat.printer().print(message));
    }

}