package tv.mechjack.mechjackbot.feature.autotrigger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tv.mechjack.mechjackbot.api.ChatCommand;
import tv.mechjack.mechjackbot.api.ChatCommandRegistry;
import tv.mechjack.mechjackbot.api.ChatCommandTrigger;
import tv.mechjack.mechjackbot.api.ChatMessageEvent;
import tv.mechjack.mechjackbot.feature.autotrigger.ProtoMessage.AutoTriggerList;
import tv.mechjack.mechjackbot.feature.autotrigger.ProtoMessage.AutoTriggerListKey;
import tv.mechjack.mechjackbot.feature.autotrigger.ProtoMessage.CommandTrigger;
import tv.mechjack.platform.utils.ExecutionUtils;
import tv.mechjack.platform.utils.RandomUtils;
import tv.mechjack.platform.utils.TimeUtils;
import tv.mechjack.twitchclient.TwitchLogin;

public class DefaultAutoTriggerService implements AutoTriggerService {

  private static final Logger LOGGER = LoggerFactory
      .getLogger(DefaultAutoTriggerService.class);

  private final AutoTriggerDataStore autoTriggerDataStore;
  private final ChatCommandRegistry chatCommandRegistry;
  private final ExecutionUtils executionUtils;
  private final RandomUtils randomUtils;
  private final TimeUtils timeUtils;
  private final Map<ListName, AutoTriggerMonitor> autoTriggerMonitorMap;

  @Inject
  DefaultAutoTriggerService(final AutoTriggerDataStore autoTriggerDataStore,
      final ChatCommandRegistry chatCommandRegistry,
      final ExecutionUtils executionUtils, final RandomUtils randomUtils,
      final TimeUtils timeUtils) {
    this.autoTriggerDataStore = autoTriggerDataStore;
    this.chatCommandRegistry = chatCommandRegistry;
    this.executionUtils = executionUtils;
    this.randomUtils = randomUtils;
    this.timeUtils = timeUtils;
    this.autoTriggerMonitorMap = new ConcurrentHashMap<>();
    this.createExistingAutoTriggerMonitors();
  }

  private void createExistingAutoTriggerMonitors() {
    for (final AutoTriggerListKey key : this.autoTriggerDataStore.getKeys()) {
      final Optional<AutoTriggerList> optionalAutoTriggerList = this.autoTriggerDataStore
          .get(key);

      if (optionalAutoTriggerList.isPresent()) {
        final AutoTriggerList autoTriggerList = optionalAutoTriggerList.get();
        final ListName listName = ListName.of(autoTriggerList.getListName());
        final AutoTriggerMonitor autoTriggerMonitor = new AutoTriggerMonitor(
            autoTriggerList, this.chatCommandRegistry,
            this.randomUtils, this.timeUtils);

        this.autoTriggerMonitorMap.put(listName, autoTriggerMonitor);
      }
    }
  }

  @Override
  public boolean isExistingAutoTriggerList(final ListName listName) {
    Objects.requireNonNull(listName,
        this.executionUtils.nullMessageForName("listName"));

    final AutoTriggerListKey key = this.autoTriggerDataStore
        .createAutoTriggerListKey(listName);

    return this.autoTriggerDataStore.containsKey(key);
  }

  @Override
  public List<ListName> getAutoTriggerListNames() {
    final List<ListName> names = new ArrayList<>();

    for (final AutoTriggerListKey key : this.autoTriggerDataStore.getKeys()) {
      names.add(ListName.of(key.getListName()));
    }
    return names;
  }

  @Override
  public void createAutoTriggerList(final ListName listName,
      final TimeRequired timeRequired, final MessageCount messageCount,
      final ChatterCount chatterCount, final Order order,
      final List<ChatCommandTrigger> commandTriggers) {
    Objects.requireNonNull(listName,
        this.executionUtils.nullMessageForName("listName"));
    Objects.requireNonNull(commandTriggers,
        this.executionUtils.nullMessageForName("commandTriggers"));
    if (this.isExistingAutoTriggerList(listName)) {
      throw new IllegalStateException(
          "Auto-trigger list with name already exists," + listName);
    } else if (commandTriggers.isEmpty()) {
      throw new IllegalArgumentException(
          "`commandTriggers` **MUST** not be empty");
    }

    final AutoTriggerListKey key = this.autoTriggerDataStore
        .createAutoTriggerListKey(listName);
    // TODO (2019-03-01 mechjack): Make defaults configurable
    final TimeRequired actualTimeRequired = Objects.isNull(timeRequired) ? TimeRequired.of(10) : timeRequired;
    final MessageCount actualMessageCount = Objects.isNull(messageCount) ? MessageCount.of(1) : messageCount;
    final ChatterCount actualChatterCount = Objects.isNull(chatterCount) ? ChatterCount.of(1) : chatterCount;
    final Order actualOrder = Objects.isNull(order) ? Order.SEQUENCE : order;
    final AutoTriggerList autoTriggerList = this.autoTriggerDataStore
        .createAutoTriggerList(listName, actualTimeRequired, actualMessageCount,
            actualChatterCount, actualOrder,
            this.convertToCommandTriggers(commandTriggers));

    this.autoTriggerDataStore.put(key, autoTriggerList);
    this.autoTriggerMonitorMap.put(listName,
        new AutoTriggerMonitor(autoTriggerList, this.chatCommandRegistry,
            this.randomUtils, this.timeUtils));
    LOGGER.info("Created auto-trigger list, " + listName);
  }

  private List<CommandTrigger> convertToCommandTriggers(
      final List<ChatCommandTrigger> chatCommandTriggers) {
    final List<CommandTrigger> commandTriggers = new ArrayList<>();

    if (Objects.nonNull(chatCommandTriggers)) {
      for (final ChatCommandTrigger trigger : chatCommandTriggers) {
        commandTriggers.add(
            this.autoTriggerDataStore.createCommandTrigger(trigger));
      }
      return commandTriggers;
    }
    return null;
  }

  @Override
  public void updateAutoTriggerList(final ListName listName,
      final TimeRequired timeRequired, final MessageCount messageCount,
      final ChatterCount chatterCount, final Order order,
      final List<ChatCommandTrigger> commandTriggers) {

    Objects.requireNonNull(listName,
        this.executionUtils.nullMessageForName("listName"));

    if (Objects.isNull(timeRequired) && Objects.isNull(messageCount)
        && Objects.isNull(chatterCount) && Objects.isNull(order)
        && Objects.isNull(commandTriggers)) {
      LOGGER.debug("No updates needed for auto-trigger list, " + listName);
      return;
    } else if (Objects.nonNull(commandTriggers) && commandTriggers.isEmpty()) {
      throw new IllegalArgumentException(
          "`commandTriggers` **MUST** not be empty");
    }

    final AutoTriggerListKey key = this.autoTriggerDataStore
        .createAutoTriggerListKey(listName);
    final Optional<AutoTriggerList> optionalAutoTriggerList = this.autoTriggerDataStore
        .get(key);

    if (optionalAutoTriggerList.isPresent()) {
      final AutoTriggerList autoTriggerList = optionalAutoTriggerList.get();
      final TimeRequired actualTimeRequired = this
          .getTimeRequired(autoTriggerList, timeRequired);
      final MessageCount actualMessageCount = this
          .getMessageCount(autoTriggerList, messageCount);
      final ChatterCount actualChatterCount = this
          .getChatterCount(autoTriggerList, chatterCount);
      final Order actualOrder = this.getOrder(autoTriggerList, order);
      final List<CommandTrigger> actualCommandTriggers = this
          .getCommandTriggers(autoTriggerList,
              this.convertToCommandTriggers(commandTriggers));
      final AutoTriggerList newAutoTriggerList = this.autoTriggerDataStore
          .createAutoTriggerList(listName, actualTimeRequired,
              actualMessageCount, actualChatterCount, actualOrder,
              actualCommandTriggers);

      this.autoTriggerDataStore.put(key, newAutoTriggerList);
      this.autoTriggerMonitorMap.put(listName,
          new AutoTriggerMonitor(newAutoTriggerList, this.chatCommandRegistry,
              this.randomUtils, this.timeUtils));
      LOGGER.info("Updated auto-trigger list, " + listName);
    } else {
      throw new IllegalStateException(
          "No existing auto-trigger list to update, " + listName);
    }
  }

  private TimeRequired getTimeRequired(
      final AutoTriggerList autoTriggerList,
      final TimeRequired timeRequired) {
    return Objects.isNull(timeRequired) ? TimeRequired.of(autoTriggerList.getTimeRequired()) : timeRequired;
  }

  private MessageCount getMessageCount(
      final AutoTriggerList autoTriggerList,
      final MessageCount messageCount) {
    return Objects.isNull(messageCount) ? MessageCount.of(autoTriggerList.getMessageCount()) : messageCount;
  }

  private ChatterCount getChatterCount(
      final AutoTriggerList autoTriggerList,
      final ChatterCount chatterCount) {
    return Objects.isNull(chatterCount) ? ChatterCount.of(autoTriggerList.getChatterCount()) : chatterCount;
  }

  private Order getOrder(final AutoTriggerList autoTriggerList,
      final Order order) {
    return Objects.isNull(order) ? Order.valueOf(autoTriggerList.getOrder())
        : order;
  }

  private List<CommandTrigger> getCommandTriggers(
      final AutoTriggerList autoTriggerList,
      final List<CommandTrigger> commandTriggers) {
    return Objects.isNull(commandTriggers) ? autoTriggerList.getTriggersList() : commandTriggers;
  }

  @Override
  public void removeAutoTriggerList(final ListName listName) {
    Objects.requireNonNull(listName,
        this.executionUtils.nullMessageForName("listName"));

    final AutoTriggerListKey key = this.autoTriggerDataStore
        .createAutoTriggerListKey(listName);
    final Optional<AutoTriggerList> optionalAutoTriggerList = this.autoTriggerDataStore
        .get(key);

    if (optionalAutoTriggerList.isPresent()) {
      this.autoTriggerDataStore.remove(key);
      this.autoTriggerMonitorMap.remove(listName);
      LOGGER.info("Removed auto-trigger list, " + listName);
    } else {
      throw new IllegalStateException(
          "No existing auto-trigger list to remove, " + listName);
    }
  }

  @Override
  public void handleMessageEvent(final ChatMessageEvent chatMessageEvent) {
    for (final AutoTriggerMonitor monitor : this.autoTriggerMonitorMap
        .values()) {
      monitor.handleMessageEvent(chatMessageEvent);
    }
  }

  private static final class AutoTriggerMonitor {

    private final AutoTriggerList autoTriggerList;
    private final ChatCommandRegistry chatCommandRegistry;
    private final RandomUtils randomUtils;
    private final TimeUtils timeUtils;

    private final Set<TwitchLogin> chatters;
    private int lastIndex;
    private long lastTriggered;
    private int messageCount;

    public AutoTriggerMonitor(final AutoTriggerList autoTriggerList,
        final ChatCommandRegistry chatCommandRegistry,
        final RandomUtils randomUtils, final TimeUtils timeUtils) {
      this.autoTriggerList = autoTriggerList;
      this.chatCommandRegistry = chatCommandRegistry;
      this.randomUtils = randomUtils;
      this.timeUtils = timeUtils;
      this.chatters = ConcurrentHashMap.newKeySet();
      this.triggered(this.autoTriggerList.getTriggersCount());
    }

    private void triggered(final int lastIndex) {
      this.lastIndex = lastIndex;
      this.lastTriggered = this.timeUtils.currentTime();
      this.messageCount = 0;
      this.chatters.clear();
    }

    public final void handleMessageEvent(
        final ChatMessageEvent chatMessageEvent) {
      this.messageCount++;
      this.chatters.add(chatMessageEvent.getChatUser().getTwitchLogin());
      if (this.timeUtils.currentTime() >= this.lastTriggered
          + TimeUnit.MINUTES.toMillis(this.autoTriggerList.getTimeRequired())
          && this.messageCount >= this.autoTriggerList.getMessageCount()
          && this.chatters.size() >= this.autoTriggerList.getChatterCount()) {
        final int index = this.getTriggerIndex();
        final ChatCommandTrigger trigger = ChatCommandTrigger.of(
            this.autoTriggerList.getTriggers(index).getName());
        final Optional<ChatCommand> optionalCommand = this.chatCommandRegistry
            .getCommand(trigger);

        if (optionalCommand.isPresent()) {
          this.triggered(index);
          optionalCommand.get().handleMessageEvent(chatMessageEvent);
        }
      }
    }

    private int getTriggerIndex() {
      final Order order = Order.valueOf(this.autoTriggerList.getOrder());

      LOGGER.info("Order: " + order.toString());
      if (Order.SEQUENCE.equals(order)) {
        this.lastIndex++;
        if (this.lastIndex >= this.autoTriggerList.getTriggersCount()) {
          this.lastIndex = 0;
        }
        return this.lastIndex;
      }
      return this.randomUtils
          .nextInteger(this.autoTriggerList.getTriggersCount());
    }

  }

}
