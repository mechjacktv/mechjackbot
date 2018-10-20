package com.mechjacktv.mechjackbot;

public interface AppConfiguration {

    String getProperty(String key, String defaultValue);

    void setProperty(String key, String value);

}
