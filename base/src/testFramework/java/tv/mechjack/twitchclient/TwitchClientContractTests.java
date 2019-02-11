package tv.mechjack.twitchclient;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

import com.google.common.collect.Sets;

import org.assertj.core.api.Condition;
import org.junit.Rule;
import org.junit.Test;

import tv.mechjack.testframework.TestFramework;
import tv.mechjack.twitchclient.ProtoMessage.User;
import tv.mechjack.twitchclient.ProtoMessage.UserFollows;
import tv.mechjack.twitchclient.ProtoMessage.Users;

public abstract class TwitchClientContractTests {

  @Rule
  public final TestFramework testFrameworkRule = new TestFramework();

  protected void installModules() {
    this.testFrameworkRule.registerModule(new TestTwitchClientModule());
  }

  protected abstract TwitchClient givenASubjectToTest();

  @Test
  public final void getUserId_nullLogin_throwsNullPointerExceptionWithMessage() {
    this.installModules();
    final TwitchClient subjectUnderTest = this.givenASubjectToTest();

    final Throwable thrown = catchThrowable(() -> subjectUnderTest.getUserId(null));

    assertThat(thrown).isInstanceOf(NullPointerException.class).hasMessage("`login` **MUST** not be `null`.");
  }

  @Test
  public final void getUserId_userNotFound_returnsEmptyOptional() {
    this.installModules();
    final TwitchClient subjectUnderTest = this.givenASubjectToTest();

    final Optional<TwitchUserId> result = subjectUnderTest
        .getUserId(TwitchLogin.of(this.testFrameworkRule.arbitraryData().getString()));

    assertThat(result).isEmpty();
  }

  @Test
  public final void getUserId_userFound_returnsOptionalWithTwitchUserId() {
    this.installModules();
    final TwitchLogin twitchLogin = TwitchLogin.of(this.testFrameworkRule.arbitraryData().getString());
    final TwitchUserId twitchUserId = TwitchUserId.of(this.testFrameworkRule.arbitraryData().getString());
    final TestTwitchUsersEndpoint twitchUsersEndpoint = this.testFrameworkRule
        .getInstance(TestTwitchUsersEndpoint.class);
    twitchUsersEndpoint.setGetUsersHandler((twitchLogins, twitchUserIds) -> Users.newBuilder()
        .addUser(User.newBuilder().setId(twitchUserId.value).setLogin(twitchLogin.value).build()).build());
    final TwitchClient subjectUnderTest = this.givenASubjectToTest();

    final Optional<TwitchUserId> result = subjectUnderTest.getUserId(twitchLogin);

    assertThat(result).is(new Condition<>(actual -> twitchUserId.equals(actual.orElse(null)),
        String.format("actual, %s, is not equal to expected, %s", result.orElse(null), twitchUserId)));
  }

  @Test
  public final void getUsers_whenCalled_forwardsToEndpoint() {
    this.installModules();
    final AtomicBoolean wasCalled = new AtomicBoolean(false);
    final TestTwitchUsersEndpoint twitchUsersEndpoint = this.testFrameworkRule
        .getInstance(TestTwitchUsersEndpoint.class);
    twitchUsersEndpoint.setGetUsersHandler((logins, userIds) -> {
      wasCalled.set(true);
      return Users.getDefaultInstance();
    });
    final TwitchClient subjectUnderTest = this.givenASubjectToTest();

    subjectUnderTest.getUsers(Sets.newHashSet(), Sets.newHashSet());

    assertThat(wasCalled).isTrue();
  }

  @Test
  public final void getUserFollowsFromId_whenCalledWithId_forwardsToEndpoint() {
    this.installModules();
    final AtomicBoolean wasCalled = new AtomicBoolean(false);
    final TestTwitchUsersFollowsEndpoint twitchUsersFollowsEndpoint = this.testFrameworkRule
        .getInstance(TestTwitchUsersFollowsEndpoint.class);
    twitchUsersFollowsEndpoint.setGetUserFollowsFromIdHandler(fromId -> {
      wasCalled.set(true);
      return UserFollows.getDefaultInstance();
    });
    final TwitchClient subjectUnderTest = this.givenASubjectToTest();

    subjectUnderTest.getUserFollowsFromId(TwitchUserId.of(this.testFrameworkRule.arbitraryData().getString()));

    assertThat(wasCalled).isTrue();
  }

  @Test
  public final void getUserFollowsFromId_whenCalledWithIdAndCursor_forwardsToEndpoint() {
    this.installModules();
    final AtomicBoolean wasCalled = new AtomicBoolean(false);
    final TestTwitchUsersFollowsEndpoint twitchUsersFollowsEndpoint = this.testFrameworkRule
        .getInstance(TestTwitchUsersFollowsEndpoint.class);
    twitchUsersFollowsEndpoint.setGetUserFollowsFromIdWithCursorHandler((fromId, cursor) -> {
      wasCalled.set(true);
      return UserFollows.getDefaultInstance();
    });
    final TwitchClient subjectUnderTest = this.givenASubjectToTest();

    subjectUnderTest.getUserFollowsFromId(TwitchUserId.of(this.testFrameworkRule.arbitraryData().getString()),
        TwitchUserFollowsCursor.of(this.testFrameworkRule.arbitraryData().getString()));

    assertThat(wasCalled).isTrue();
  }

}
