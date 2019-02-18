package tv.mechjack.gradle.webpack;

import java.io.IOException;

import javax.inject.Inject;

import org.gradle.api.DefaultTask;
import org.gradle.api.Project;
import org.gradle.api.tasks.TaskAction;

public class NpmInstallTask extends DefaultTask {

  public static final String TASK_NAME = "npmInstall";

  private final Project project;
  private final TaskUtils taskUtils;

  @Inject
  public NpmInstallTask(final Project project) {
    this.project = project;
    this.taskUtils = new TaskUtils(project);
    this.setGroup(WebpackPlugin.TASK_GROUP);
    this.setDescription(
        "Generates `package.json` if missing and executes `npm install`");
  }

  @TaskAction
  public final void executeNpmInstall() {
    try {
      final ProcessBuilder builder = new ProcessBuilder("npm", "install")
          .directory(this.project.getProjectDir());

      this.taskUtils.createFile("package.json");
      this.taskUtils.monitorProcess("NpmInstall", builder.start());
    } catch (final IOException | InterruptedException | ProcessExecutionException e) {
      throw new RuntimeException(e.getMessage(), e);
    }
  }

}
