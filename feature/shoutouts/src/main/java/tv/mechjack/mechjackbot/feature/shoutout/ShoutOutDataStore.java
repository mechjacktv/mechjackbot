package tv.mechjack.mechjackbot.feature.shoutout;

import tv.mechjack.mechjackbot.command.shoutout.ProtoMessage.Caster;
import tv.mechjack.mechjackbot.command.shoutout.ProtoMessage.CasterKey;
import tv.mechjack.platform.keyvaluestore.MessageStore;

public interface ShoutOutDataStore extends MessageStore<CasterKey, Caster> {

  CasterKey createCasterKey(String casterName);

  Caster createCaster(String casterName, Long lastShoutOut);

}
