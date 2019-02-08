package tv.mechjack.gradle.testframework;

import javax.inject.Inject;

import org.gradle.api.Project;
import org.gradle.api.plugins.JavaPluginConvention;
import org.gradle.jvm.tasks.Jar;

public class TestFrameworkJarTask extends Jar {

  public static final String TASK_NAME = "testFrameworkJar";

  @Inject
  public TestFrameworkJarTask(final Project project) {
    final JavaPluginConvention javaPluginConvention = project.getConvention().getPlugin(JavaPluginConvention.class);

    this.setGroup("Build");
    this.setDescription("Assembles a jar archive containing the test framework classes.");
    this.from(javaPluginConvention.getSourceSets()
        .getByName("testFramework").getOutput());
    this.getArchiveClassifier().set("testFramework");
  }

}
