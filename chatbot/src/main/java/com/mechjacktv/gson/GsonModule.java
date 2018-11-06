package com.mechjacktv.gson;

import java.util.Set;

import javax.inject.Singleton;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;

public final class GsonModule extends AbstractModule {

  @Override
  protected final void configure() {
    // empty
  }

  @Provides
  @Singleton
  Gson provideGson(final Set<TypeAdapterRegistrar> typeAdapterRegistrars) {
    final GsonBuilder gsonBuilder = new GsonBuilder();

    gsonBuilder.setPrettyPrinting();
    for (final TypeAdapterRegistrar typeAdapterRegistrar : typeAdapterRegistrars) {
      typeAdapterRegistrar.registerTypeAdapters(gsonBuilder);
    }
    return gsonBuilder.create();
  }
}
