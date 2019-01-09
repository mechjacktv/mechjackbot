package tv.mechjack.twitchclient;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.function.BiConsumer;

public class TestUrlConnection implements UrlConnection {

  private InputStreamSupplier getInputStreamHandler;
  private BiConsumer<String, String> setRequestPropertyHandler;

  public TestUrlConnection() {
    this.getInputStreamHandler = () -> new ByteArrayInputStream(new byte[0]);
    this.setRequestPropertyHandler = (name, value) -> {
      /* no-op (2018-12-10 mechjack) */
    };
  }

  @Override
  public InputStream getInputStream() throws IOException {
    return this.getInputStreamHandler.get();
  }

  public void setGetInputStreamHandler(final InputStreamSupplier getInputStreamHandler) {
    this.getInputStreamHandler = getInputStreamHandler;
  }

  @Override
  public void setRequestProperty(final String name, final String value) {
    this.setRequestPropertyHandler.accept(name, value);
  }

  public void setSetRequestPropertyHandler(final BiConsumer<String, String> setRequestPropertyHandler) {
    this.setRequestPropertyHandler = setRequestPropertyHandler;
  }

  @FunctionalInterface
  public interface InputStreamSupplier {

    InputStream get() throws IOException;

  }

}
