package tv.mechjack.mechjackbot.feature.wouldyourather;

import java.io.IOException;
import java.io.Reader;

public interface QuestionsDataSource {

  Reader getQuestions() throws IOException;

}
