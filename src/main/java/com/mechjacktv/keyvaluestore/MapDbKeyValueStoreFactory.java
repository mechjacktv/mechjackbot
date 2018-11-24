package com.mechjacktv.keyvaluestore;

import java.io.File;

import javax.inject.Inject;

import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mapdb.Serializer;

import com.mechjacktv.mechjackbot.ChatBotConfiguration;

final class MapDbKeyValueStoreFactory implements KeyValueStoreFactory {

  private final DB db;

  @Inject
  MapDbKeyValueStoreFactory(final ChatBotConfiguration chatBotConfiguration) {
    this.db = DBMaker.fileDB(new File(chatBotConfiguration.getDataLocation().value, "mapdb.bin"))
        .closeOnJvmShutdown()
        .make();
  }

  @Override
  public KeyValueStore createOrOpenKeyValueStore(String name) {
    return new MapKeyValueStore(this.db.hashMap(name, Serializer.STRING, Serializer.STRING).createOrOpen());
  }
}
