package tv.mechjack.util;

import java.io.InputStream;

import tv.mechjack.util.function.ConsumerWithException;

public interface PropertiesSource {

  void read(ConsumerWithException<InputStream> propertiesLoader) throws Exception;

}
