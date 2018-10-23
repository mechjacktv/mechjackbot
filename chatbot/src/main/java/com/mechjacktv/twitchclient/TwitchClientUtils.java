package com.mechjacktv.twitchclient;

import com.mechjacktv.util.ExecutionUtils;
import com.mechjacktv.util.function.ConsumerWithException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLConnection;

final class TwitchClientUtils {

  private static final Logger log = LoggerFactory.getLogger(TwitchClientUtils.class);
  private static final String TWITCH_API_URL = "https://api.twitch.tv/helix";

  private final String clientId;
  private final ExecutionUtils executionUtils;

  TwitchClientUtils(final String clientId, final ExecutionUtils executionUtils) {
    this.clientId = clientId;
    this.executionUtils = executionUtils;
  }

  final void handleInvalidName(final String name) {
    log.warn(String.format("Name '%s' was found but not expected", name));
  }

  final void handleResponse(final String serviceUrl, final ConsumerWithException<Reader> consumer) {
    this.executionUtils.softenException(() -> {
      try (final Reader reader = this.openResponseReader(serviceUrl)) {
        consumer.accept(reader);
      }
    }, ConnectionException.class);
  }

  private Reader openResponseReader(final String serviceUrl) {
    return new InputStreamReader(openResponseInputStream(serviceUrl));
  }

  private InputStream openResponseInputStream(final String serviceUrl) {
    return this.executionUtils.softenException(() -> this.openConnection(serviceUrl).getInputStream(),
        ConnectionException.class);
  }

  private URLConnection openConnection(final String serviceUrl) {
    return executionUtils.softenException(() -> {
      final URL url = new URL(String.format("%s/%s", TWITCH_API_URL, serviceUrl));
      final URLConnection urlConnection = url.openConnection();

      urlConnection.setRequestProperty("Client-ID", this.clientId);
      return urlConnection;
    }, ConnectionException.class);
  }

}
