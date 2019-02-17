package tv.mechjack.platform.webserver.resourcebase;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tv.mechjack.platform.webserver.ResourceBase;
import tv.mechjack.platform.webserver.ResourceBaseFactory;

public class DefaultResourceBaseFactory implements ResourceBaseFactory {

  private static final Logger LOGGER = LoggerFactory
      .getLogger(DefaultResourceBaseFactory.class);

  @Override
  public ResourceBase createResourceBase(final Class<?> moduleClass)
      throws IOException {
    final URL moduleLocation = moduleClass.getResource(
        getCanonicalNameAsPath(moduleClass));
    final String moduleLocationPath = moduleLocation.getPath();

    LOGGER.info(moduleLocationPath);
    if (moduleLocationPath.contains(".jar!")) {
      return new JarPublicResourceBase(moduleLocation);
    } else if (moduleLocationPath.contains("/build/classes/java/main"
        .replace('/', File.separatorChar))) {
      final String projectRoot = moduleLocationPath.substring(0,
          moduleLocationPath.indexOf("/build/classes/java/main"));

      return new GradleReactBuildResourceBase(projectRoot);
    }
    return new RelativePublicResourceBase();
  }

  private String getCanonicalNameAsPath(final Class<?> moduleClass) {
    final String className = moduleClass.getCanonicalName();

    return "/" + className.replace('.', '/') + ".class";
  }

}
