package tv.mechjack.platform.webapp.resourcebase;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import tv.mechjack.platform.webapp.ResourceBase;

public final class JarPublicResourceBase implements ResourceBase {

  private final File resourceBase;

  public JarPublicResourceBase(final URL moduleLocation) throws IOException {
    final File jarDestination = Files.createTempDirectory(null).toFile();

    unpackJar(getJarLocation(moduleLocation.getPath()), jarDestination.getAbsolutePath());
    this.resourceBase = new File(jarDestination, "public");
  }

  @Override
  public String getPath() {
    return this.resourceBase.getAbsolutePath();
  }

  private String getJarLocation(final String moduleLocation) {
    return moduleLocation.substring(5, moduleLocation.indexOf("jar!") + 3);
  }

  private void unpackJar(final String jarFile, final String dest) throws IOException {
    try (final ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(jarFile))) {
      ZipEntry entry;

      while ((entry = zipInputStream.getNextEntry()) != null) {
        final String filePath = dest + File.separator + entry.getName();

        if (!entry.isDirectory()) {
          extractFile(zipInputStream, filePath);
        } else {
          new File(filePath).mkdir();
        }
        zipInputStream.closeEntry();
      }
    }
  }

  private void extractFile(ZipInputStream zipIn, String filePath) throws IOException {
    try (final BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath))) {
      final byte[] bytesIn = new byte[4096];
      int read;

      while ((read = zipIn.read(bytesIn)) != -1) {
        bos.write(bytesIn, 0, read);
      }
    }
  }

}
