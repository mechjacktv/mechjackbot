package com.mechjacktv.twitchclient;

import java.io.IOException;
import java.io.InputStream;

interface UrlConnection {

  InputStream getInputStream() throws IOException;

  void setRequestProperty(String name, String value);

}
