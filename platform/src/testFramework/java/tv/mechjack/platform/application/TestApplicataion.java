package tv.mechjack.platform.application;

import javax.inject.Inject;

import tv.mechjack.testframework.ArbitraryDataGenerator;

public final class TestApplicataion implements Application {

  private ApplicationDataLocation applicationDataLocation;

  @Inject
  TestApplicataion(final ArbitraryDataGenerator arbitraryDataGenerator) {
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
