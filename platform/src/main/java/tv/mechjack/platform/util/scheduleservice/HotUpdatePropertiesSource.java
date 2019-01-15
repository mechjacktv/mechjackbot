package tv.mechjack.platform.util.scheduleservice;

import java.io.InputStream;

import tv.mechjack.platform.util.function.ConsumerWithException;

public interface HotUpdatePropertiesSource {

  void read(ConsumerWithException<InputStream> propertiesLoader) throws Exception;

}
