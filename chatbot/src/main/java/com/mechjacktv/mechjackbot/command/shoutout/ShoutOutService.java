package com.mechjacktv.mechjackbot.command.shoutout;

import com.mechjacktv.mechjackbot.MessageEvent;
import com.mechjacktv.mechjackbot.command.caster.ShoutOutServiceMessage.Caster;
import com.mechjacktv.mechjackbot.command.caster.ShoutOutServiceMessage.CasterKey;

import javax.inject.Inject;
import java.util.Optional;

public final class ShoutOutService {

  private static final long TWELVE_HOURS = 1000 * 60 * 60 * 12;

  private final ShoutOutDataStore shoutOutDataStore;

  @Inject
  public ShoutOutService(final ShoutOutDataStore shoutOutMessageStore) {
    this.shoutOutDataStore = shoutOutMessageStore;
  }

  final boolean isCasterDue(final String casterName) {
    final CasterKey casterKey = this.shoutOutDataStore.createCasterKey(casterName);
    final Optional<Caster> caster = this.shoutOutDataStore.get(casterKey);

    return caster.filter(c -> System.currentTimeMillis() - c.getLastShoutOut() > TWELVE_HOURS).isPresent();
  }

  final void sendCasterShoutOut(final MessageEvent messageEvent, final String casterName) {
    messageEvent.sendResponse(String.format("Fellow caster in the stream! " +
            "Everyone, please give a warm welcome to @%s. " +
            "It would be great if you checked them out " +
            "and gave them a follow. https://twitch.tv/%s",
        casterName, casterName));
    this.shoutOutDataStore.put(this.shoutOutDataStore.createCasterKey(casterName),
        this.shoutOutDataStore.createCaster(casterName, System.currentTimeMillis()));
  }

}
