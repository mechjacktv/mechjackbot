package com.mechjacktv.mechjackbot.command.shoutout;

import com.mechjacktv.mechjackbot.ChatBotConfiguration;
import com.mechjacktv.mechjackbot.KeyValueStore;
import com.mechjacktv.mechjackbot.KeyValueStoreFactory;
import com.mechjacktv.mechjackbot.MessageEvent;
import com.mechjacktv.mechjackbot.command.caster.ShoutOutServiceMessage;
import com.mechjacktv.twitchclient.TwitchClient;
import com.mechjacktv.twitchclient.TwitchClientMessage;
import com.mechjacktv.util.ProtobufUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public final class ShoutOutService {

  private static final Logger log = LoggerFactory.getLogger(ShoutOutService.class);
  private static final long TWELVE_HOURS = 1000 * 60 * 60 * 12;

  private final KeyValueStore casters;
  private final ProtobufUtils protobufUtils;

  @Inject
  public ShoutOutService(final ChatBotConfiguration chatBotConfiguration,
                         final KeyValueStoreFactory keyValueStoreFactory,
                         final ProtobufUtils protobufUtils,
                         final TwitchClient twitchClient) {
    final KeyValueStore casters = keyValueStoreFactory.createOrOpenKeyValueStore("casters");
    this.protobufUtils = protobufUtils;
    this.casters = this.updatedCasters(chatBotConfiguration, twitchClient, casters);
  }

  private KeyValueStore updatedCasters(final ChatBotConfiguration chatBotConfiguration,
                                       final TwitchClient twitchClient,
                                       final KeyValueStore casters) {
    final String channel = chatBotConfiguration.getTwitchChannel();
    final Optional<String> casterId = twitchClient.getUserId(channel);

    if (casterId.isPresent()) {
      final Collection<ShoutOutServiceMessage.CasterKey> exitingCasterKey =
          this.protobufUtils.parseAllMessages(ShoutOutServiceMessage.CasterKey.class, casters.getKeys());
      final Set<TwitchClientMessage.UserFollow> userFollows = this.getUserFollows(casterId.get(), twitchClient);
      int addCount = 0;
      int removeCount = 0;

      for (final TwitchClientMessage.UserFollow userFollow : userFollows) {
        final ShoutOutServiceMessage.CasterKey casterKey = this.createCasterKey(userFollow.getToName());
        final byte[] casterKeyBytes = casterKey.toByteArray();

        if (casters.containsKey(casterKeyBytes)) {
          exitingCasterKey.remove(casterKey);
        } else {
          casters.put(casterKeyBytes, this.createCaster(userFollow.getToName(), 0L).toByteArray());
          addCount++;
        }
      }
      for (final ShoutOutServiceMessage.CasterKey casterKey : exitingCasterKey) {
        casters.remove(casterKey.toByteArray());
        removeCount++;
      }
      log.info(String.format("Channel, %s, is following %d casters (%d added, %d removed)",
          channel, userFollows.size(), addCount, removeCount));
    } else {
      log.warn(String.format("Failed to find Twitch id for Twitch login, %s. Using existing data", channel));
    }
    return casters;
  }

  private Set<TwitchClientMessage.UserFollow> getUserFollows(final String casterId, final TwitchClient twitchClient) {
    TwitchClientMessage.UserFollows userFollows = twitchClient.getUserFollowsFromId(casterId);
    final Set<TwitchClientMessage.UserFollow> userFollowsList = new HashSet<>(userFollows.getUserFollowList());
    int lastSize = 0;

    while (userFollowsList.size() < userFollows.getTotalFollows() && lastSize != userFollowsList.size()) {
      userFollows = twitchClient.getUserFollowsFromId(casterId, userFollows.getCursor());
      userFollowsList.addAll(userFollows.getUserFollowList());
      lastSize = userFollowsList.size();
    }
    return userFollowsList;
  }

  private ShoutOutServiceMessage.CasterKey createCasterKey(final String casterName) {
    final ShoutOutServiceMessage.CasterKey.Builder builder = ShoutOutServiceMessage.CasterKey.newBuilder();

    return builder.setName(casterName)
        .build();
  }

  private ShoutOutServiceMessage.Caster createCaster(final String casterName, final Long lastShoutOut) {
    final ShoutOutServiceMessage.Caster.Builder builder = ShoutOutServiceMessage.Caster.newBuilder();

    return builder.setName(casterName)
        .setLastShoutOut(lastShoutOut)
        .build();
  }

  final boolean isCasterDue(final String casterName) {
    final ShoutOutServiceMessage.CasterKey casterKey = this.createCasterKey(casterName);
    final Optional<byte[]> casterBytes = this.casters.get(casterKey.toByteArray());

    if (casterBytes.isPresent()) {
      final long now = System.currentTimeMillis();
      final ShoutOutServiceMessage.Caster caster =
          this.protobufUtils.parseMessage(ShoutOutServiceMessage.Caster.class, casterBytes.get());

      return now - caster.getLastShoutOut() > TWELVE_HOURS;
    }
    return false;
  }

  final void removeCaster(final String casterName) {
    final ShoutOutServiceMessage.CasterKey casterKey = this.createCasterKey(casterName);

    this.casters.remove(casterKey.toByteArray());
  }

  final void sendCasterShoutOut(final MessageEvent messageEvent, final String casterName) {
    messageEvent.sendResponse(String.format("Fellow caster in the stream! " +
            "Everyone, please give a warm welcome to @%s. " +
            "It would be great if you checked them out " +
            "and gave them a follow. https://twitch.tv/%s",
        casterName, casterName));
    this.setCaster(casterName, System.currentTimeMillis());
  }

  final void setCaster(final String casterName, final long lastShoutOut) {
    final ShoutOutServiceMessage.CasterKey casterKey = this.createCasterKey(casterName);
    final ShoutOutServiceMessage.Caster caster = this.createCaster(casterName, lastShoutOut);

    this.casters.put(casterKey.toByteArray(), caster.toByteArray());
  }

}
