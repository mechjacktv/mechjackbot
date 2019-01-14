package tv.mechjack.mechjackbot.command.shoutout;

import java.util.Optional;

import javax.inject.Inject;

import tv.mechjack.configuration.Configuration;
import tv.mechjack.configuration.ConfigurationKey;
import tv.mechjack.mechjackbot.api.BaseChatCommand;
import tv.mechjack.mechjackbot.api.ChatCommand;
import tv.mechjack.mechjackbot.api.ChatCommandRegistry;
import tv.mechjack.mechjackbot.api.ChatMessage;
import tv.mechjack.mechjackbot.api.ChatMessageEvent;
import tv.mechjack.mechjackbot.api.ChatMessageEventWrapper;
import tv.mechjack.mechjackbot.api.CommandConfigurationBuilder;
import tv.mechjack.mechjackbot.command.shoutout.ProtoMessage.CasterKey;
import tv.mechjack.twitchclient.TwitchLogin;
import tv.mechjack.util.TimeUtils;

public final class ShoutOutListenerChatCommand extends BaseChatCommand {

  public static final String DEFAULT_DESCRIPTION = "Monitors chat looking for casters who are due for a shout out.";
  public static final String DEFAULT_FREQUENCY = "1";
  public static final String KEY_FREQUENCY = "frequency.hours";
  public static final String FORWARDED_MESSAGE_FORMAT = "%s %s";

  private final ChatCommandRegistry chatCommandRegistry;
  private final Configuration configuration;
  private final ShoutOutDataStore shoutOutDataStore;
  private final TimeUtils timeUtils;
  private ConfigurationKey frequencyKey;

  @Inject
  ShoutOutListenerChatCommand(final CommandConfigurationBuilder commandConfigurationBuilder,
      final ChatCommandRegistry chatCommandRegistry, final Configuration configuration,
      final ShoutOutDataStore shoutOutDataStore, final TimeUtils timeUtils) {
    super(commandConfigurationBuilder.setDescription(DEFAULT_DESCRIPTION));
    this.chatCommandRegistry = chatCommandRegistry;
    this.configuration = configuration;
    this.shoutOutDataStore = shoutOutDataStore;
    this.timeUtils = timeUtils;
    this.frequencyKey = ConfigurationKey.of(KEY_FREQUENCY, this.getClass());
  }

  @Override
  public final boolean isTriggered(final ChatMessageEvent chatMessageEvent) {
    return this.isCasterDue(chatMessageEvent);
  }

  private boolean isCasterDue(final ChatMessageEvent chatMessageEvent) {
    final Long frequency = this.timeUtils.hoursAsMs(
        Integer.parseInt(this.configuration.get(this.frequencyKey.value, DEFAULT_FREQUENCY)));
    final TwitchLogin twitchLogin = chatMessageEvent.getChatUser().getTwitchLogin();
    final CasterKey casterKey = this.shoutOutDataStore.createCasterKey(twitchLogin.value);

    return this.shoutOutDataStore.get(casterKey)
        .filter(caster -> this.timeUtils.currentTime() - caster.getLastShoutOut() > frequency).isPresent();
  }

  @Override
  public void handleMessageEvent(final ChatMessageEvent chatMessageEvent) {
    final TwitchLogin twitchLogin = chatMessageEvent.getChatUser().getTwitchLogin();
    final Optional<ChatCommand> shoutOutChatCommand = this.chatCommandRegistry.getCommand(ShoutOutChatCommand.class);

    shoutOutChatCommand.ifPresent(chatCommand -> {
      final ChatMessage chatMessage = ChatMessage
          .of(String.format(FORWARDED_MESSAGE_FORMAT, chatCommand.getTrigger(), twitchLogin));

      chatCommand.handleMessageEvent(new ShoutOutChatMessageEventWrapper(chatMessageEvent, chatMessage));
      this.shoutOutDataStore.put(this.shoutOutDataStore.createCasterKey(twitchLogin.value),
          this.shoutOutDataStore.createCaster(twitchLogin.value, this.timeUtils.currentTime()));
    });
  }

  private static final class ShoutOutChatMessageEventWrapper extends ChatMessageEventWrapper {

    private final ChatMessage chatMessage;

    public ShoutOutChatMessageEventWrapper(final ChatMessageEvent chatMessageEvent, final ChatMessage chatMessage) {
      super(chatMessageEvent);
      this.chatMessage = chatMessage;
    }

    @Override
    public ChatMessage getChatMessage() {
      return this.chatMessage;
    }

  }

}
