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
        final CasterServiceMessage.CasterKey casterKey = createCasterKey(casterName);
        final Optional<byte[]> casterBytes = this.casters.get(casterKey.toByteArray());

        if (casterBytes.isPresent()) {
            final long now = System.currentTimeMillis();
            final CasterServiceMessage.Caster caster =
                    protobufUtils.parseMessage(CasterServiceMessage.Caster.class, casterBytes.get());

            return now - caster.getLastShoutOut() > TWELVE_HOURS;
        }
        return false;
    }

    final void removeCaster(final String casterName) {
        final CasterServiceMessage.CasterKey casterKey = createCasterKey(casterName);

        this.casters.remove(casterKey.toByteArray());
    }

    final void sendCasterShoutOut(final MessageEvent messageEvent, final String casterName) {
        messageEvent.sendResponse(String.format("Fellow caster in the stream! " +
                        "Everyone, please give a warm welcome to @%s. " +
                        "It would be great if you checked them out " +
                        "and gave them a follow. https://twitch.tv/%s",
                casterName, casterName));
        setCaster(casterName, System.currentTimeMillis());
    }

    final void setCaster(final String casterName, final long lastShoutOut) {
        final CasterServiceMessage.CasterKey casterKey = createCasterKey(casterName);
        final CasterServiceMessage.Caster caster = createCaster(casterName, lastShoutOut);

        this.casters.put(casterKey.toByteArray(), caster.toByteArray());
    }

    private CasterServiceMessage.CasterKey createCasterKey(final String casterName) {
        final CasterServiceMessage.CasterKey.Builder builder = CasterServiceMessage.CasterKey.newBuilder();

        return builder.setName(casterName)
                .build();
    }

    private CasterServiceMessage.Caster createCaster(final String casterName, final Long lastShoutOut) {
        final CasterServiceMessage.Caster.Builder builder = CasterServiceMessage.Caster.newBuilder();

        return builder.setName(casterName)
                .setLastShoutOut(lastShoutOut)
                .build();
    }

}
