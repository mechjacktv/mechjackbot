package tv.mechjack.platform.keyvaluestore;

import java.io.File;

import javax.inject.Inject;

import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mapdb.Serializer;

import tv.mechjack.platform.application.Application;

public final class MapDbKeyValueStoreFactory implements KeyValueStoreFactory {

  private final DB db;

  @Inject
  MapDbKeyValueStoreFactory(final Application application) {
    this.db = DBMaker.fileDB(new File(application.getApplicationDataLocation().value, "mapdb.bin"))
        .closeOnJvmShutdown()
        .make();
  }

  @Override
  public KeyValueStore createOrOpenKeyValueStore(String name) {
    return new MapKeyValueStore(this.db.hashMap(name, Serializer.STRING, Serializer.STRING).createOrOpen());
  }
}
