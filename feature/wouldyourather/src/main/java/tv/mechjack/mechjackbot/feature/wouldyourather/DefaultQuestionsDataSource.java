package tv.mechjack.mechjackbot.feature.wouldyourather;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;

import tv.mechjack.platform.application.Application;

public class DefaultQuestionsDataSource implements QuestionsDataSource {

  public static final String QUESTIONS_FILE_NAME = "wouldyourather.questions";

  private final File questionsFile;

  DefaultQuestionsDataSource(final Application application) {
    this.questionsFile = new File(application.getApplicationDataLocation().value, QUESTIONS_FILE_NAME);
  }

  @Override
  public BufferedReader getQuestions() throws IOException {
    if (!questionsFile.exists()) {
      throw new FileNotFoundException(questionsFile.getCanonicalPath());
    }
    return Files.newBufferedReader(this.questionsFile.toPath());
  }

}
