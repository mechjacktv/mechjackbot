package tv.mechjack.gradle.react;

import java.util.Objects;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.tasks.TaskContainer;
import org.gradle.jvm.tasks.Jar;

public class ReactWebappPlugin implements Plugin<Project> {

  public static final String TASK_GROUP = "ReactJS";

  @Override
  public void apply(final Project project) {
    final TaskContainer tasks = project.getTasks();

    tasks.register(NpmInstallTask.TASK_NAME, NpmInstallTask.class, project);
    tasks.register(UseReactTask.TASK_NAME, UseReactTask.class, project);
    tasks.register(WebpackReactTask.TASK_NAME, WebpackReactTask.class, project);

    final Jar jarTask = (Jar) project.getTasks().findByPath("jar");
    final Task runTask = project.getTasks().findByPath("run");

    if (Objects.nonNull(jarTask)) {
      jarTask
          .dependsOn(project.getTasks().getByName(WebpackReactTask.TASK_NAME));
      jarTask.into("public/", copySpec -> copySpec.from("build/react/main"));
    }
    if (Objects.nonNull(runTask)) {
      runTask
          .dependsOn(project.getTasks().getByName(WebpackReactTask.TASK_NAME));
    }
  }

}
