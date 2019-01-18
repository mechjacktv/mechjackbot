package tv.mechjack.mechjackbot.feature.wouldyourather;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class TestQuestionsDataSource implements QuestionsDataSource {

  private final List<String> questions = new ArrayList<>();

  @Override
  public BufferedReader getQuestions() {
    final byte[] questionBytes = String.join("\n", questions).getBytes();
    final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(questionBytes);

    return new BufferedReader(new InputStreamReader(byteArrayInputStream));
  }

  public final void addQuestion(final String question) {
    this.questions.add(question);
  }

}
