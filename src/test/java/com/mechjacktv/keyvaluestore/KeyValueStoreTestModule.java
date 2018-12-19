package com.mechjacktv.keyvaluestore;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;

public class KeyValueStoreTestModule extends AbstractModule {

  @Override
  protected void configure() {
    this.bind(TestKeyValueStoreFactory.class).in(Scopes.SINGLETON);
    this.bind(KeyValueStoreFactory.class).to(TestKeyValueStoreFactory.class);
  }

}
