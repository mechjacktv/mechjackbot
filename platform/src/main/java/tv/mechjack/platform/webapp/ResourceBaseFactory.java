package tv.mechjack.platform.webapp;

import java.io.IOException;

public interface ResourceBaseFactory {

  ResourceBase createResourceBase(final Class<?> moduleClass) throws IOException;

}
