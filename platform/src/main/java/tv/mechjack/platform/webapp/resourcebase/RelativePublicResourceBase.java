package tv.mechjack.platform.webapp.resourcebase;

import java.io.File;

import tv.mechjack.platform.webapp.ResourceBase;

public class RelativePublicResourceBase implements ResourceBase {

  private final File resourceBase = new File("public/");

  @Override
  public String getPath() {
    return this.resourceBase.getAbsolutePath();
  }

}
