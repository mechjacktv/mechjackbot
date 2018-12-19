package com.mechjacktv.util;

import java.io.InputStream;

import com.mechjacktv.util.function.ConsumerWithException;

public interface PropertiesSource {

  void read(ConsumerWithException<InputStream> propertiesLoader) throws Exception;

}
