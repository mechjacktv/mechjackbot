package tv.mechjack.platform.webapp.resourcebase;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import tv.mechjack.platform.webapp.ResourceBase;
import tv.mechjack.platform.webapp.ResourceBaseFactory;

public final class DefaultResourceBaseFactory implements ResourceBaseFactory {

  @Override
  public ResourceBase createResourceBase(final Class<?> moduleClass)
      throws IOException {
    final URL moduleLocation = moduleClass.getResource(
        getCanonicalNameAsPath(moduleClass));
    final String moduleLocationPath = moduleLocation.getPath();

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
