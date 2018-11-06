package com.mechjacktv.mechjackbot.command.shoutout;

import java.util.Optional;

import javax.inject.Inject;

import com.mechjacktv.mechjackbot.ChatUsername;
import com.mechjacktv.mechjackbot.Message;
import com.mechjacktv.mechjackbot.MessageEvent;
import com.mechjacktv.mechjackbot.command.shoutout.ShoutOutServiceMessage.Caster;
import com.mechjacktv.mechjackbot.command.shoutout.ShoutOutServiceMessage.CasterKey;

public final class ShoutOutService {

  private static final long ONE_HOUR = 1000 * 60 * 60;

  private final ShoutOutDataStore shoutOutDataStore;

  @Inject
  public ShoutOutService(final ShoutOutDataStore shoutOutMessageStore) {
    this.shoutOutDataStore = shoutOutMessageStore;
  }

  final boolean isCasterDue(final ChatUsername casterUsername) {
    final CasterKey casterKey = this.shoutOutDataStore.createCasterKey(casterUsername.value);
    final Optional<Caster> caster = this.shoutOutDataStore.get(casterKey);

    return caster.filter(c -> System.currentTimeMillis() - c.getLastShoutOut() > ONE_HOUR).isPresent();
  }

  final void sendCasterShoutOut(final MessageEvent messageEvent, final ChatUsername casterUsername) {
    messageEvent.sendResponse(Message.of(String.format("Fellow caster in the stream! " +
            "Everyone, please give a warm welcome to @%s. " +
            "It would be great if you checked them out " +
            "and gave them a follow. https://twitch.tv/%s",
            casterUsername, casterUsername)));
    this.shoutOutDataStore.put(this.shoutOutDataStore.createCasterKey(casterUsername.value),
            this.shoutOutDataStore.createCaster(casterUsername.value, System.currentTimeMillis()));
  }

}
