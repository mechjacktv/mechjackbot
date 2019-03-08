package tv.mechjack.mechjackbot.feature.autotrigger;

import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import tv.mechjack.mechjackbot.api.ChatCommandTrigger;
import tv.mechjack.mechjackbot.feature.autotrigger.ProtoMessage.AutoTriggerList;
import tv.mechjack.mechjackbot.feature.autotrigger.ProtoMessage.AutoTriggerListKey;
import tv.mechjack.mechjackbot.feature.autotrigger.ProtoMessage.CommandTrigger;
import tv.mechjack.platform.keyvaluestore.KeyValueStoreFactory;
import tv.mechjack.platform.protobuf.BaseMessageStore;
import tv.mechjack.platform.protobuf.ProtobufUtils;
import tv.mechjack.platform.utils.ExecutionUtils;

public class DefaultAutoTriggerDataStore
    extends BaseMessageStore<AutoTriggerListKey, AutoTriggerList>
    implements AutoTriggerDataStore {

  private final ExecutionUtils executionUtils;

  @Inject
  protected DefaultAutoTriggerDataStore(
      final KeyValueStoreFactory keyValueStoreFactory,
      final ExecutionUtils executionUtils,
      final ProtobufUtils protobufUtils) {
    super(keyValueStoreFactory.createOrOpenKeyValueStore(
        DefaultAutoTriggerDataStore.class.getCanonicalName()), executionUtils,
        protobufUtils);
    this.executionUtils = executionUtils;
  }

  @Override
  public AutoTriggerListKey createAutoTriggerListKey(
      final ListName listName) {
    Objects.requireNonNull(listName,
        this.executionUtils.nullMessageForName("listName"));
    return AutoTriggerListKey.newBuilder()
        .setListName(listName.value).build();
  }

  @Override
  public AutoTriggerList createAutoTriggerList(
      final ListName listName, final TimeRequired timeRequired,
      final MessageCount messageCount, final ChatterCount chatterCount,
      final Order order, final List<CommandTrigger> commandTriggers) {
    Objects.requireNonNull(listName,
        this.executionUtils.nullMessageForName("listName"));
    Objects.requireNonNull(timeRequired,
        this.executionUtils.nullMessageForName("timeRequired"));
    Objects.requireNonNull(messageCount,
        this.executionUtils.nullMessageForName("messageCount"));
    Objects.requireNonNull(chatterCount,
        this.executionUtils.nullMessageForName("chatterCount"));
    Objects.requireNonNull(order,
        this.executionUtils.nullMessageForName("order"));
    Objects.requireNonNull(commandTriggers,
        this.executionUtils.nullMessageForName("commandTriggers"));
    if (commandTriggers.isEmpty()) {
      throw new IllegalArgumentException(
          "`commandTriggers` **MUST** not be empty");
    }
    return AutoTriggerList.newBuilder()
        .setListName(listName.value)
        .setTimeRequired(timeRequired.value)
        .setMessageCount(messageCount.value)
        .setChatterCount(chatterCount.value)
        .setOrder(order.toString())
        .addAllTriggers(commandTriggers)
        .build();
  }

  @Override
  public CommandTrigger createCommandTrigger(final ChatCommandTrigger trigger) {
    Objects.requireNonNull(trigger,
        this.executionUtils.nullMessageForName("trigger"));
    return CommandTrigger.newBuilder().setName(trigger.value).build();
  }

  @Override
  protected Class<AutoTriggerListKey> getKeyClass() {
    return AutoTriggerListKey.class;
  }

  @Override
  protected Class<AutoTriggerList> getValueClass() {
    return AutoTriggerList.class;
  }

}
