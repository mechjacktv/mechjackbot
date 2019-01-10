package tv.mechjack.util.scheduleservice;

import java.io.InputStream;

import tv.mechjack.util.function.ConsumerWithException;

public interface HotUpdatePropertiesSource {

  void read(ConsumerWithException<InputStream> propertiesLoader) throws Exception;

}
