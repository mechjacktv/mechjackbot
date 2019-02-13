package tv.mechjack.gradle.react;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;

import javax.inject.Inject;

import org.gradle.api.DefaultTask;
import org.gradle.api.Project;
import org.gradle.api.tasks.TaskAction;
import org.gradle.api.tasks.TaskExecutionException;

public class WebpackTask extends DefaultTask {

  public static final String TASK_NAME = "webpack";
  public static final String TEMP_DIR = "build/tmp";
  public static final String MAIN_SRC_DIR = "src/main/react";
  public static final String MAIN_OUTPUT_DIR = "build/react/main";
  public static final String BABEL_OUTPUT_DIR = TEMP_DIR + "/react/output";
  public static final String WEBPACK_CONFIG_FILE = TEMP_DIR + "/react/webpack.config.js";

  private final Project project;
  private final TaskUtils taskUtils;

  @Inject
  public WebpackTask(final Project project) {
    this.project = project;
    this.taskUtils = new TaskUtils(project);
    this.setGroup("Build");
    this.setDescription("Assembles the ReactJS webapp.");
    this.dependsOn(project.getTasks().getByName(NpmInstallTask.TASK_NAME));
    this.dependsOn(project.getTasks().getByName(CompileJavaScriptTask.TASK_NAME));
    this.dependsOn(project.getTasks().getByName(CopyCssTask.TASK_NAME));
  }

  @TaskAction
  public final void webpackReactApplication() {
    try {
      new File(this.taskUtils.getAbsolutePath(MAIN_OUTPUT_DIR)).mkdirs();

      final ProcessBuilder webpackProcess = new ProcessBuilder("npx", "webpack", "--config",
          this.taskUtils.getAbsolutePath(WEBPACK_CONFIG_FILE)).directory(project.getProjectDir());

      writeWebpackConfig(this.taskUtils.getAbsolutePath(WEBPACK_CONFIG_FILE));
      this.taskUtils.monitorProcess("WebPack", webpackProcess.start());
    } catch (final IOException | InterruptedException | ProcessExecutionException e) {
      throw new TaskExecutionException(this, e);
    }
  }

  private void writeWebpackConfig(final String location) throws IOException {

    final StringBuilder builder = new StringBuilder();

    builder.append("const HtmlWebpackPlugin = require('html-webpack-plugin');\n");
    builder.append("const webpack = require('webpack');\n");
    // builder.append("const path = require('path');\n");

    builder.append("module.exports = {\n");
    builder.append("  mode: 'production',\n");
    builder.append("  entry: '" + this.taskUtils.getAbsolutePath(BABEL_OUTPUT_DIR) + "/index.js',\n");
    builder.append("  output: {\n");
    builder.append("    path: '" + this.taskUtils.getAbsolutePath(MAIN_OUTPUT_DIR) + "',\n");
    builder.append("    filename: 'index.js'\n");
    builder.append("  },\n");
    builder.append("  module: {\n");
    builder.append("    rules: [{\n");
    builder.append("      test: /\\.js$/,\n");
    builder.append("      exclude: '" + this.taskUtils.getAbsolutePath("node_modules") + "',\n");
    builder.append("      loader: \"babel-loader\"\n");
    builder.append("    },\n");
    builder.append("    {\n");
    builder.append("      test: /\\.css$/,\n");
    builder.append("      loader: [\"style-loader\", \"css-loader\"]\n");
    builder.append("    }]\n");
    builder.append("  },\n");
    builder.append("  plugins: [new HtmlWebpackPlugin({\n");
    builder.append("      template: '" + this.taskUtils.getAbsolutePath(MAIN_SRC_DIR) + "/index.html'\n");
    builder.append("  })]\n");
    builder.append("};\n");
    Files.write(new File(location).toPath(), builder.toString().getBytes(),
        StandardOpenOption.CREATE);
  }

}
