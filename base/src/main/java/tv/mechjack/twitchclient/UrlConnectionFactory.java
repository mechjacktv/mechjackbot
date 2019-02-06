package tv.mechjack.twitchclient;

import java.io.IOException;

interface UrlConnectionFactory {

  UrlConnection openConnection(String url) throws IOException;

}
