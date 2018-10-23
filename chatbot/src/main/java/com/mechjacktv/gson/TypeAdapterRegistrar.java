package com.mechjacktv.gson;

import com.google.gson.GsonBuilder;

public interface TypeAdapterRegistrar {

    GsonBuilder registerTypeAdapters(GsonBuilder gsonBuilder);

}
