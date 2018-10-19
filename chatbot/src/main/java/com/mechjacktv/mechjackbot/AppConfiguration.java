package com.mechjacktv.mechjackbot;

public interface AppConfiguration {

    String getProperty(String key);

    String getProperty(String key, String defaultValue);

}
