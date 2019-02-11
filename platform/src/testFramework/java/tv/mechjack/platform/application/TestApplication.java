package tv.mechjack.platform.application;

import javax.inject.Inject;

import tv.mechjack.testframework.ArbitraryData;

public final class TestApplication implements Application {

  private ApplicationDataLocation applicationDataLocation;

  @Inject
  TestApplication(final ArbitraryData arbitraryDataGenerator) {
    this.applicationDataLocation = ApplicationDataLocation.of(arbitraryDataGenerator.getString());
  }

  @Override
  public ApplicationDataLocation getApplicationDataLocation() {
    return this.applicationDataLocation;
  }

  public void setApplicationDataLocation(final String applicationDataLocation) {
    this.applicationDataLocation = ApplicationDataLocation.of(applicationDataLocation);
  }

}
