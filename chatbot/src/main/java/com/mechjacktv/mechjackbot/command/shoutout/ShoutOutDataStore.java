package com.mechjacktv.mechjackbot.command.shoutout;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mechjacktv.keyvaluestore.AbstractMessageStore;
import com.mechjacktv.keyvaluestore.KeyValueStoreFactory;
import com.mechjacktv.mechjackbot.ChatBotConfiguration;
import com.mechjacktv.mechjackbot.command.shoutout.ShoutOutServiceMessage.Caster;
import com.mechjacktv.mechjackbot.command.shoutout.ShoutOutServiceMessage.CasterKey;
import com.mechjacktv.twitchclient.TwitchClient;
import com.mechjacktv.twitchclient.TwitchClientMessage.UserFollow;
import com.mechjacktv.twitchclient.TwitchClientMessage.UserFollows;
import com.mechjacktv.twitchclient.TwitchLogin;
import com.mechjacktv.twitchclient.TwitchUserFollowsCursor;
import com.mechjacktv.twitchclient.TwitchUserId;
import com.mechjacktv.util.ProtobufUtils;
import com.mechjacktv.util.scheduleservice.ScheduleService;

public final class ShoutOutDataStore extends AbstractMessageStore<CasterKey, Caster> {

  private static final Logger log = LoggerFactory.getLogger(ShoutOutDataStore.class);
  private static final String KEY_VALUE_STORE_NAME = ShoutOutDataStore.class.getCanonicalName();

  @Inject
  ShoutOutDataStore(final ChatBotConfiguration chatBotConfiguration,
      final KeyValueStoreFactory keyValueStoreFactory,
      final ProtobufUtils protobufUtils,
      final ScheduleService scheduleService,
      final TwitchClient twitchClient) {
    super(keyValueStoreFactory.createOrOpenKeyValueStore(KEY_VALUE_STORE_NAME), protobufUtils);
    scheduleService.schedule(() -> this.updateCasters(chatBotConfiguration, twitchClient), 10, TimeUnit.MINUTES);
  }

  private void updateCasters(final ChatBotConfiguration chatBotConfiguration,
      final TwitchClient twitchClient) {
    try {
      final String channel = chatBotConfiguration.getTwitchChannel().value;
      final Optional<TwitchUserId> casterId = twitchClient.getUserId(TwitchLogin.of(channel));

      if (casterId.isPresent()) {
        final Collection<CasterKey> existingCasterKeys = this.getKeys();
        final int addCount = this.addCasters(casterId.get(), existingCasterKeys, twitchClient);
        final int removeCount = this.removeCasters(existingCasterKeys);

        log.info(String.format("Channel, %s, is following %d casters (%d added, %d removed)",
            channel, this.getKeys().size(), addCount, removeCount));
      } else {
        log.warn(String.format("Failed to find Twitch id for Twitch login, %s. Using existing data", channel));
      }
    } catch (final Throwable t) {
      log.error("Failure while updating caster shout out data store", t);
    }
  }

  private int addCasters(final TwitchUserId casterId,
      final Collection<CasterKey> existingCasterKeys,
      final TwitchClient twitchClient) {
    final Set<UserFollow> userFollows = this.getUserFollows(casterId, twitchClient);
    int addCount = 0;

    for (final UserFollow userFollow : userFollows) {
      final CasterKey casterKey = this.createCasterKey(userFollow.getToName());

      if (this.containsKey(casterKey)) {
        existingCasterKeys.remove(casterKey);
      } else {
        this.put(casterKey, this.createCaster(userFollow.getToName(), 0L));
        addCount++;
      }
    }
    return addCount;
  }

  private int removeCasters(final Collection<CasterKey> existingCasterKeys) {
    int removeCount = 0;

    for (final CasterKey casterKey : existingCasterKeys) {
      this.remove(casterKey);
      removeCount++;
    }
    return removeCount;
  }

  private Set<UserFollow> getUserFollows(final TwitchUserId casterId, final TwitchClient twitchClient) {
    UserFollows userFollows = twitchClient.getUserFollowsFromId(casterId);
    final Set<UserFollow> userFollowsList = new HashSet<>(userFollows.getUserFollowList());
    int lastSize = 0;

    while (userFollowsList.size() < userFollows.getTotalFollows() && lastSize != userFollowsList.size()) {
      userFollows = twitchClient.getUserFollowsFromId(casterId, TwitchUserFollowsCursor.of(userFollows.getCursor()));
      userFollowsList.addAll(userFollows.getUserFollowList());
      lastSize = userFollowsList.size();
    }
    return userFollowsList;
  }

  final CasterKey createCasterKey(final String casterName) {
    final CasterKey.Builder builder = CasterKey.newBuilder();

    return builder.setName(casterName.toLowerCase())
        .build();
  }

  final Caster createCaster(final String casterName, final Long lastShoutOut) {
    final Caster.Builder builder = Caster.newBuilder();

    return builder.setName(casterName)
        .setLastShoutOut(lastShoutOut)
        .build();
  }

  @Override
  protected Class<CasterKey> getKeyClass() {
    return CasterKey.class;
  }

  @Override
  protected Class<Caster> getValueClass() {
    return Caster.class;
  }

}
