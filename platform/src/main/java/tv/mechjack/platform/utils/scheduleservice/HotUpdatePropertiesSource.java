package tv.mechjack.platform.utils.scheduleservice;

import java.io.InputStream;

import tv.mechjack.platform.utils.function.ConsumerWithException;

public interface HotUpdatePropertiesSource {

  void read(ConsumerWithException<InputStream> propertiesLoader) throws Exception;

}
