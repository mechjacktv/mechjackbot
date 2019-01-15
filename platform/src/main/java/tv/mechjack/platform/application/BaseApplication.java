package tv.mechjack.platform.application;

public abstract class BaseApplication implements Application {

  public static final String KEY_APPLICATION_DATA_LOCATION = "application.data_location";

  private final ApplicationDataLocation applicationDataLocation;

  protected BaseApplication(final String defaultApplicationDataLocation) {
    this.applicationDataLocation = ApplicationDataLocation
        .of(System.getProperty(KEY_APPLICATION_DATA_LOCATION, defaultApplicationDataLocation));
  }

  public ApplicationDataLocation getApplicationDataLocation() {
    return this.applicationDataLocation;
  }

}
