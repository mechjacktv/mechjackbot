package tv.mechjack.gradle.react;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.tasks.TaskContainer;

public class ReactWebappPlugin implements Plugin<Project> {

  public static final String TASK_GROUP = "React";

  @Override
  public void apply(final Project project) {
    final TaskContainer tasks = project.getTasks();

    tasks.register(NpmInstallTask.TASK_NAME, NpmInstallTask.class, project);
    tasks.register(UseReactTask.TASK_NAME, UseReactTask.class, project);
  }

}
