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
    this.loadProperties(propertiesFile);
    scheduleService.schedule(() -> this.loadProperties(propertiesFile), 10, TimeUnit.MINUTES, true);
  }

  private void loadProperties(final File propertiesFile) {
    try {
      if (this.didCreatePropertiesFile(propertiesFile)) {
        log.info(String.format("Created %s", propertiesFile.getCanonicalPath()));
      }
      try (final FileInputStream fileInputStream = new FileInputStream(propertiesFile)) {
        log.info(String.format("Loading %s", propertiesFile.getCanonicalPath()));
        this.properties.load(fileInputStream);
      }
    } catch (final Throwable t) {
      log.error(String.format("Failure while loading properties file, %s: %s", propertiesFile.getPath(),
          t.getMessage()), t);
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

  protected Properties getProperties() {
    return this.properties;
  }

}
