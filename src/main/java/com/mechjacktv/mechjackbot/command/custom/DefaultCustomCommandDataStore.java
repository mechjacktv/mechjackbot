package com.mechjacktv.mechjackbot.command.custom;

import java.util.Objects;

import javax.inject.Inject;

import com.mechjacktv.keyvaluestore.BaseMessageStore;
import com.mechjacktv.keyvaluestore.KeyValueStoreFactory;
import com.mechjacktv.mechjackbot.ChatCommandTrigger;
import com.mechjacktv.mechjackbot.UserRole;
import com.mechjacktv.proto.mechjackbot.command.custom.CustomComandDataStoreMessage.CustomCommand;
import com.mechjacktv.proto.mechjackbot.command.custom.CustomComandDataStoreMessage.CustomCommandKey;
import com.mechjacktv.util.ExecutionUtils;
import com.mechjacktv.util.ProtobufUtils;

public class DefaultCustomCommandDataStore extends BaseMessageStore<CustomCommandKey, CustomCommand>
    implements CustomCommandDataStore {

  private final ExecutionUtils executionUtils;

  @Inject
  protected DefaultCustomCommandDataStore(final KeyValueStoreFactory keyValueStoreFactory,
      final ExecutionUtils executionUtils, final ProtobufUtils protobufUtils) {
    super(keyValueStoreFactory.createOrOpenKeyValueStore(DefaultCustomCommandDataStore.class.getCanonicalName()),
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
  public CustomCommandKey createCustomCommandKey(final ChatCommandTrigger trigger) {
    Objects.requireNonNull(trigger, this.executionUtils.nullMessageForName("trigger"));
    return CustomCommandKey.newBuilder().setTrigger(trigger.value).build();
  }

  @Override
  public CustomCommand createCustomCommand(final ChatCommandTrigger trigger, final CommandBody commandBody,
      final UserRole userRole) {
    Objects.requireNonNull(trigger, this.executionUtils.nullMessageForName("trigger"));
    Objects.requireNonNull(commandBody, this.executionUtils.nullMessageForName("commandBody"));
    Objects.requireNonNull(userRole, this.executionUtils.nullMessageForName("userRole"));
    return CustomCommand.newBuilder().setTrigger(trigger.value).setCommandBody(commandBody.value)
        .setAccessLevel(userRole.toString()).build();
  }

}
