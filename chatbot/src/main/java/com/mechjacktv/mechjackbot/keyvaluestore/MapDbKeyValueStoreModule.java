package com.mechjacktv.mechjackbot.keyvaluestore;

import com.google.inject.AbstractModule;
import com.mechjacktv.mechjackbot.KeyValueStoreFactory;

public final class MapDbKeyValueStoreModule extends AbstractModule {

  @Override
  protected void configure() {
      this.bind(KeyValueStoreFactory.class).to(MapDbKeyValueStoreFactory.class).asEagerSingleton();
  }

}
