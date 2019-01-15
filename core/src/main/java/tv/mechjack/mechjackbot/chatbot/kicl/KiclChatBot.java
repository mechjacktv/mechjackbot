package tv.mechjack.mechjackbot.chatbot.kicl;

import java.util.Objects;

import javax.inject.Inject;

import org.kitteh.irc.client.library.Client;

import tv.mechjack.mechjackbot.api.ChatBot;
import tv.mechjack.mechjackbot.api.ChatChannel;
import tv.mechjack.mechjackbot.api.ChatMessage;
import tv.mechjack.platform.configuration.Configuration;
import tv.mechjack.platform.util.ExecutionUtils;

public class KiclChatBot implements ChatBot {

  public static final String DEFAULT_CHAT_BOT_MESSAGE_FORMAT = "/me MrDestructoid > %s";
  public static final String DEFAULT_SHUTDOWN_MESSAGE = "Shutdown";
  public static final String KEY_CHAT_BOT_MESSAGE_FORMAT = "chat_bot.message_format";
  public static final String KEY_SHUTDOWN_MESSAGE = "chat_bot.shutdown.message";

  private final Configuration configuration;
  private final ExecutionUtils executionUtils;
  private final Client ircClient;

  @Inject
  KiclChatBot(final Configuration configuration, final ExecutionUtils executionUtils, final Client ircClient) {
    this.configuration = configuration;
    this.executionUtils = executionUtils;
    this.ircClient = ircClient;
  }

  @Override
  public void sendMessage(final ChatChannel chatChannel, final ChatMessage chatMessage) {
    Objects.requireNonNull(chatChannel, this.executionUtils.nullMessageForName("chatChannel"));
    Objects.requireNonNull(chatMessage, this.executionUtils.nullMessageForName("chatMessage"));
    this.ircClient.sendMessage(chatChannel.value, String.format(this.configuration.get(KEY_CHAT_BOT_MESSAGE_FORMAT,
        DEFAULT_CHAT_BOT_MESSAGE_FORMAT), chatMessage));
  }

  @Override
  public void start() {
    this.ircClient.connect();
  }

  @Override
  public void stop() {
    this.ircClient.shutdown(this.configuration.get(KEY_SHUTDOWN_MESSAGE, DEFAULT_SHUTDOWN_MESSAGE));
  }

}
