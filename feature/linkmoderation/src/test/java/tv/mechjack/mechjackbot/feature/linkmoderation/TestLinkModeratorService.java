package tv.mechjack.mechjackbot.feature.linkmoderation;

import java.util.Objects;

import tv.mechjack.mechjackbot.api.ChatUser;
import tv.mechjack.twitchclient.TwitchLogin;

public class TestLinkModeratorService implements LinkModeratorService {

  private int duration = 1;
  private TwitchLogin grantee;

  @Override
  public void grantPermit(final TwitchLogin twitchLogin, final long duration) {
    this.grantee = twitchLogin;
  }

  @Override
  public boolean hasPermit(final ChatUser chatUser) {
    return Objects.nonNull(this.grantee)
        && this.grantee.equals(chatUser.getTwitchLogin());
  }

  @Override
  public int timeoutDuration(final ChatUser chatUser) {
    return this.duration;
  }

  public void setTimeoutDuration(final int duration) {
    this.duration = duration;
  }

}
