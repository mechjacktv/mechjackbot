package com.mechjacktv.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileInputStreamSupplier implements Supplier<InputStream> {

  private static final Logger log = LoggerFactory.getLogger(FileInputStreamSupplier.class);

  private final ExecutionUtils executionUtils;
  private final File file;

  public FileInputStreamSupplier(final ExecutionUtils executionUtils, final File file) {
    this.executionUtils = executionUtils;
    this.file = file;
  }

  @Override
  public InputStream get() {
    return executionUtils.softenException(() -> {
      if (didCreateFile(file)) {
        log.info(String.format("Created %s", file.getCanonicalPath()));
      }
      log.info(String.format("Accessing %s", file.getCanonicalPath()));
      return new FileInputStream(file);
    }, IORuntimeException.class);
  }

  private boolean didCreateFile(final File file) throws IOException {
    final File parentLocation = file.getParentFile();

    if (!parentLocation.exists()) {
      if (!parentLocation.mkdirs()) {
        throw new IOException(String.format("Failed to create %s", parentLocation.getCanonicalPath()));
      }
    } else if (!parentLocation.isDirectory()) {
      throw new IOException(parentLocation.getCanonicalPath() + " MUST be a directory");
    }
    return file.createNewFile();
  }

}
