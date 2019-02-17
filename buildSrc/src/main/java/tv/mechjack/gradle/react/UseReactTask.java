package tv.mechjack.gradle.react;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import javax.inject.Inject;

import org.gradle.api.DefaultTask;
import org.gradle.api.Project;
import org.gradle.api.tasks.TaskAction;

public class InitReactTask extends DefaultTask {

  public static final String TASK_NAME = "initReact";

  private final Project project;
  private final TaskUtils taskUtils;

  @Inject
  public InitReactTask(final Project project) {
    this.project = project;
    this.taskUtils = new TaskUtils(project);
    this.setGroup(ReactWebappPlugin.TASK_GROUP);
    this.setDescription("Generates a simple ReactJS webapp");
  }

  @TaskAction
  public final void initializeReact() {
    createFile(".babelrc");
    createFile("package.json");
    createFile("webpack.config.js");
    this.createReactSourceSet();

    try {
      final ProcessBuilder builder = new ProcessBuilder("npm", "install")
          .directory(this.project.getProjectDir());

      this.taskUtils.monitorProcess("NpmInstall", builder.start());
    } catch (final IOException | InterruptedException | ProcessExecutionException e) {
      throw new RuntimeException(e.getMessage(), e);
    }
  }

  private void createReactSourceSet() {
    final File reactSource = new File(
        this.taskUtils.getAbsolutePath("src/main/react"));

    if (!reactSource.exists()) {
      reactSource.mkdirs();
      createFile("src/main/react/App.css");
      createFile("src/main/react/App.js");
      createFile("src/main/react/index.css");
      createFile("src/main/react/index.js");
    }
  }

  public final void createFile(final String path) {
    if (this.doesNotExist(path)) {
      try {
        Files.copy(this.getResourceStream(path), this.getAbsolutePath(path),
            StandardCopyOption.ATOMIC_MOVE);
      } catch (final IOException e) {
        throw new RuntimeException(e.getMessage(), e);
      }
    }
  }

  private boolean doesNotExist(final String path) {
    return !new File(this.taskUtils.getAbsolutePath(path)).exists();
  }

  private InputStream getResourceStream(final String path) {
    return this.getClass().getResourceAsStream(path);
  }

  private Path getAbsolutePath(final String path) {
    return Paths.get(this.taskUtils.getAbsolutePath(path));
  }

}
