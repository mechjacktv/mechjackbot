package tv.mechjack.gradle.react;

import javax.inject.Inject;

import org.gradle.api.Project;
import org.gradle.api.tasks.Copy;

public class CopyCssTask extends Copy {

  public static final String TASK_NAME = "copyCss";
  public static final String TEMP_DIR = "build/tmp";
  public static final String MAIN_SRC_DIR = "src/main/react";
  public static final String BABEL_OUTPUT_DIR = TEMP_DIR + "/react/output";

  private final Project project;
  private final TaskUtils taskUtils;

  @Inject
  public CopyCssTask(final Project project) {
    this.project = project;
    this.taskUtils = new TaskUtils(project);

    from(MAIN_SRC_DIR);
    include("*.css");
    into(BABEL_OUTPUT_DIR);
  }

}
