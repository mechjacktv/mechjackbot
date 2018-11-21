package com.mechjacktv.mechjackbot.command.shoutout;

import static org.mockito.Mockito.mock;

import java.util.Map;

import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mapdb.Serializer;

import com.mechjacktv.keyvaluestore.AbstractMessageStore;
import com.mechjacktv.keyvaluestore.MapKeyValueStore;
import com.mechjacktv.keyvaluestore.MessageStoreContractTests;
import com.mechjacktv.mechjackbot.configuration.ArbitraryChatBotConfiguration;
import com.mechjacktv.mechjackbot.configuration.MapAppConfiguration;
import com.mechjacktv.proto.mechjackbot.command.shoutout.ShoutOutServiceMessage.Caster;
import com.mechjacktv.proto.mechjackbot.command.shoutout.ShoutOutServiceMessage.CasterKey;
import com.mechjacktv.twitchclient.TwitchClient;
import com.mechjacktv.util.ArbitraryDataGenerator;
import com.mechjacktv.util.DefaultProtobufUtils;
import com.mechjacktv.util.scheduleservice.ScheduleService;

public class DefaultShoutOutDataStoreUnitTests extends MessageStoreContractTests<CasterKey, Caster> {

  private final ArbitraryDataGenerator arbitraryDataGenerator = new ArbitraryDataGenerator();

  protected CasterKey givenAKey() {
    return CasterKey.newBuilder().setName(this.arbitraryDataGenerator.getString()).build();
  }

  @Override
  protected Caster givenAValue() {
    return Caster.newBuilder().setName(this.arbitraryDataGenerator.getString())
        .setLastShoutOut(this.arbitraryDataGenerator.getLong()).build();
  }

  @Override
  protected AbstractMessageStore<CasterKey, Caster> givenASubjectToTest(Map<CasterKey, Caster> data) {
    final DB db = DBMaker.memoryDB().closeOnJvmShutdown().make();
    final DefaultShoutOutDataStore defaultShoutOutDataStore = new DefaultShoutOutDataStore(
        new MapAppConfiguration(this.executionUtils),
        new ArbitraryChatBotConfiguration(this.arbitraryDataGenerator),
        (name) -> new MapKeyValueStore(db.hashMap(name, Serializer.BYTE_ARRAY,
            Serializer.BYTE_ARRAY).createOrOpen()),
        this.executionUtils,
        new DefaultProtobufUtils(this.executionUtils), mock(ScheduleService.class), mock(TwitchClient.class));

    for (final CasterKey key : data.keySet()) {
      defaultShoutOutDataStore.put(key, data.get(key));
    }
    return defaultShoutOutDataStore;
  }

}
