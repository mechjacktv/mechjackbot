package tv.mechjack.gradle.react;

import java.io.File;
import java.util.NoSuchElementException;
import java.util.Scanner;

import org.gradle.api.Project;

final class TaskUtils {

  private final Project project;

  TaskUtils(final Project project) {
    this.project = project;
  }


  final String getAbsolutePath(final String relativePath) {
    return new File(this.project.getProjectDir(), relativePath).getAbsolutePath();
  }

  final  void monitorProcess(final String name, final Process process)
      throws InterruptedException, ProcessExecutionException {
    final Scanner sout = new Scanner(process.getInputStream());
    final Scanner serr = new Scanner(process.getErrorStream());

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
      throw new ProcessExecutionException(name + " failed with result: " + result);
    }
  }

}
