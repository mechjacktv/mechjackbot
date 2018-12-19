package com.mechjacktv.twitchclient;

import java.io.IOException;

public class TestUrlConnectionFactory implements UrlConnectionFactory {

  private OpenConnectionHandler openConnectionHandler;

  public TestUrlConnectionFactory() {
    this.openConnectionHandler = url -> new TestUrlConnection();
  }

  @Override
  public UrlConnection openConnection(final String url) throws IOException {
    return this.openConnectionHandler.get(url);
  }

  public void setOpenConnectionHandler(final OpenConnectionHandler openConnectionHandler) {
    this.openConnectionHandler = openConnectionHandler;
  }

  @FunctionalInterface
  public interface OpenConnectionHandler {

    UrlConnection get(String url) throws IOException;

  }

}
