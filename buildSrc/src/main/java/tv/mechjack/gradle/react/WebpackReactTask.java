package tv.mechjack.gradle.react;

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

public class WebpackReactTask extends DefaultTask {

  public static final String TASK_NAME = "webpackReact";

  private final Project project;
  private final TaskUtils taskUtils;

  @Inject
  public WebpackReactTask(final Project project) {
    this.project = project;
    this.taskUtils = new TaskUtils(project);
    this.setGroup(ReactWebappPlugin.TASK_GROUP);
    this.setDescription(
        "Executes webpack over all 'index.js' files in react source set");
  }

  @TaskAction
  public final void webpackReactSource() {
    if (this.taskUtils.doesExist("src/main/react")) {
      try {
        this.taskUtils.absolutePath("build/react/main/").toFile().mkdirs();
        Files.walkFileTree(this.taskUtils.absolutePath("src/main/react"),
            new IndexJsFileVisitor(this.project));
      } catch (final IOException e) {
        throw new RuntimeException(e.getMessage(), e);
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
          final String relativePath = relativePath(
              file.toAbsolutePath().toString());
          final Path buildPath = this.taskUtils
              .absolutePath("build/react/main/" + relativePath);

          final ProcessBuilder builder = new ProcessBuilder("npx", "webpack",
              "--entry", file.toAbsolutePath().toString(), "--output",
              buildPath.toString()).directory(this.project.getProjectDir());

          this.taskUtils.monitorProcess("Webpack", builder.start());
        }
        return FileVisitResult.CONTINUE;
      } catch (final IOException | InterruptedException | ProcessExecutionException e) {
        e.printStackTrace();
        return FileVisitResult.TERMINATE;
      }
    }

    private String relativePath(final String path) {
      final Path rootPath = this.taskUtils.absolutePath("src/main/react");

      return path.substring(rootPath.toString().length() + 1);
    }

  }

}
