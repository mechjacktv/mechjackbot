package tv.mechjack.gradle.react;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.NoSuchElementException;
import java.util.Scanner;

import javax.inject.Inject;

import org.gradle.api.DefaultTask;
import org.gradle.api.Project;
import org.gradle.api.tasks.TaskAction;
import org.gradle.api.tasks.TaskExecutionException;

public class CompileJavaScriptTask extends DefaultTask {

  public static final String TASK_NAME = "compileJavaScript";
  public static final String TEMP_DIR = "build/tmp";
  public static final String MAIN_SRC_DIR = "src/main/react";
  public static final String BABEL_OUTPUT_DIR = TEMP_DIR + "/react/output";
  public static final String BABEL_CONFIG_FILE = TEMP_DIR + "/react/babel.config.js";

  private final Project project;
  private final TaskUtils taskUtils;

  @Inject
  public CompileJavaScriptTask(final Project project) {
    this.dependsOn(project.getTasks().getByName(NpmInstallTask.TASK_NAME));
    this.project = project;
    this.taskUtils = new TaskUtils(project);
    this.getOutputs().dir(BABEL_OUTPUT_DIR);
  }

  @TaskAction
  public final void compileJavaScript() {
    try {
      new File(this.taskUtils.getAbsolutePath(BABEL_OUTPUT_DIR)).mkdirs();

      final ProcessBuilder babelProcess = new ProcessBuilder("npx", "babel", "--config-file",
          this.taskUtils.getAbsolutePath(BABEL_CONFIG_FILE), this.taskUtils.getAbsolutePath(MAIN_SRC_DIR), "--out-dir",
          this.taskUtils.getAbsolutePath(BABEL_OUTPUT_DIR)).directory(project.getProjectDir());

      writeBabelConfig(this.taskUtils.getAbsolutePath(BABEL_CONFIG_FILE));
      this.taskUtils.monitorProcess("Babel", babelProcess.start());
    } catch (final IOException | InterruptedException | ProcessExecutionException e) {
      throw new TaskExecutionException(this, e);
    }  }

  private void writeBabelConfig(final String location) throws IOException {

    final StringBuilder builder = new StringBuilder();

    builder.append("const presets = [\n");
    builder.append("  [\n");
    builder.append("    \"@babel/preset-env\", {\n");
    builder.append("      targets: \"> 0.25%, not dead\",\n");
    builder.append("      useBuiltIns: \"entry\"\n");
    builder.append("    },\n");
    builder.append("  ],\n");
    builder.append("  [ \"@babel/preset-react\" ]\n");
    builder.append("];\n");
    builder.append("module.exports = { presets }\n");
    Files.write(new File(location).toPath(), builder.toString().getBytes(),
        StandardOpenOption.CREATE);

  }

}
