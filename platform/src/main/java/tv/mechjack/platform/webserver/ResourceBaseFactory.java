package tv.mechjack.platform.webserver;

import java.io.IOException;

public interface ResourceBaseFactory {

  ResourceBase createResourceBase(final Class<?> moduleClass) throws IOException;

}
