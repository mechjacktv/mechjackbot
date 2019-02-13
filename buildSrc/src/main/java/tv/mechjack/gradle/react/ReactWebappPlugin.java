package tv.mechjack.gradle.react;

import java.io.File;
import java.util.Objects;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.tasks.TaskContainer;
import org.gradle.jvm.tasks.Jar;

public class ReactWebappPlugin implements Plugin<Project> {

  public static final String TASK_GROUP = "React Webapp";

  @Override
  public void apply(final Project project) {
    if(new File(project.getProjectDir(), "src/main/react").exists()) {
      project.getTasks().register(NpmInstallTask.TASK_NAME, NpmInstallTask.class, project);
      project.getTasks().register(CompileJavaScriptTask.TASK_NAME, CompileJavaScriptTask.class, project);
      project.getTasks().register(CopyCssTask.TASK_NAME, CopyCssTask.class, project);
      project.getTasks().register(WebpackTask.TASK_NAME, WebpackTask.class, project);

      final TaskContainer tasks = project.getTasks();

      final Jar jarTask = (Jar) project.getTasks().findByPath("jar");
      final Task runTask = project.getTasks().findByPath("run");

      if(Objects.nonNull(jarTask)) {
        jarTask.dependsOn(project.getTasks().getByName(WebpackTask.TASK_NAME));
        jarTask.into("public/", copySpec -> {
          copySpec.from(WebpackTask.MAIN_OUTPUT_DIR);
        });
      }
      if(Objects.nonNull(runTask)) {
        runTask.dependsOn(project.getTasks().getByName(WebpackTask.TASK_NAME));
      }
    }
  }

}
