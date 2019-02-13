package tv.mechjack.platform.webserver.resourcebase;

import java.io.File;

import tv.mechjack.platform.webserver.ResourceBase;

public class RelativePublicResourceBase implements ResourceBase {

  private final File resourceBase = new File("public/");

  @Override
  public String getPath() {
    return this.resourceBase.getAbsolutePath();
  }

}
