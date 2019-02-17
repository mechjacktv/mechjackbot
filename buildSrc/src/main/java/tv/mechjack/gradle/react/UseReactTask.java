package tv.mechjack.gradle.react;

import java.io.File;
import java.io.IOException;

import javax.inject.Inject;

import org.gradle.api.DefaultTask;
import org.gradle.api.Project;
import org.gradle.api.tasks.TaskAction;

public class UseReactTask extends DefaultTask {

  public static final String TASK_NAME = "useReact";

  private final Project project;
  private final TaskUtils taskUtils;

  @Inject
  public UseReactTask(final Project project) {
    this.project = project;
    this.taskUtils = new TaskUtils(project);
    this.setGroup(ReactWebappPlugin.TASK_GROUP);
    this.setDescription("Generates a simple ReactJS webapp");
    this.dependsOn(project.getTasks().getByName(NpmInstallTask.TASK_NAME));
  }

  @TaskAction
  public final void initializeReact() {
    try {
      this.createReactSourceSet();
      this.taskUtils.createFile(".babelrc");
      this.taskUtils.createFile("webpack.config.js");
    } catch (final IOException e) {
      throw new RuntimeException(e.getMessage(), e);
    }
  }

  private void createReactSourceSet() throws IOException {
    final File reactSource =
        this.taskUtils.absolutePath("src/main/react").toFile();

    if (!reactSource.exists()) {
      reactSource.mkdirs();
      this.taskUtils.createFile("src/main/react/App.css");
      this.taskUtils.createFile("src/main/react/App.js");
      this.taskUtils.createFile("src/main/react/index.css");
      this.taskUtils.createFile("src/main/react/index.js");
    }
  }

}
