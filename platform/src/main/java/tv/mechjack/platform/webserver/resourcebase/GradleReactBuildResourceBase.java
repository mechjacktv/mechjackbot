package tv.mechjack.platform.webserver.resourcebase;

import java.io.File;

import tv.mechjack.platform.webserver.ResourceBase;

public class GradleReactBuildResourceBase implements ResourceBase {

  public final File resourceBase;

  public GradleReactBuildResourceBase(final String projectRoot) {
    this.resourceBase = new File(projectRoot, "build/webpack/main/");
  }

  @Override
  public String getPath() {
    return this.resourceBase.getAbsolutePath();
  }

}
