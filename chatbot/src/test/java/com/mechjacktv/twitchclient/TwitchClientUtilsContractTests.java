package com.mechjacktv.twitchclient;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

import org.junit.Test;
import org.mockito.ArgumentCaptor;

import com.mechjacktv.util.DefaultExecutionUtils;
import com.mechjacktv.util.ExecutionUtils;
import com.mechjacktv.util.function.ConsumerWithException;

public abstract class TwitchClientUtilsContractTests {

  private static final String EXCEPTION_MESSAGE = "exception message";
  private static final TwitchUrl SERVICE_URL = TwitchUrl.of("api/test");
  private static final TwitchClientId TWITCH_CLIENT_ID = TwitchClientId.of("TWITCH_CLIENT_ID");

  @Test
  public final void handleResponse_openConnectionThrowsIOException_consumerIsNotCalled() throws IOException {
    final UrlConnectionFactory urlConnectionFactory = mock(UrlConnectionFactory.class);
    final TwitchClientUtils subjectUnderTest = this.givenASubjectToTest(urlConnectionFactory);
    when(urlConnectionFactory.openConnection(isA(String.class))).thenAnswer((i) -> {
      throw new IOException(EXCEPTION_MESSAGE);
    });

    final Throwable thrown = catchThrowable(() -> subjectUnderTest.handleResponse(SERVICE_URL,
        (reader) -> fail("Response handler **MUST** not be called on connection error")));

    assertThat(thrown).isNotNull();
  }

  private TwitchClientUtils givenASubjectToTest(final UrlConnectionFactory urlConnectionFactory) {
    final TwitchClientConfiguration twitchClientConfiguration = mock(TwitchClientConfiguration.class);

    when(twitchClientConfiguration.getTwitchClientId()).thenReturn(TWITCH_CLIENT_ID);
    return this.givenASubjectToTest(twitchClientConfiguration, new DefaultExecutionUtils(),
        urlConnectionFactory);
  }

  abstract TwitchClientUtils givenASubjectToTest(TwitchClientConfiguration twitchClientConfiguration,
      ExecutionUtils executionUtils,
      UrlConnectionFactory urlConnectionFactory);

  @Test
  public final void handleResponse_openConnectionThrowsIOException_twitchConnectExceptionIsThrown() throws IOException {
    final UrlConnectionFactory urlConnectionFactory = mock(UrlConnectionFactory.class);
    final TwitchClientUtils subjectUnderTest = this.givenASubjectToTest(urlConnectionFactory);
    when(urlConnectionFactory.openConnection(isA(String.class))).thenAnswer((i) -> {
      throw new IOException(EXCEPTION_MESSAGE);
    });

    final Throwable thrown = catchThrowable(() -> subjectUnderTest.handleResponse(SERVICE_URL, (reader) -> {
      // empty
    }));

    assertThat(thrown).isInstanceOf(TwitchConnectionException.class).hasMessage(EXCEPTION_MESSAGE);
  }

  @Test
  public final void handleResponse_connectionOpened_callsCorrectServiceApi() throws IOException {
    final UrlConnection urlConnection = this.givenAFakeUrlConnection();
    final UrlConnectionFactory urlConnectionFactory = mock(UrlConnectionFactory.class);
    final TwitchClientUtils subjectUnderTest = this.givenASubjectToTest(urlConnectionFactory);
    final ArgumentCaptor<String> url = ArgumentCaptor.forClass(String.class);
    when(urlConnectionFactory.openConnection(url.capture())).thenReturn(urlConnection);

    subjectUnderTest.handleResponse(SERVICE_URL, (reader) -> {
      // empty
    });

    assertThat(url.getValue()).contains(TwitchClientUtils.TWITCH_API_URL);
    assertThat(url.getValue()).contains(SERVICE_URL.value);
  }

  private UrlConnection givenAFakeUrlConnection() throws IOException {
    final UrlConnection urlConnection = mock(UrlConnection.class);

    when(urlConnection.getInputStream()).thenReturn(mock(InputStream.class));
    return urlConnection;
  }

  @Test
  public final void handleResponse_connectionOpened_setClientId() throws IOException {
    final UrlConnection urlConnection = this.givenAFakeUrlConnection();
    final TwitchClientUtils subjectUnderTest = this.givenASubjectToTest(urlConnection);
    final ArgumentCaptor<String> url = ArgumentCaptor.forClass(String.class);
    doNothing().when(urlConnection).setRequestProperty(eq("Client-ID"), url.capture());

    subjectUnderTest.handleResponse(SERVICE_URL, (reader) -> {
      // empty
    });

    assertThat(url.getValue()).isEqualTo(TWITCH_CLIENT_ID.value);
  }

  private TwitchClientUtils givenASubjectToTest(final UrlConnection urlConnection) throws IOException {
    return this.givenASubjectToTest(this.givenAFakeUrlConnectionFactory(urlConnection));
  }

  private UrlConnectionFactory givenAFakeUrlConnectionFactory(final UrlConnection urlConnection) throws IOException {
    final UrlConnectionFactory urlConnectionFactory = mock(UrlConnectionFactory.class);

    when(urlConnectionFactory.openConnection(isA(String.class))).thenReturn(urlConnection);
    return urlConnectionFactory;
  }

  @Test
  public final void handleResponse_getInputStreamThrowsIOException_throwsTwitchConnectException() throws IOException {
    final UrlConnection urlConnection = mock(UrlConnection.class);
    final TwitchClientUtils subjectUnderTest = this.givenASubjectToTest(urlConnection);
    when(urlConnection.getInputStream()).thenThrow(new IOException(EXCEPTION_MESSAGE));

    final Throwable thrown = catchThrowable(() -> subjectUnderTest.handleResponse(SERVICE_URL, (reader) -> {
      // empty
    }));

    assertThat(thrown).isInstanceOf(TwitchConnectionException.class).hasMessage(EXCEPTION_MESSAGE);
  }

  @Test
  public final void handleResponse_consumerThrowsException_throwsTwitchDataException() throws IOException {
    final TwitchClientUtils subjectUnderTest = this.givenASubjectToTest(this.givenAFakeUrlConnectionFactory());

    final Throwable thrown = catchThrowable(() -> subjectUnderTest.handleResponse(SERVICE_URL, (reader) -> {
      throw new Exception(EXCEPTION_MESSAGE);
    }));

    assertThat(thrown).isInstanceOf(TwitchDataException.class).hasMessage(EXCEPTION_MESSAGE);
  }

  private UrlConnectionFactory givenAFakeUrlConnectionFactory() throws IOException {
    return this.givenAFakeUrlConnectionFactory(this.givenAFakeUrlConnection());
  }

  @Test
  @SuppressWarnings("unchecked")
  public final void handleResponse_allGoesWell_consumerIsCalled() throws Exception {
    final TwitchClientUtils subjectUnderTest = this.givenASubjectToTest(this.givenAFakeUrlConnectionFactory());
    final ConsumerWithException<Reader> consumer = mock(ConsumerWithException.class);

    subjectUnderTest.handleResponse(SERVICE_URL, consumer);

    verify(consumer).accept(isA(Reader.class));
  }

}
