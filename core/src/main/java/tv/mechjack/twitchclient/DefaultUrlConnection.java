package tv.mechjack.twitchclient;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;

final class DefaultUrlConnection implements UrlConnection {

  private final URLConnection urlConnection;

  DefaultUrlConnection(final URLConnection urlConnection) {
    this.urlConnection = urlConnection;
  }

  @Override
  public final InputStream getInputStream() throws IOException {
    return this.urlConnection.getInputStream();
  }

  @Override
  public final void setRequestProperty(final String name, final String value) {
    this.urlConnection.setRequestProperty(name, value);
  }

}
