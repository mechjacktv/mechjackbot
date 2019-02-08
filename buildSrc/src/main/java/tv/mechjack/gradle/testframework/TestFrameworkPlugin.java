package tv.mechjack.gradle.testframework;

import java.util.Objects;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.plugins.JavaPluginConvention;
import org.gradle.api.tasks.SourceSet;
import org.gradle.jvm.tasks.Jar;

public class TestFrameworkPlugin implements Plugin<Project> {

  @Override
  public void apply(final Project project) {
    createTestFrameworkSourceSet(project);
    createTestFrameworkConfiguration(project);
    project.getTasks().register(InitTestFrameworkTask.TASK_NAME, InitTestFrameworkTask.class, project);
    project.getTasks().register(TestFrameworkJarTask.TASK_NAME, TestFrameworkJarTask.class, project);

    final Task assembleTask = project.getTasks().findByPath("assemble");

    if (Objects.nonNull(assembleTask)) {
      assembleTask.dependsOn(project.getTasks().getByName(TestFrameworkJarTask.TASK_NAME));
    }
    project.getArtifacts().add("testFramework",
        project.getTasks().getByName(TestFrameworkJarTask.TASK_NAME));
  }

  private void createTestFrameworkSourceSet(final Project project) {
    final JavaPluginConvention javaPluginConvention = project.getConvention().getPlugin(JavaPluginConvention.class);
    final SourceSet testFrameworkSourceSet = javaPluginConvention.getSourceSets()
        .create("testFramework",
            sourceSet -> {
              final SourceSet mainSourceSet = javaPluginConvention.getSourceSets().getByName("main");

              sourceSet.setCompileClasspath(sourceSet.getCompileClasspath().plus(mainSourceSet.getOutput()));
              sourceSet.setRuntimeClasspath(sourceSet.getRuntimeClasspath().plus(mainSourceSet.getOutput()));
            });
    final SourceSet testSourceSet = javaPluginConvention.getSourceSets().getByName("test");

    testSourceSet.setCompileClasspath(testSourceSet.getCompileClasspath().plus(testFrameworkSourceSet.getOutput()));
    testSourceSet.setRuntimeClasspath(testSourceSet.getRuntimeClasspath().plus(testFrameworkSourceSet.getOutput()));
  }

  private void createTestFrameworkConfiguration(final Project project) {
    project.getConfigurations().create("testFramework");

    project.getConfigurations().getByName("testFrameworkImplementation")
        .extendsFrom(project.getConfigurations().getByName("implementation"));

    project.getConfigurations().getByName("testFrameworkRuntimeOnly")
        .extendsFrom(project.getConfigurations().getByName("runtimeOnly"));

    project.getConfigurations().getByName("testImplementation")
        .extendsFrom(project.getConfigurations().getByName("testFrameworkImplementation"));

    project.getConfigurations().getByName("testRuntimeOnly")
        .extendsFrom(project.getConfigurations().getByName("testFrameworkRuntimeOnly"));
  }

}
