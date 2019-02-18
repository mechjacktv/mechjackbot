package tv.mechjack.gradle.webpack;

import java.util.Objects;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.tasks.TaskContainer;
import org.gradle.jvm.tasks.Jar;

public class WebpackPlugin implements Plugin<Project> {

  public static final String TASK_GROUP = "Webpack";
  public static final String SOURCE_DIR = "src/main/webpack";
  public static final String BUILD_DIR = "build/webpack/main";

  @Override
  public void apply(final Project project) {
    final TaskContainer tasks = project.getTasks();

    tasks.register(NpmInstallTask.TASK_NAME, NpmInstallTask.class, project);
    tasks.register(InitWebpackTask.TASK_NAME, InitWebpackTask.class, project);
    tasks.register(WebpackTask.TASK_NAME, WebpackTask.class, project);

    final Jar jarTask = (Jar) project.getTasks().findByPath("jar");
    final Task runTask = project.getTasks().findByPath("run");

    if (Objects.nonNull(jarTask)) {
      jarTask.dependsOn(project.getTasks().getByName(WebpackTask.TASK_NAME));
      jarTask.into("public/", copySpec -> copySpec.from(BUILD_DIR));
    }
    if (Objects.nonNull(runTask)) {
      runTask.dependsOn(project.getTasks().getByName(WebpackTask.TASK_NAME));
    }
  }

}
