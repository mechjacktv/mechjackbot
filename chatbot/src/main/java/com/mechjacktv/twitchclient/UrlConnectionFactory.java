package com.mechjacktv.twitchclient;

import java.io.IOException;

interface UrlConnectionFactory {

    UrlConnection openConnection(String url) throws IOException;

}
