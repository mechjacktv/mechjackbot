package tv.mechjack.platform.web;

import java.io.IOException;

public interface ResourceBaseFactory {

  ResourceBase createResourceBase(final Class<?> moduleClass) throws IOException;

}
