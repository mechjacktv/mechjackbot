package com.mechjacktv.mechjackbot.command.shoutout;

import com.mechjacktv.keyvaluestore.MessageStore;
import com.mechjacktv.proto.mechjackbot.command.shoutout.ShoutOutServiceMessage.Caster;
import com.mechjacktv.proto.mechjackbot.command.shoutout.ShoutOutServiceMessage.CasterKey;

public interface ShoutOutDataStore extends MessageStore<CasterKey, Caster> {

  CasterKey createCasterKey(String casterName);

  Caster createCaster(String casterName, Long lastShoutOut);

}
