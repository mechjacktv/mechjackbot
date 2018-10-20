package com.mechjacktv.mechjackbot.keyvaluestore;

import com.mechjacktv.mechjackbot.ChatBotConfiguration;
import com.mechjacktv.mechjackbot.KeyValueStore;
import com.mechjacktv.mechjackbot.KeyValueStoreFactory;
import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mapdb.Serializer;

import javax.inject.Inject;
import java.io.File;

final class MapDbKeyValueStoreFactory implements KeyValueStoreFactory {

    private final DB db;

    @Inject
    MapDbKeyValueStoreFactory(final ChatBotConfiguration chatBotConfiguration) {
        this.db = DBMaker.fileDB(new File(chatBotConfiguration.getDataLocation(), "mapdb.bin"))
                .closeOnJvmShutdown()
                .make();
    }

    @Override
    public KeyValueStore createOrOpenKeyValueStore(String name) {
        return new MapDbKeyValueStore(db.hashMap(name, Serializer.BYTE_ARRAY, Serializer.BYTE_ARRAY).createOrOpen());
    }
}
