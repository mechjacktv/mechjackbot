package tv.mechjack.mechjackbot.feature.linkmoderation;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import tv.mechjack.mechjackbot.api.ChatUser;
import tv.mechjack.mechjackbot.api.UserRole;
import tv.mechjack.platform.utils.TimeUtils;
import tv.mechjack.twitchclient.TwitchLogin;

public class DefaultLinkModeratorService implements LinkModeratorService {

  // twitchLogin, expires
  private final Map<TwitchLogin, Long> permits = new ConcurrentHashMap<>();

  private final Map<TwitchLogin, Integer> lastActionDuration = new ConcurrentHashMap<>();
  private final Map<TwitchLogin, Long> lastActionTaken = new ConcurrentHashMap<>();

  private final TimeUtils timeUtils;

  @Inject
  DefaultLinkModeratorService(final TimeUtils timeUtils) {
    this.timeUtils = timeUtils;
  }

  @Override
  public void grantPermit(final TwitchLogin twitchLogin, long duration) {
    this.permits.put(twitchLogin, System.currentTimeMillis() + duration);
    this.lastActionTaken.remove(twitchLogin);
  }

  @Override
  public boolean hasPermit(final ChatUser chatUser) {
    final TwitchLogin twitchLogin = chatUser.getTwitchLogin();

    if (chatUser.hasAccessLevel(UserRole.SUBSCRIBER)) {
      return true;
    } else if (this.permits.containsKey(twitchLogin)) {
      return this.timeUtils.currentTime() <= this.permits.get(twitchLogin);
    }
    return false;
  }

  @Override
  public int timeoutDuration(final ChatUser chatUser) {
    final TwitchLogin twitchLogin = chatUser.getTwitchLogin();
    final Long lastAction = this.lastActionTaken.get(twitchLogin);

    try {
      if (Objects.nonNull(lastAction)) {
        if (this.timeUtils.currentTime() > lastAction + TimeUnit.HOURS.toMillis(24)) {
          this.lastActionDuration.remove(twitchLogin);
        } else {
          final int duration = this.lastActionDuration.get(twitchLogin) * 2;

          this.lastActionDuration.put(twitchLogin, duration);
          return duration;
        }
      }
      this.lastActionDuration.put(twitchLogin, 1);
      return 1;
    } finally {
      this.lastActionTaken.put(twitchLogin, this.timeUtils.currentTime());
    }
  }

}
