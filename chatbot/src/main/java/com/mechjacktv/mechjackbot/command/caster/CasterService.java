package com.mechjacktv.mechjackbot.command.caster;

import com.mechjacktv.mechjackbot.KeyValueStore;
import com.mechjacktv.mechjackbot.KeyValueStoreFactory;
import com.mechjacktv.mechjackbot.MessageEvent;
import com.mechjacktv.util.ProtobufUtils;

import javax.inject.Inject;
import java.util.Optional;

public final class CasterService {

    private static final long TWELVE_HOURS = 1000 * 60 * 60 * 12;

    private final KeyValueStore casters;
    private final ProtobufUtils protobufUtils;

    @Inject
    public CasterService(final KeyValueStoreFactory keyValueStoreFactory, final ProtobufUtils protobufUtils) {
        this.casters = keyValueStoreFactory.createOrOpenKeyValueStore("casters");
        this.protobufUtils = protobufUtils;
    }

    final boolean isCasterDue(final String casterName) {
        final Optional<byte[]> casterBytes = this.casters.get(casterName.getBytes());

        if(casterBytes.isPresent()) {
            final long now = System.currentTimeMillis();
            final CasterMessages.Caster caster =
                    protobufUtils.parseMessage(CasterMessages.Caster.class, casterBytes.get());

            return now - caster.getLastShoutOut() > TWELVE_HOURS;
        }
        return false;
    }

    final void removeCaster(final String casterName) {
        this.casters.remove(casterName.getBytes());
    }

    final void setCaster(final String casterName, final long lastShoutOut) {
        final CasterMessages.Caster caster = CasterMessages.Caster.newBuilder()
                .setName(casterName)
                .setLastShoutOut(lastShoutOut)
                .build();
        this.casters.put(casterName.getBytes(), caster.toByteArray());
    }

    final void sendCasterShoutOut(final MessageEvent messageEvent, final String casterName) {
        messageEvent.sendResponse(String.format(
                "Fellow caster in the stream! " +
                "Everyone, please give a warm welcome to @%s. " +
                "It would be great if you checked them out " +
                "and gave them a follow too. https://twitch.tv/%s",
                casterName, casterName));
        setCaster(casterName, System.currentTimeMillis());
    }

}
