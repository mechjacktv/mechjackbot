package tv.mechjack.mechjackbot.feature.wouldyourather;

import java.io.BufferedReader;
import java.io.IOException;

public interface QuestionsDataSource {

  BufferedReader getQuestions() throws IOException;

}
