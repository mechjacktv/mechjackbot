package com.mechjacktv.twitchclient;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;

import java.io.Reader;
import java.io.StringReader;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.assertj.core.api.SoftAssertions;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import com.mechjacktv.twitchclient.TwitchClientMessage.UserFollow;
import com.mechjacktv.twitchclient.TwitchClientMessage.UserFollows;
import com.mechjacktv.twitchclient.messageadapter.UserFollowMessageTypeAdapter;
import com.mechjacktv.util.function.ConsumerWithException;

public abstract class TwitchUsersFollowsEndpointContractTests {

  private static final String CURSOR = "eyJiIjpudWxsLCJhIjp7IkN1cnNvciI6IjE1NDA2MTE0NzA2NjU2ODM4NDUifX0";
  private static final String FOLLOWED_AT = "2018-10-30T21:47:09Z";
  private static final String FROM_ID = "123456789";
  private static final String FROM_NAME = "TestFrom";
  private static final String TO_ID = "987654321";
  private static final String TO_NAME = "TestTo";
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
          "        \"cursor\": \"" + CURSOR + "\"\n" +
          "    }\n" +
          "}\n";

  abstract TwitchUsersFollowsEndpoint givenASubjectToTest(Gson gson, TwitchClientUtils twitchClientUtils);

  private Gson givenAGson() {
    final GsonBuilder gsonBuilder = new GsonBuilder();

    gsonBuilder.registerTypeAdapter(UserFollow.class, new UserFollowMessageTypeAdapter());
    return gsonBuilder.create();
  }

  @Test
  public final void getUserFollowsFromId_nullFromId_throwsNullPointerExceptionWithMessage() {
    final TwitchUsersFollowsEndpoint subjectUnderTest = this.givenASubjectToTest(this.givenAGson(),
            mock(TwitchClientUtils.class));

    final Throwable thrown = catchThrowable(() -> subjectUnderTest.getUserFollowsFromId(null));

    assertThat(thrown).isInstanceOf(NullPointerException.class).hasMessageContaining("**MUST** not be `null`");
  }

  @Test
  @SuppressWarnings("unchecked")
  public final void getUserFollowsFromId_forFromId_returnsListOfUsersFollowed() {
    final TwitchClientUtils twitchClientUtils = mock(TwitchClientUtils.class);
    final TwitchUsersFollowsEndpoint subjectUnderTest = this.givenASubjectToTest(this.givenAGson(),
            twitchClientUtils);
    doAnswer((invocation) -> {
      final ConsumerWithException<Reader> consumer = invocation.getArgument(1);

      consumer.accept(new StringReader(RESPONSE_BODY));
      return null;
    }).when(twitchClientUtils).handleResponse(isA(TwitchUrl.class), isA(ConsumerWithException.class));

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
  @SuppressWarnings("unchecked")
  public final void getUserFollowsFromId_forFromId_requestsFollowsForId() {
    final TwitchClientUtils twitchClientUtils = mock(TwitchClientUtils.class);
    final TwitchUsersFollowsEndpoint subjectUnderTest = this.givenASubjectToTest(this.givenAGson(),
            twitchClientUtils);
    final ArgumentCaptor<TwitchUrl> serviceUrl = ArgumentCaptor.forClass(TwitchUrl.class);
    doAnswer((invocation) -> {
      final ConsumerWithException<Reader> consumer = invocation.getArgument(1);

      consumer.accept(new StringReader(RESPONSE_BODY));
      return null;
    }).when(twitchClientUtils).handleResponse(serviceUrl.capture(), isA(ConsumerWithException.class));

    subjectUnderTest.getUserFollowsFromId(TwitchUserId.of(FROM_ID));

    assertThat(serviceUrl.getValue().value).contains("from_id=" + FROM_ID);
  }

  @Test
  @SuppressWarnings("unchecked")
  public final void getUserFollowsFromId_forFromIdAndCursor_requestsFollowsForIdAndCursor() {
    final TwitchClientUtils twitchClientUtils = mock(TwitchClientUtils.class);
    final TwitchUsersFollowsEndpoint subjectUnderTest = this.givenASubjectToTest(this.givenAGson(),
            twitchClientUtils);
    final ArgumentCaptor<TwitchUrl> serviceUrl = ArgumentCaptor.forClass(TwitchUrl.class);
    doAnswer((invocation) -> {
      final ConsumerWithException<Reader> consumer = invocation.getArgument(1);

      consumer.accept(new StringReader(RESPONSE_BODY));
      return null;
    }).when(twitchClientUtils).handleResponse(serviceUrl.capture(), isA(ConsumerWithException.class));

    subjectUnderTest.getUserFollowsFromId(TwitchUserId.of(FROM_ID), TwitchUserFollowsCursor.of(CURSOR));

    final SoftAssertions softly = new SoftAssertions();
    softly.assertThat(serviceUrl.getValue().value).contains("from_id=" + FROM_ID);
    softly.assertThat(serviceUrl.getValue().value).contains("after=" + CURSOR);
    softly.assertAll();
  }

}
