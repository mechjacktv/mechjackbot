package com.mechjacktv.mechjackbot.command.shoutout;

import java.util.Optional;

import javax.inject.Inject;

import com.mechjacktv.mechjackbot.AppConfiguration;
import com.mechjacktv.mechjackbot.ChatUsername;
import com.mechjacktv.mechjackbot.Message;
import com.mechjacktv.mechjackbot.MessageEvent;
import com.mechjacktv.proto.mechjackbot.command.shoutout.ShoutOutServiceMessage.Caster;
import com.mechjacktv.proto.mechjackbot.command.shoutout.ShoutOutServiceMessage.CasterKey;
import com.mechjacktv.util.TimeUtils;

public final class ShoutOutService {

  private static final String FREQUENCY_KEY = "command.shout_out.frequency.hours";

  private final AppConfiguration appConfiguration;
  private final ShoutOutDataStore shoutOutDataStore;
  private final TimeUtils timeUtils;

  @Inject
  public ShoutOutService(final AppConfiguration appConfiguration, final ShoutOutDataStore shoutOutMessageStore,
      final TimeUtils timeUtils) {
    this.appConfiguration = appConfiguration;
    this.shoutOutDataStore = shoutOutMessageStore;
    this.timeUtils = timeUtils;
  }

  final boolean isCasterDue(final ChatUsername casterUsername) {
    final CasterKey casterKey = this.shoutOutDataStore.createCasterKey(casterUsername.value);
    final Optional<Caster> caster = this.shoutOutDataStore.get(casterKey);
    final Long frequency = this.timeUtils.hoursAsMs(
        Integer.parseInt(this.appConfiguration.get(FREQUENCY_KEY, "1")));

    return caster.filter(c -> System.currentTimeMillis() - c.getLastShoutOut() > frequency).isPresent();
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
