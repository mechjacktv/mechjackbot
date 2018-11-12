package com.mechjacktv.twitchclient;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.*;

import java.util.Optional;
import java.util.Set;

import com.google.common.collect.Sets;

import org.assertj.core.api.SoftAssertions;
import org.junit.Test;

import com.mechjacktv.proto.twitchclient.TwitchClientMessage.User;
import com.mechjacktv.proto.twitchclient.TwitchClientMessage.Users;

public abstract class TwitchClientContractTests {

  private static final TwitchUserFollowsCursor CURSOR = TwitchUserFollowsCursor.of("twitch_cursor");
  private static final TwitchLogin TWITCH_LOGIN = TwitchLogin.of("twitch_login");
  private static final TwitchUserId TWITCH_USER_ID = TwitchUserId.of("twitch_user_id");

  @SuppressWarnings("unchecked")
  private TwitchClient givenASubjectToTest() {
    final TwitchUsersEndpoint twitchUsersEndpoint = mock(TwitchUsersEndpoint.class);
    final TwitchUsersFollowsEndpoint twitchUsersFollowsEndpoint = mock(TwitchUsersFollowsEndpoint.class);

    when(twitchUsersEndpoint.getUsers(isA(Set.class), isA(Set.class))).thenReturn(Users.newBuilder().build());
    return this.givenASubjectToTest(twitchUsersEndpoint, twitchUsersFollowsEndpoint);
  }

  private TwitchClient givenASubjectToTest(final TwitchUsersEndpoint twitchUsersEndpoint) {
    final TwitchUsersFollowsEndpoint twitchUsersFollowsEndpoint = mock(TwitchUsersFollowsEndpoint.class);

    return this.givenASubjectToTest(twitchUsersEndpoint, twitchUsersFollowsEndpoint);
  }

  private TwitchClient givenASubjectToTest(final TwitchUsersFollowsEndpoint twitchUsersFollowsEndpoint) {
    final TwitchUsersEndpoint twitchUsersEndpoint = mock(TwitchUsersEndpoint.class);

    return this.givenASubjectToTest(twitchUsersEndpoint, twitchUsersFollowsEndpoint);
  }

  abstract TwitchClient givenASubjectToTest(TwitchUsersEndpoint twitchUsersEndpoint,
      TwitchUsersFollowsEndpoint twitchUsersFollowsEndpoint);

  @Test
  public final void getUserId_nullLogin_throwsNullPointerExceptionWithMessage() {
    final TwitchClient subjectUnderTest = this.givenASubjectToTest();

    final Throwable thrown = catchThrowable(() -> subjectUnderTest.getUserId(null));

    assertThat(thrown).isInstanceOf(NullPointerException.class).hasMessage("`login` **MUST** not be `null`.");
  }

  @Test
  public final void getUserId_userNotFound_returnsEmptyOptional() {
    final TwitchClient subjectUnderTest = this.givenASubjectToTest();

    final Optional<TwitchUserId> result = subjectUnderTest.getUserId(TWITCH_LOGIN);

    assertThat(result).isEmpty();
  }

  @Test
  @SuppressWarnings("unchecked")
  public final void getUserId_userFound_returnsOptionalWithTwitchUserId() {
    final User user = User.newBuilder().setId(TWITCH_USER_ID.value).setLogin(TWITCH_LOGIN.value).build();
    final TwitchUsersEndpoint twitchUsersEndpoint = mock(TwitchUsersEndpoint.class);
    final TwitchClient subjectUnderTest = this.givenASubjectToTest(twitchUsersEndpoint);
    when(twitchUsersEndpoint.getUsers(isA(Set.class), isA(Set.class))).thenReturn(Users.newBuilder()
        .addUser(user).build());

    final Optional<TwitchUserId> result = subjectUnderTest.getUserId(TWITCH_LOGIN);

    final SoftAssertions softly = new SoftAssertions();

    softly.assertThat(result).isNotEmpty();
    result.ifPresent((twitchUserId) -> softly.assertThat(twitchUserId).isEqualTo(TWITCH_USER_ID));
    softly.assertAll();
  }

  @Test
  public final void getUsers_whenCalled_forwardsToEndpoint() {
    final Set<TwitchLogin> logins = Sets.newHashSet();
    final Set<TwitchUserId> ids = Sets.newHashSet();
    final TwitchUsersEndpoint twitchUsersEndpoint = mock(TwitchUsersEndpoint.class);
    final TwitchClient subjectUnderTest = this.givenASubjectToTest(twitchUsersEndpoint);

    subjectUnderTest.getUsers(logins, ids);

    verify(twitchUsersEndpoint).getUsers(eq(logins), eq(ids));
  }

  @Test
  public final void getUserFollowsFromId_whenCalledWithId_forwardsToEndpoint() {
    final TwitchUsersFollowsEndpoint twitchUsersFollowsEndpoint = mock(TwitchUsersFollowsEndpoint.class);
    final TwitchClient subjectUnderTest = this.givenASubjectToTest(twitchUsersFollowsEndpoint);

    subjectUnderTest.getUserFollowsFromId(TWITCH_USER_ID);

    verify(twitchUsersFollowsEndpoint).getUserFollowsFromId(eq(TWITCH_USER_ID));
  }

  @Test
  public final void getUserFollowsFromId_whenCalledWithIdAndCursor_forwardsToEndpoint() {
    final TwitchUsersFollowsEndpoint twitchUsersFollowsEndpoint = mock(TwitchUsersFollowsEndpoint.class);
    final TwitchClient subjectUnderTest = this.givenASubjectToTest(twitchUsersFollowsEndpoint);

    subjectUnderTest.getUserFollowsFromId(TWITCH_USER_ID, CURSOR);

    verify(twitchUsersFollowsEndpoint).getUserFollowsFromId(eq(TWITCH_USER_ID), eq(CURSOR));
  }

}
