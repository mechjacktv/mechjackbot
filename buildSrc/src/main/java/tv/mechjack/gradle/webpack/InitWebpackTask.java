package tv.mechjack.gradle.webpack;

import java.io.File;
import java.io.IOException;

import javax.inject.Inject;

import org.gradle.api.DefaultTask;
import org.gradle.api.Project;
import org.gradle.api.tasks.TaskAction;

import static tv.mechjack.gradle.webpack.WebpackPlugin.SOURCE_DIR;

public class InitWebpackTask extends DefaultTask {

  public static final String TASK_NAME = "initWebpack";

  private final Project project;
  private final TaskUtils taskUtils;

  @Inject
  public InitWebpackTask(final Project project) {
    this.project = project;
    this.taskUtils = new TaskUtils(project);
    this.setGroup(WebpackPlugin.TASK_GROUP);
    this.setDescription("Generates a webpack source set");
    this.dependsOn(project.getTasks().getByName(NpmInstallTask.TASK_NAME));
  }

  @TaskAction
  public final void initializeReact() {
    try {
      this.taskUtils.createFile(".babelrc");
      this.taskUtils.createFile("webpack.config.js");
      this.createWebpackSourceSet();
      this.executeNpmInstall();
    } catch (final IOException e) {
      throw new RuntimeException(e.getMessage(), e);
    }
  }

  private void createWebpackSourceSet() throws IOException {
    final File reactSource =
        this.taskUtils.absolutePath(SOURCE_DIR).toFile();

    if (!reactSource.exists()) {
      reactSource.mkdirs();
      this.taskUtils.createFile(SOURCE_DIR + "/index.js");
      this.taskUtils.createFile(SOURCE_DIR + "/reset.css");
    }
  }

  public final void executeNpmInstall() {
    try {
      final ProcessBuilder builder = new ProcessBuilder("npm", "install",
          "@babel/core", "@babel/preset-env", "acorn", "babel-loader",
          "css-loader", "style-loader", "webpack", "webpack-cli", "--save-dev")
          .directory(this.project.getProjectDir());

      this.taskUtils.createFile("package.json");
      this.taskUtils.monitorProcess("NpmInstall", builder.start());
    } catch (final IOException | InterruptedException | ProcessExecutionException e) {
      throw new RuntimeException(e.getMessage(), e);
    }
  }

}
