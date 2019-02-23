package tv.mechjack.mechjackbot.webapp.tempdata;

import com.google.gson.GsonBuilder;

import tv.mechjack.platform.gson.TypeAdapterRegistrar;

public class WebappTypeAdapterRegistrar implements TypeAdapterRegistrar {

  @Override
  public void registerTypeAdapters(final GsonBuilder gsonBuilder) {
    gsonBuilder.registerTypeAdapter(ApplicationCredentials.class,
        new ApplicationCredentialsTypeAdapter());
  }

}
