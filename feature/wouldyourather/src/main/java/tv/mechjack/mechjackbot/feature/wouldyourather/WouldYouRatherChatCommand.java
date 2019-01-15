package tv.mechjack.mechjackbot.feature.wouldyourather;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.inject.Inject;

import com.google.common.base.Strings;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tv.mechjack.mechjackbot.api.BaseChatCommand;
import tv.mechjack.mechjackbot.api.ChatMessageEvent;
import tv.mechjack.mechjackbot.api.CommandConfigurationBuilder;
import tv.mechjack.platform.application.Application;

public class WouldYouRatherChatCommand extends BaseChatCommand {

  private static final Logger LOGGER = LoggerFactory.getLogger(WouldYouRatherChatCommand.class);
  private static final Random RANDOM = new Random();

  public static final String DEFAULT_DESCRIPTION = "Asks a 'would you rather' style question in chat.";
  public static final String DEFAULT_MESSAGE_FORMAT = "Would your rather %s?";
  public static final String DEFAULT_TRIGGER = "!wyr";
  public static final String QUESTIONS_FILE_NAME = "wyr.questions";

  private final List<String> questions;

  @Inject
  protected WouldYouRatherChatCommand(final Application application,
      final CommandConfigurationBuilder commandConfigurationBuilder) throws IOException {
    super(commandConfigurationBuilder.setTrigger(DEFAULT_TRIGGER)
        .setDescription(DEFAULT_DESCRIPTION)
        .setMessageFormat(DEFAULT_MESSAGE_FORMAT));
    this.questions = new ArrayList<>();
    this.loadQuestions(application);
  }

  private void loadQuestions(final Application application) throws IOException {
    final File questionsFile = new File(application.getApplicationDataLocation().value, QUESTIONS_FILE_NAME);

    if (questionsFile.exists()) {
      try (final BufferedReader in = Files.newBufferedReader(questionsFile.toPath())) {
        String line;

        while ((line = in.readLine()) != null) {
          line = line.trim();
          if (Strings.isNullOrEmpty(line) || line.startsWith("#")) {
            continue;
          }
          if (line.endsWith("?")) {
            line = line.substring(0, line.length() - 1);
          }
          if (line.toLowerCase().startsWith("would you rather ")) {
            line = line.substring(17);
          }
          this.questions.add(line);
        }
      }
      LOGGER.info(String.format("Loaded %d would you rather questions.", this.questions.size()));
    } else {
      LOGGER.warn("Unable to find questions file at " + questionsFile.getCanonicalPath());
    }
  }

  @Override
  public void handleMessageEvent(final ChatMessageEvent chatMessageEvent) {
    this.sendResponse(chatMessageEvent, this.questions.get(RANDOM.nextInt(this.questions.size())));
  }

}
