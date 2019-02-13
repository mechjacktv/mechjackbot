package tv.mechjack.gradle.react;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.Scanner;

import javax.inject.Inject;

import org.gradle.api.DefaultTask;
import org.gradle.api.Project;
import org.gradle.api.tasks.TaskAction;

public class NpmInstallTask extends DefaultTask {

  public static final String TASK_NAME = "npmInstall";

  private final Project project;
  private final TaskUtils taskUtils;

  @Inject
  public NpmInstallTask(final Project project) {
    this.project = project;
    this.taskUtils = new TaskUtils(project);
  }

  @TaskAction
  public final void installPackages() {
    try {
      final ProcessBuilder builder = new ProcessBuilder("npm", "install")
          .directory(project.getProjectDir());

      createPackageJson(this.taskUtils.getAbsolutePath("package.json"));
      this.taskUtils.monitorProcess("NpmInstall", builder.start());

      final Process npmInstallProcess = builder.start();
      final Scanner scanner = new Scanner(npmInstallProcess.getInputStream());

      while (scanner.hasNextLine()) {
        System.out.println(scanner.nextLine());
      }
      scanner.close();
      npmInstallProcess.waitFor();
    } catch (final IOException | InterruptedException | ProcessExecutionException e) {
      throw new RuntimeException(e.getMessage(), e);
    }
  }

  public final void createPackageJson(final String location) {
    try {
      final StringBuilder builder = new StringBuilder();

      builder.append("{\n");
      builder.append("  \"name\": \"" + project.getName() + "\",\n");
      builder.append("  \"version\": \"0.1.0\",\n");
      builder.append("  \"private\": true,\n");
      builder.append("  \"dependencies\": {\n");
      builder.append("    \"react\": \"^16.7.0\",\n");
      builder.append("    \"react-dom\": \"^16.7.0\"\n");
      builder.append("  },\n");
      builder.append("  \"devDependencies\": {\n");
      builder.append("    \"@babel/cli\": \"^7.2.3\",\n");
      builder.append("    \"@babel/core\": \"^7.2.2\",\n");
      builder.append("    \"@babel/preset-react\": \"^7.0.0\",\n");
      builder.append("    \"@babel/preset-env\": \"^7.3.1\",\n");
      builder.append("    \"babel-loader\": \"^8.0.5\",\n");
      builder.append("    \"webpack\": \"^4.29.0\",\n");
      builder.append("    \"webpack-cli\": \"^3.2.1\",\n");
      builder.append("    \"css-loader\": \"^2.1.0\",\n");
      builder.append("    \"html-webpack-plugin\": \"^3.2.0\",\n");
      builder.append("    \"style-loader\": \"^0.23.1\"\n");
      builder.append("  },\n");
      builder.append("  \"browserslist\": [\n");
      builder.append("    \">0.2%\",\n");
      builder.append("    \"not dead\",\n");
      builder.append("    \"not ie <= 11\",\n");
      builder.append("    \"not op_mini all\"\n");
      builder.append("  ]\n");
      builder.append("}\n");
      Files.write(new File(location).toPath(), builder.toString().getBytes(),
          StandardOpenOption.CREATE);
    } catch (final IOException e) {
      throw new RuntimeException(e.getMessage(), e);
    }
  }

}
