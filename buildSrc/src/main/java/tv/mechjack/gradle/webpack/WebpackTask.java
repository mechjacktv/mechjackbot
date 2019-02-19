package tv.mechjack.gradle.webpack;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

import javax.inject.Inject;

import org.gradle.api.DefaultTask;
import org.gradle.api.Project;
import org.gradle.api.tasks.TaskAction;
import org.gradle.api.tasks.TaskExecutionException;
import org.gradle.internal.impldep.aQute.bnd.build.Run;

import static tv.mechjack.gradle.webpack.WebpackPlugin.BUILD_DIR;
import static tv.mechjack.gradle.webpack.WebpackPlugin.SOURCE_DIR;

public class WebpackTask extends DefaultTask {

  public static final String TASK_NAME = "webpack";

  private final Project project;
  private final TaskUtils taskUtils;

  @Inject
  public WebpackTask(final Project project) {
    this.project = project;
    this.taskUtils = new TaskUtils(project);
    this.setGroup(WebpackPlugin.TASK_GROUP);
    this.setDescription(
        "Executes webpack on the webpack source set (each index.js is an entry point");
  }

  @TaskAction
  public final void webpackReactSource() {
    if (this.taskUtils.doesExist(SOURCE_DIR)) {
      try {
        this.taskUtils.absolutePath(BUILD_DIR).toFile().mkdirs();
        Files.walkFileTree(this.taskUtils.absolutePath(SOURCE_DIR),
            new IndexJsFileVisitor(this.project));
      } catch (final Exception e) {
        throw new TaskExecutionException(this, e);
      }
    }
  }

  private static final class IndexJsFileVisitor
      extends SimpleFileVisitor<Path> {

    private final Project project;
    private final TaskUtils taskUtils;

    public IndexJsFileVisitor(final Project project) {
      this.project = project;
      this.taskUtils = new TaskUtils(project);
    }

    @Override
    public FileVisitResult visitFile(final Path file,
        final BasicFileAttributes attrs) {
      try {
        if ("index.js".equals(file.getFileName().toString())) {
          final String sourcePart =
              sourcePart(file.toAbsolutePath().toString());
          final Path buildPath = this.taskUtils
              .absolutePath(BUILD_DIR + sourcePart);
          final ProcessBuilder builder = new ProcessBuilder("npx", "webpack",
              "--entry", file.toAbsolutePath().toString(), "--output",
              buildPath.toString()).directory(this.project.getProjectDir());

          this.taskUtils.monitorProcess("Webpack", builder.start());
        }
        return FileVisitResult.CONTINUE;
      } catch (final IOException | InterruptedException | ProcessExecutionException e) {
        throw new RuntimeException(e.getMessage(), e);
      }
    }

    private String sourcePart(final String path) {
      return path.substring(this.taskUtils.absolutePath(SOURCE_DIR)
          .toString().length());
    }

  }

}
