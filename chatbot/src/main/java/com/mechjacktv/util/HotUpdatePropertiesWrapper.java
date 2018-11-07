package com.mechjacktv.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mechjacktv.util.scheduleservice.ScheduleService;

public abstract class HotUpdatePropertiesWrapper {

  private static final Logger log = LoggerFactory.getLogger(HotUpdatePropertiesWrapper.class);

  private final Properties properties;

  public HotUpdatePropertiesWrapper(final File propertiesFile, final ScheduleService scheduleService) {
    this.properties = new Properties();
    scheduleService.schedule(() -> this.loadProperties(propertiesFile), 10, TimeUnit.MINUTES);
  }

  private void loadProperties(final File propertiesFile) {
    try {
      if (this.didCreatePropertiesFile(propertiesFile)) {
        this.handleMissingPropertiesFile();
      }

      try (final FileInputStream fileInputStream = new FileInputStream(propertiesFile)) {
        log.info(String.format("Loading %s", propertiesFile.getCanonicalPath()));
        this.properties.load(fileInputStream);
      }

      if (this.isMissingRequiredValues()) {
        this.handleMissingRequiredValues();
      }
    } catch (final Throwable t) {
      log.error(String.format("Failure while loading properties file, %s", propertiesFile.getPath()), t);
    }
  }

  private boolean didCreatePropertiesFile(final File propertiesFile) throws IOException {
    final File parentLocation = propertiesFile.getParentFile();

    if (!parentLocation.exists()) {
      if (!parentLocation.mkdirs()) {
        throw new IOException(String.format("Failed to create %s", parentLocation.getCanonicalPath()));
      }
    } else if (!parentLocation.isDirectory()) {
      throw new IOException(parentLocation.getCanonicalPath() + " MUST be a directory");
    }
    return propertiesFile.createNewFile();
  }

  protected void handleMissingPropertiesFile() {
    // empty
  }

  protected boolean isMissingRequiredValues() {
    return false;
  }

  protected void handleMissingRequiredValues() {
    // empty
  }

  protected Properties getProperties() {
    return this.properties;
  }

}
