package tv.mechjack.gradle.react;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.NoSuchElementException;
import java.util.Scanner;

import org.gradle.api.Project;

final class TaskUtils {

  private final Project project;

  TaskUtils(final Project project) {
    this.project = project;
  }

  final Path absolutePath(final String path) {
    return new File(this.project.getProjectDir(), path).toPath();
  }

  final void createFile(final String path) throws IOException {
    if (this.doesNotExist(path)) {
      Files.copy(this.resourceStream(path), this.absolutePath(path));
    }
  }

  private boolean doesNotExist(final String path) {
    return !this.absolutePath(path).toFile().exists();
  }

  private InputStream resourceStream(final String path) {
    return this.getClass().getResourceAsStream("/" + path);
  }

  final void monitorProcess(final String name, final Process process)
      throws InterruptedException, ProcessExecutionException {
    final Scanner sout = new Scanner(process.getInputStream());
    final Scanner serr = new Scanner(process.getErrorStream());

    // TODO (2019-02-17 mechjack): use threads for reading pipes
    while (sout.hasNextLine() || serr.hasNextLine()) {
      try {
        System.out.println(sout.nextLine());
      } catch (final NoSuchElementException e) {
        // ignore
      }
      try {
        System.out.println(serr.nextLine());
      } catch (final NoSuchElementException e) {
        // ignore
      }
    }
    sout.close();
    serr.close();

    final int result = process.waitFor();

    if (result != 0) {
      throw new ProcessExecutionException(
          name + " failed with result: " + result);
    }
  }

}
