package tv.mechjack.twitchclient;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

import java.io.Reader;
import java.io.StringReader;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.assertj.core.api.SoftAssertions;
import org.junit.Rule;
import org.junit.Test;

import tv.mechjack.platform.utils.function.ConsumerWithException;
import tv.mechjack.testframework.TestFramework;
import tv.mechjack.testframework.fake.ArgumentCaptor;
import tv.mechjack.testframework.fake.FakeBuilder;
import tv.mechjack.testframework.fake.FakeFactory;
import tv.mechjack.testframework.fake.InvocationCounter;
import tv.mechjack.twitchclient.ProtoMessage.UserFollow;
import tv.mechjack.twitchclient.ProtoMessage.UserFollows;
import tv.mechjack.twitchclient.messageadapter.UserFollowMessageTypeAdapter;

public abstract class TwitchUsersFollowsEndpointContractTests {

  @Rule
  public final TestFramework testFrameworkRule = new TestFramework();

  private static final String CURSOR = "CURSOR_CURSOR_CURSOR_CURSOR";
  private static final String FOLLOWED_AT = "2018-10-30T21:47:09Z";
  private static final String FROM_ID = "123456789";
  private static final String FROM_NAME = "TestFrom";
  private static final String TO_ID = "987654321";
  private static final String TO_NAME = "TestTo";
  private static final String UNKNOWN_DATA = "unknown";
  private static final String RESPONSE_BODY = "{\n" +
      "    \"total\": 1,\n" +
      "    \"data\": [\n" +
      "        {\n" +
      "            \"from_id\": \"" + FROM_ID + "\",\n" +
      "            \"from_name\": \"" + FROM_NAME + "\",\n" +
      "            \"to_id\": \"" + TO_ID + "\",\n" +
      "            \"to_name\": \"" + TO_NAME + "\",\n" +
      "            \"followed_at\": \"" + FOLLOWED_AT + "\"\n" +
      "        }\n" +
      "    ],\n" +
      "    \"pagination\": {\n" +
      "        \"cursor\": \"" + CURSOR + "\",\n" +
      "        \"" + UNKNOWN_DATA + "\": \"" + UNKNOWN_DATA + "\"\n" +
      "    },\n" +
      "  \"" + UNKNOWN_DATA + "\": \"" + UNKNOWN_DATA + "\"\n" +
      "}\n";

  protected abstract TwitchUsersFollowsEndpoint givenASubjectToTest(Gson gson, TwitchClientUtils twitchClientUtils);

  private Gson givenAGson() {
    final GsonBuilder gsonBuilder = new GsonBuilder();

    gsonBuilder.registerTypeAdapter(UserFollow.class, new UserFollowMessageTypeAdapter());
    return gsonBuilder.create();
  }

  @Test
  public final void getUserFollowsFromId_nullFromId_throwsNullPointerExceptionWithMessage() {
    final FakeFactory fakeFactory = this.testFrameworkRule.getInstance(FakeFactory.class);
    final TwitchUsersFollowsEndpoint subjectUnderTest = this.givenASubjectToTest(this.givenAGson(),
        fakeFactory.fake(TwitchClientUtils.class));

    final Throwable thrown = catchThrowable(() -> subjectUnderTest.getUserFollowsFromId(null));

    assertThat(thrown).isInstanceOf(NullPointerException.class).hasMessageContaining("**MUST** not be `null`");
  }

  @Test
  public final void getUserFollowsFromId_forFromId_returnsListOfUsersFollowed() {
    final FakeBuilder<TwitchClientUtils> fakeBuilder = this.testFrameworkRule.fakeBuilder(TwitchClientUtils.class);
    fakeBuilder.forMethod("handleResponse", new Class[] { TwitchUrl.class, ConsumerWithException.class })
        .setHandler(invocation -> {
          final ConsumerWithException<Reader> consumer = invocation.getArgument(1);

          consumer.accept(new StringReader(RESPONSE_BODY));
          return null;
        });
    final TwitchClientUtils twitchClientUtils = fakeBuilder.build();
    final TwitchUsersFollowsEndpoint subjectUnderTest = this.givenASubjectToTest(this.givenAGson(), twitchClientUtils);

    final UserFollows result = subjectUnderTest.getUserFollowsFromId(TwitchUserId.of(FROM_NAME));

    final SoftAssertions softly = new SoftAssertions();
    softly.assertThat(result).isNotNull();
    softly.assertThat(result.getTotalFollows()).isEqualTo(1);
    softly.assertThat(result.getUserFollowCount()).isEqualTo(1);
    softly.assertThat(result.getUserFollow(0).getFromId()).isEqualTo(FROM_ID);
    softly.assertThat(result.getUserFollow(0).getFromName()).isEqualTo(FROM_NAME);
    softly.assertThat(result.getUserFollow(0).getToId()).isEqualTo(TO_ID);
    softly.assertThat(result.getUserFollow(0).getToName()).isEqualTo(TO_NAME);
    softly.assertThat(result.getUserFollow(0).getFollowedAt()).isEqualTo(FOLLOWED_AT);
    softly.assertThat(result.getCursor()).isEqualTo(CURSOR);
    softly.assertAll();
  }

  @Test
  public final void getUserFollowsFromId_forFromId_requestsFollowsForId() {
    final FakeBuilder<TwitchClientUtils> fakeBuilder = this.testFrameworkRule.fakeBuilder(TwitchClientUtils.class);
    final ArgumentCaptor capturingHandler = new ArgumentCaptor(0, invocation -> {
      final ConsumerWithException<Reader> consumer = invocation.getArgument(1);

      consumer.accept(new StringReader(RESPONSE_BODY));
      return null;
    });
    fakeBuilder.forMethod("handleResponse", new Class[] { TwitchUrl.class, ConsumerWithException.class })
        .setHandler(capturingHandler);
    final TwitchClientUtils twitchClientUtils = fakeBuilder.build();
    final TwitchUsersFollowsEndpoint subjectUnderTest = this.givenASubjectToTest(this.givenAGson(), twitchClientUtils);

    subjectUnderTest.getUserFollowsFromId(TwitchUserId.of(FROM_ID));
    final TwitchUrl result = capturingHandler.getValue();

    assertThat(result.value).contains("from_id=" + FROM_ID);
  }

  @Test
  public final void getUserFollowsFromId_forFromIdAndCursor_requestsFollowsForIdAndCursor() {
    final FakeBuilder<TwitchClientUtils> fakeBuilder = this.testFrameworkRule.fakeBuilder(TwitchClientUtils.class);
    final ArgumentCaptor capturingHandler = new ArgumentCaptor(0, invocation -> {
      final ConsumerWithException<Reader> consumer = invocation.getArgument(1);

      consumer.accept(new StringReader(RESPONSE_BODY));
      return null;
    });
    fakeBuilder.forMethod("handleResponse", new Class[] { TwitchUrl.class, ConsumerWithException.class })
        .setHandler(capturingHandler);
    final TwitchClientUtils twitchClientUtils = fakeBuilder.build();
    final TwitchUsersFollowsEndpoint subjectUnderTest = this.givenASubjectToTest(this.givenAGson(), twitchClientUtils);

    subjectUnderTest.getUserFollowsFromId(TwitchUserId.of(FROM_ID), TwitchUserFollowsCursor.of(CURSOR));
    final TwitchUrl result = capturingHandler.getValue();

    final SoftAssertions softly = new SoftAssertions();
    softly.assertThat(result.value).contains("from_id=" + FROM_ID);
    softly.assertThat(result.value).contains("after=" + CURSOR);
    softly.assertAll();
  }

  @Test
  public final void getUsers_invalidObjectNames_handlesInvalidObjectName() {
    final FakeFactory fakeFactory = this.testFrameworkRule.getInstance(FakeFactory.class);
    final FakeBuilder<TwitchClientUtils> fakeBuilder = fakeFactory.builder(TwitchClientUtils.class);
    final InvocationCounter countingHandler = new InvocationCounter();
    fakeBuilder.forMethod("handleUnknownObjectName", new Class[] { String.class }).setHandler(countingHandler);
    fakeBuilder.forMethod("handleResponse", new Class[] { TwitchUrl.class, ConsumerWithException.class })
        .setHandler(invocation -> {
          final ConsumerWithException<Reader> consumer = invocation.getArgument(1);

          consumer.accept(new StringReader(RESPONSE_BODY));
          return null;
        });
    final TwitchClientUtils twitchClientUtils = fakeBuilder.build();
    final TwitchUsersFollowsEndpoint subjectUnderTest = this.givenASubjectToTest(this.givenAGson(),
        twitchClientUtils);

    subjectUnderTest.getUserFollowsFromId(TwitchUserId.of(FROM_ID));

    assertThat(countingHandler.getCallCount()).isEqualTo(2);
  }

}
