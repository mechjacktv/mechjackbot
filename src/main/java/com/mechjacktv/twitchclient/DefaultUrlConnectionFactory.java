package com.mechjacktv.twitchclient;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

final class DefaultUrlConnectionFactory implements UrlConnectionFactory {

  @Override
  public final UrlConnection openConnection(final String url) throws IOException {
    final URLConnection urlConnection = new URL(url).openConnection();

    return new DefaultUrlConnection(urlConnection);
  }

}
