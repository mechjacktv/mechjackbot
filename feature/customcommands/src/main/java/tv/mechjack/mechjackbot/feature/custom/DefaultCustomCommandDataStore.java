package tv.mechjack.mechjackbot.feature.custom;

import java.util.Objects;

import javax.inject.Inject;

import tv.mechjack.mechjackbot.api.ChatCommandDescription;
import tv.mechjack.mechjackbot.api.ChatCommandTrigger;
import tv.mechjack.mechjackbot.api.UserRole;
import tv.mechjack.mechjackbot.feature.custom.ProtoMessage.CustomCommand;
import tv.mechjack.mechjackbot.feature.custom.ProtoMessage.CustomCommandKey;
import tv.mechjack.platform.keyvaluestore.KeyValueStoreFactory;
import tv.mechjack.platform.utils.ExecutionUtils;
import tv.mechjack.protobuf.BaseMessageStore;
import tv.mechjack.protobuf.ProtobufUtils;

public class DefaultCustomCommandDataStore
    extends BaseMessageStore<CustomCommandKey, CustomCommand>
    implements CustomCommandDataStore {

  private final ExecutionUtils executionUtils;

  @Inject
  protected DefaultCustomCommandDataStore(
      final KeyValueStoreFactory keyValueStoreFactory,
      final ExecutionUtils executionUtils, final ProtobufUtils protobufUtils) {
    super(keyValueStoreFactory.createOrOpenKeyValueStore(
        DefaultCustomCommandDataStore.class.getCanonicalName()),
        executionUtils, protobufUtils);
    this.executionUtils = executionUtils;
  }

  @Override
  protected Class<CustomCommandKey> getKeyClass() {
    return CustomCommandKey.class;
  }

  @Override
  protected Class<CustomCommand> getValueClass() {
    return CustomCommand.class;
  }

  @Override
  public CustomCommandKey createCustomCommandKey(
      final ChatCommandTrigger trigger) {
    Objects.requireNonNull(trigger,
        this.executionUtils.nullMessageForName("trigger"));
    return CustomCommandKey.newBuilder().setTrigger(trigger.value).build();
  }

  @Override
  public CustomCommand createCustomCommand(final ChatCommandTrigger trigger,
      final CommandBody commandBody,
      final ChatCommandDescription description, final UserRole userRole) {
    Objects.requireNonNull(trigger,
        this.executionUtils.nullMessageForName("trigger"));
    Objects.requireNonNull(commandBody,
        this.executionUtils.nullMessageForName("commandBody"));
    Objects.requireNonNull(description,
        this.executionUtils.nullMessageForName("description"));
    Objects.requireNonNull(userRole,
        this.executionUtils.nullMessageForName("userRole"));
    return CustomCommand.newBuilder().setTrigger(trigger.value)
        .setCommandBody(commandBody.value)
        .setDescription(description.value).setAccessLevel(userRole.toString())
        .build();
  }

}
