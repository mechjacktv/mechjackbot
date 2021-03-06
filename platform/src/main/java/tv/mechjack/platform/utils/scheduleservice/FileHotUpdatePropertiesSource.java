package tv.mechjack.platform.utils.scheduleservice;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tv.mechjack.platform.utils.function.ConsumerWithException;

public final class FileHotUpdatePropertiesSource implements HotUpdatePropertiesSource {

  private static final Logger LOGGER = LoggerFactory.getLogger(FileHotUpdatePropertiesSource.class);

  private final File propertiesFile;

  public FileHotUpdatePropertiesSource(final File propertiesFile) {
    this.propertiesFile = propertiesFile;
  }

  @Override
  public void read(final ConsumerWithException<InputStream> propertiesLoader) throws Exception {
    if (this.didCreateFile(this.propertiesFile)) {
      LOGGER.info(String.format("Created %s", this.propertiesFile.getCanonicalPath()));
    }
    try (final InputStream propertiesInputStream = new FileInputStream(this.propertiesFile)) {
      LOGGER.debug(String.format("Accessing %s",
          this.propertiesFile.getCanonicalPath()));
      propertiesLoader.accept(propertiesInputStream);
    }
  }

  private boolean didCreateFile(final File propertiesFile) throws IOException {
    final File parentLocation = propertiesFile.getParentFile();

    if (!parentLocation.exists()) {
      if (!parentLocation.mkdirs()) {
        throw new IOException(String.format("Failed to create %s", parentLocation.getCanonicalPath()));
      }
    } else if (!parentLocation.isDirectory()) {
      throw new IllegalStateException(parentLocation.getCanonicalPath() + " MUST be a directory");
    }
    return propertiesFile.createNewFile();
  }

}
