package tv.mechjack.mechjackbot.command.shoutout;

import tv.mechjack.keyvaluestore.MessageStore;
import tv.mechjack.proto.mechjackbot.command.shoutout.ShoutOutServiceMessage.Caster;
import tv.mechjack.proto.mechjackbot.command.shoutout.ShoutOutServiceMessage.CasterKey;

public interface ShoutOutDataStore extends MessageStore<CasterKey, Caster> {

  CasterKey createCasterKey(String casterName);

  Caster createCaster(String casterName, Long lastShoutOut);

}
