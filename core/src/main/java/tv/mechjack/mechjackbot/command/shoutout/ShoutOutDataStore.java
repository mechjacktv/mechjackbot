package tv.mechjack.mechjackbot.command.shoutout;

import tv.mechjack.keyvaluestore.MessageStore;
import tv.mechjack.mechjackbot.command.shoutout.ProtoMessage.Caster;
import tv.mechjack.mechjackbot.command.shoutout.ProtoMessage.CasterKey;

public interface ShoutOutDataStore extends MessageStore<CasterKey, Caster> {

  CasterKey createCasterKey(String casterName);

  Caster createCaster(String casterName, Long lastShoutOut);

}
