package com.mechjacktv.keyvaluestore;

import com.google.inject.AbstractModule;

public final class KeyValueStoreModule extends AbstractModule {

  @Override
  protected void configure() {
    this.bind(KeyValueStoreFactory.class).to(MapDbKeyValueStoreFactory.class).asEagerSingleton();
  }

}
