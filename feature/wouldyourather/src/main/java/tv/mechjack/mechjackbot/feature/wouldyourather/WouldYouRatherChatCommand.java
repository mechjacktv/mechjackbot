package tv.mechjack.mechjackbot.feature.wouldyourather;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import com.google.common.base.Strings;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tv.mechjack.mechjackbot.api.BaseChatCommand;
import tv.mechjack.mechjackbot.api.ChatMessageEvent;
import tv.mechjack.mechjackbot.api.CommandConfigurationBuilder;
import tv.mechjack.platform.application.Application;
import tv.mechjack.platform.utils.RandomUtils;

public class WouldYouRatherChatCommand extends BaseChatCommand {

  private static final Logger LOGGER = LoggerFactory.getLogger(WouldYouRatherChatCommand.class);

  public static final String DEFAULT_DESCRIPTION = "Asks a 'would you rather' style question in chat.";
  public static final String DEFAULT_MESSAGE_FORMAT = "Would your rather %s?";
  public static final String DEFAULT_TRIGGER = "!wouldyourather";

  private final List<String> questions;
  private final RandomUtils randomUtils;

  @Inject
  WouldYouRatherChatCommand(final CommandConfigurationBuilder commandConfigurationBuilder,
      final RandomUtils randomUtils, final Application application) {
    this(commandConfigurationBuilder, randomUtils, new DefaultQuestionsDataSource(application));
  }

  WouldYouRatherChatCommand(final CommandConfigurationBuilder commandConfigurationBuilder,
      final RandomUtils randomUtils, final QuestionsDataSource questionsDataSource) {
    super(commandConfigurationBuilder.setTrigger(DEFAULT_TRIGGER)
        .setDescription(DEFAULT_DESCRIPTION)
        .setMessageFormat(DEFAULT_MESSAGE_FORMAT));
    this.questions = new ArrayList<>();
    this.loadQuestions(questionsDataSource);
    this.randomUtils = randomUtils;
  }

  private void loadQuestions(final QuestionsDataSource questionsDataSource) {
    try (final BufferedReader in = questionsDataSource.getQuestions()) {
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
      LOGGER.info(String.format("Loaded %d would you rather questions.", this.questions.size()));
    } catch (final IOException e) {
      LOGGER.warn("Unable to load would you rather questions", e);
    }
  }

  @Override
  public void handleMessageEvent(final ChatMessageEvent chatMessageEvent) {
    if (this.questions.size() > 0) {
      this.sendResponse(chatMessageEvent, this.questions.get(randomUtils.nextInteger(this.questions.size())));
    }
  }

}
