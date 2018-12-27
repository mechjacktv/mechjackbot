package tv.mechjack.twitchclient;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.io.Reader;
import java.io.StringReader;
import java.util.Set;

import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.assertj.core.api.SoftAssertions;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import tv.mechjack.proto.twitchclient.TwitchClientMessage.User;
import tv.mechjack.proto.twitchclient.TwitchClientMessage.Users;
import tv.mechjack.twitchclient.messageadapter.UserMessageTypeAdapter;
import tv.mechjack.util.function.ConsumerWithException;

public abstract class TwitchUsersEndpointContractTests {

  private static final String USER_ID = "123456789";
  private static final String USER_DISPLAY_NAME = "TestUser";
  private static final String USER_LOGIN = USER_DISPLAY_NAME.toLowerCase();
  private static final String USER_TYPE = "staff";
  private static final String USER_BROADCASTER_TYPE = "affiliate";
  private static final String USER_DESCRIPTION = "This is a test user.";
  private static final String USER_PROFILE_IMAGE_URL = "https://static-cdn.jtvnw.net/jtv_user_pictures/769d567c-2bd0-4544-b865-03a67f03f8a8-profile_image-300x300.png";
  private static final String USER_OFFLINE_IMAGE_URL = "https://static-cdn.jtvnw.net/jtv_user_pictures/0b371e3acf19d098-channel_offline_image-1920x1080.png";
  private static final int USER_VIEW_COUNT = 9001;
  private static final String UNKNOWN_DATA = "unknown";
  private static final String RESPONSE_BODY = "{\n"
      + "  \"data\": [{\n"
      + "    \"id\": \"" + USER_ID + "\",\n"
      + "    \"login\": \"" + USER_LOGIN + "\",\n"
      + "    \"display_name\": \"" + USER_DISPLAY_NAME + "\",\n"
      + "    \"type\": \"" + USER_TYPE + "\",\n"
      + "    \"broadcaster_type\": \"" + USER_BROADCASTER_TYPE + "\",\n"
      + "    \"description\": \"" + USER_DESCRIPTION + "\",\n"
      + "    \"profile_image_url\": \"" + USER_PROFILE_IMAGE_URL + "\",\n"
      + "    \"offline_image_url\": \"" + USER_OFFLINE_IMAGE_URL + "\",\n"
      + "    \"view_count\": " + USER_VIEW_COUNT + "\n"
      + "  }],\n"
      + "  \"" + UNKNOWN_DATA + "\": \"" + UNKNOWN_DATA + "\"\n"
      + "}";

  protected abstract TwitchUsersEndpoint givenASubjectToTest(Gson gson, TwitchClientUtils twitchClientUtils);

  private Gson givenAGson() {
    final GsonBuilder gsonBuilder = new GsonBuilder();

    gsonBuilder.registerTypeAdapter(User.class, new UserMessageTypeAdapter());
    return gsonBuilder.create();
  }

  private Set<TwitchUserId> givenASetOfTwitchIds(final int count) {
    final Set<TwitchUserId> ids = Sets.newHashSet();

    for (int i = 0; i < count; i++) {
      ids.add(TwitchUserId.of(Integer.toString(i)));
    }
    return ids;
  }

  private Set<TwitchLogin> givenASetOfTwitchLogins(final int count) {
    final Set<TwitchLogin> logins = Sets.newHashSet();

    for (int i = 0; i < count; i++) {
      logins.add(TwitchLogin.of("Login" + i));
    }
    return logins;
  }

  @Test
  public final void getUsers_nullLogins_throwsNullPointerExceptionWithMessage() {
    final TwitchUsersEndpoint subjectUnderTest = this.givenASubjectToTest(this.givenAGson(),
        mock(TwitchClientUtils.class));

    final Throwable thrown = catchThrowable(() -> subjectUnderTest.getUsers(null,
        this.givenASetOfTwitchIds(1)));

    assertThat(thrown).isInstanceOf(NullPointerException.class).hasMessage("Logins set **MUST** not be `null`.");
  }

  @Test
  public final void getUsers_nullIds_throwsNullPointerExceptionWithMessage() {
    final TwitchUsersEndpoint subjectUnderTest = this.givenASubjectToTest(this.givenAGson(),
        mock(TwitchClientUtils.class));

    final Throwable thrown = catchThrowable(() -> subjectUnderTest.getUsers(this.givenASetOfTwitchLogins(1),
        null));

    assertThat(thrown).isInstanceOf(NullPointerException.class).hasMessage("Ids set **MUST** not be `null`.");
  }

  @Test
  public final void getUsers_notEnoughLoginsAndIds_throwsIllegalArgumentExceptionWithMessage() {
    final TwitchUsersEndpoint subjectUnderTest = this.givenASubjectToTest(this.givenAGson(),
        mock(TwitchClientUtils.class));

    final Throwable thrown = catchThrowable(() -> subjectUnderTest.getUsers(this.givenASetOfTwitchLogins(0),
        this.givenASetOfTwitchIds(0)));

    assertThat(thrown).isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Minimum number of combined Twitch logins and ids is `1`.");
  }

  @Test
  public final void getUsers_tooManyLogins_throwsIllegalArgumentExceptionWithMessage() {
    final TwitchUsersEndpoint subjectUnderTest = this.givenASubjectToTest(this.givenAGson(),
        mock(TwitchClientUtils.class));

    final Throwable thrown = catchThrowable(() -> subjectUnderTest.getUsers(this.givenASetOfTwitchLogins(101),
        this.givenASetOfTwitchIds(0)));

    assertThat(thrown).isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Maximum number of combined Twitch logins and ids is `100`.");
  }

  @Test
  public final void getUsers_tooManyIds_throwsIllegalArgumentExceptionWithMessage() {
    final TwitchUsersEndpoint subjectUnderTest = this.givenASubjectToTest(this.givenAGson(),
        mock(TwitchClientUtils.class));

    final Throwable thrown = catchThrowable(() -> subjectUnderTest.getUsers(this.givenASetOfTwitchLogins(0),
        this.givenASetOfTwitchIds(101)));

    assertThat(thrown).isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Maximum number of combined Twitch logins and ids is `100`.");
  }

  @Test
  public final void getUsers_tooManyLoginsAndIds_throwsIllegalArgumentExceptionWithMessage() {
    final TwitchUsersEndpoint subjectUnderTest = this.givenASubjectToTest(this.givenAGson(),
        mock(TwitchClientUtils.class));

    final Throwable thrown = catchThrowable(() -> subjectUnderTest.getUsers(this.givenASetOfTwitchLogins(51),
        this.givenASetOfTwitchIds(51)));

    assertThat(thrown).isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Maximum number of combined Twitch logins and ids is `100`.");
  }

  @Test
  @SuppressWarnings("unchecked")
  public final void getUsers_forTwitchLogin_returnsAListWithOneUser() {
    final TwitchClientUtils twitchClientUtils = mock(TwitchClientUtils.class);
    final TwitchUsersEndpoint subjectUnderTest = this.givenASubjectToTest(this.givenAGson(), twitchClientUtils);
    doAnswer((invocation) -> {
      final ConsumerWithException<Reader> consumer = invocation.getArgument(1);

      consumer.accept(new StringReader(RESPONSE_BODY));
      return null;
    }).when(twitchClientUtils).handleResponse(isA(TwitchUrl.class), isA(ConsumerWithException.class));

    final Users result = subjectUnderTest.getUsers(this.givenASetOfTwitchLogins(1),
        this.givenASetOfTwitchIds(0));

    final SoftAssertions softly = new SoftAssertions();
    softly.assertThat(result).isNotNull();
    softly.assertThat(result.getUserCount()).isEqualTo(1);
    softly.assertThat(result.getUser(0).getId()).isEqualTo(USER_ID);
    softly.assertThat(result.getUser(0).getLogin()).isEqualTo(USER_LOGIN);
    softly.assertThat(result.getUser(0).getDisplayName()).isEqualTo(USER_DISPLAY_NAME);
    softly.assertThat(result.getUser(0).getType()).isEqualTo(USER_TYPE);
    softly.assertThat(result.getUser(0).getBroadcasterType()).isEqualTo(USER_BROADCASTER_TYPE);
    softly.assertThat(result.getUser(0).getDescription()).isEqualTo(USER_DESCRIPTION);
    softly.assertThat(result.getUser(0).getProfileImageUrl()).isEqualTo(USER_PROFILE_IMAGE_URL);
    softly.assertThat(result.getUser(0).getOfflineImageUrl()).isEqualTo(USER_OFFLINE_IMAGE_URL);
    softly.assertThat(result.getUser(0).getViewCount()).isEqualTo(USER_VIEW_COUNT);
    softly.assertAll();
  }

  @Test
  @SuppressWarnings("unchecked")
  public final void getUsers_forUserLogin_requestsUserForLogin() {
    final TwitchClientUtils twitchClientUtils = mock(TwitchClientUtils.class);
    final TwitchUsersEndpoint subjectUnderTest = this.givenASubjectToTest(this.givenAGson(), twitchClientUtils);
    final ArgumentCaptor<TwitchUrl> serviceUrl = ArgumentCaptor.forClass(TwitchUrl.class);
    doAnswer((invocation) -> {
      final ConsumerWithException<Reader> consumer = invocation.getArgument(1);

      consumer.accept(new StringReader(RESPONSE_BODY));
      return null;
    }).when(twitchClientUtils).handleResponse(serviceUrl.capture(), isA(ConsumerWithException.class));

    subjectUnderTest.getUsers(Sets.newHashSet(TwitchLogin.of(USER_LOGIN)), this.givenASetOfTwitchIds(0));

    assertThat(serviceUrl.getValue().value).contains("login=" + USER_LOGIN);
  }

  @Test
  @SuppressWarnings("unchecked")
  public final void getUsers_forUserId_requestsUserForId() {
    final TwitchClientUtils twitchClientUtils = mock(TwitchClientUtils.class);
    final TwitchUsersEndpoint subjectUnderTest = this.givenASubjectToTest(this.givenAGson(), twitchClientUtils);
    final ArgumentCaptor<TwitchUrl> serviceUrl = ArgumentCaptor.forClass(TwitchUrl.class);
    doAnswer((invocation) -> {
      final ConsumerWithException<Reader> consumer = invocation.getArgument(1);

      consumer.accept(new StringReader(RESPONSE_BODY));
      return null;
    }).when(twitchClientUtils).handleResponse(serviceUrl.capture(), isA(ConsumerWithException.class));

    subjectUnderTest.getUsers(this.givenASetOfTwitchLogins(0), Sets.newHashSet(TwitchUserId.of(USER_ID)));

    assertThat(serviceUrl.getValue().value).contains("id=" + USER_ID);
  }

  @Test
  @SuppressWarnings("unchecked")
  public final void getUsers_invalidObjectName_handlesInvalidObjectName() {
    final TwitchClientUtils twitchClientUtils = mock(TwitchClientUtils.class);
    final TwitchUsersEndpoint subjectUnderTest = this.givenASubjectToTest(this.givenAGson(),
        twitchClientUtils);
    doAnswer((invocation) -> {
      final ConsumerWithException<Reader> consumer = invocation.getArgument(1);

      consumer.accept(new StringReader(RESPONSE_BODY));
      return null;
    }).when(twitchClientUtils).handleResponse(isA(TwitchUrl.class), isA(ConsumerWithException.class));

    subjectUnderTest.getUsers(this.givenASetOfTwitchLogins(0), Sets.newHashSet(TwitchUserId.of(USER_ID)));

    verify(twitchClientUtils).handleUnknownObjectName(isA(String.class));
  }

}
