package com.mechjacktv.gson;

import com.google.gson.GsonBuilder;

public interface TypeAdapterRegistrar {

  void registerTypeAdapters(GsonBuilder gsonBuilder);

}
