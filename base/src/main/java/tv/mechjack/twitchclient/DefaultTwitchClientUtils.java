package tv.mechjack.twitchclient;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tv.mechjack.platform.utils.ExecutionUtils;
import tv.mechjack.platform.utils.function.ConsumerWithException;

public final class DefaultTwitchClientUtils implements TwitchClientUtils {

  private final Logger log;
  private final TwitchClientId clientId;
  private final ExecutionUtils executionUtils;
  private final UrlConnectionFactory urlConnectionFactory;

  @Inject
  DefaultTwitchClientUtils(final TwitchClientConfiguration twitchClientConfiguration,
      final ExecutionUtils executionUtils) {
    this(twitchClientConfiguration, executionUtils, new DefaultUrlConnectionFactory(),
        LoggerFactory.getLogger(DefaultTwitchClientUtils.class));
  }

  DefaultTwitchClientUtils(final TwitchClientConfiguration twitchClientConfiguration,
      final ExecutionUtils executionUtils,
      final UrlConnectionFactory urlConnectionFactory, final Logger log) {
    this.log = log;
    this.clientId = twitchClientConfiguration.getTwitchClientId();
    this.executionUtils = executionUtils;
    this.urlConnectionFactory = urlConnectionFactory;
  }

  @Override
  public final void handleUnknownObjectName(final String name) {
    this.log.warn(String.format("Name '%s' was found but not expected", name));
  }

  @Override
  public final void handleResponse(final TwitchUrl serviceUrl, final ConsumerWithException<Reader> consumer) {
    this.executionUtils.softenException(() -> {
      try (final Reader reader = this.openResponseReader(serviceUrl)) {
        this.executionUtils.softenException(() -> consumer.accept(reader), TwitchDataException.class);
      }
    }, TwitchConnectionException.class);
  }

  private Reader openResponseReader(final TwitchUrl serviceUrl) {
    return new InputStreamReader(this.openResponseInputStream(serviceUrl), Charset.defaultCharset());
  }

  private InputStream openResponseInputStream(final TwitchUrl serviceUrl) {
    return this.executionUtils.softenException(() -> this.openConnection(serviceUrl).getInputStream(),
        TwitchConnectionException.class);
  }

  private UrlConnection openConnection(final TwitchUrl serviceUrl) {
    return this.executionUtils.softenException(() -> {
      final UrlConnection urlConnection = this.urlConnectionFactory
          .openConnection(String.format("%s/%s", TWITCH_API_URL, serviceUrl.value));

      urlConnection.setRequestProperty("Client-ID", this.clientId.value);
      return urlConnection;
    }, TwitchConnectionException.class);
  }

}
