package com.mechjacktv.keyvaluestore;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;

public final class KeyValueStoreModule extends AbstractModule {

  @Override
  protected void configure() {
    this.bind(KeyValueStoreFactory.class).to(MapDbKeyValueStoreFactory.class).in(Scopes.SINGLETON);
  }

}
