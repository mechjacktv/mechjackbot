package tv.mechjack.gradle.testframework;

import java.io.File;

import javax.inject.Inject;

import org.gradle.api.DefaultTask;
import org.gradle.api.Project;
import org.gradle.api.tasks.TaskAction;

public class InitTestFrameworkTask extends DefaultTask {

  public static final String TASK_NAME = "initTestFramework";

  private final Project project;

  @Inject
  public InitTestFrameworkTask(final Project project) {
    this.project = project;
    this.setGroup("Init");
    this.setDescription("Creates useful files and directories for using the test framework plugin.");
  }

  @TaskAction
  public final void execute() {
    new File(this.project.getRootDir(), "src/testFramework/java").mkdirs();
  }

}
