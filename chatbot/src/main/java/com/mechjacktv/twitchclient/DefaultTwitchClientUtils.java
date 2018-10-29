package com.mechjacktv.twitchclient;

import com.mechjacktv.util.ExecutionUtils;
import com.mechjacktv.util.function.ConsumerWithException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLConnection;

public final class DefaultTwitchClientUtils implements TwitchClientUtils {

  private static final Logger log = LoggerFactory.getLogger(DefaultTwitchClientUtils.class);
  private static final String TWITCH_API_URL = "https://api.twitch.tv/helix";

  private final TwitchClientId clientId;
  private final ExecutionUtils executionUtils;

  @Inject
  DefaultTwitchClientUtils(final TwitchClientConfiguration twitchClientConfiguration, final ExecutionUtils executionUtils) {
    this.clientId = twitchClientConfiguration.getTwitchClientId();
    this.executionUtils = executionUtils;
  }

  @Override
  public final void handleInvalidObjectName(final String name) {
    log.warn(String.format("Name '%s' was found but not expected", name));
  }

  @Override
  public final void handleResponse(final TwitchUrl serviceUrl, final ConsumerWithException<Reader> consumer) {
    this.executionUtils.softenException(() -> {
      try (final Reader reader = this.openResponseReader(serviceUrl)) {
        consumer.accept(reader);
      }
    }, TwitchConnectionException.class);
  }

  private Reader openResponseReader(final TwitchUrl serviceUrl) {
    return new InputStreamReader(this.openResponseInputStream(serviceUrl));
  }

  private InputStream openResponseInputStream(final TwitchUrl serviceUrl) {
    return this.executionUtils.softenException(() -> this.openConnection(serviceUrl).getInputStream(),
        TwitchConnectionException.class);
  }

  private URLConnection openConnection(final TwitchUrl serviceUrl) {
    return this.executionUtils.softenException(() -> {
      final URL url = new URL(String.format("%s/%s", TWITCH_API_URL, serviceUrl.value));
      final URLConnection urlConnection = url.openConnection();

      urlConnection.setRequestProperty("Client-ID", this.clientId.value);
      return urlConnection;
    }, TwitchConnectionException.class);
  }

}
