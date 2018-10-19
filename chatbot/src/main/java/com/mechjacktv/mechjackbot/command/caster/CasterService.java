package com.mechjacktv.mechjackbot.command.caster;

import com.google.protobuf.InvalidProtocolBufferException;
import com.mechjacktv.mechjackbot.MessageEvent;
import com.mechjacktv.util.ProtobufUtils;
import org.mapdb.DB;
import org.mapdb.Serializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.concurrent.ConcurrentMap;

public class CasterService {

    private static final Logger log = LoggerFactory.getLogger(CasterService.class);

    private static final long TWELVE_HOURS = 1000 * 60 * 60 * 12;

    private final ConcurrentMap<String, byte[]> casters;
    private final ProtobufUtils protobufUtils;

    @Inject
    public CasterService(final DB mapDb, final ProtobufUtils protobufUtils) {
        this.casters = mapDb.hashMap("casters", Serializer.STRING, Serializer.BYTE_ARRAY).createOrOpen();
        this.protobufUtils = protobufUtils;
    }

    public boolean isCasterDue(final String casterName) {
        if (isExistingCaster(casterName)) {
            final long now = System.currentTimeMillis();
            final CasterMessages.Caster caster = protobufUtils.parseMessage(CasterMessages.Caster.class, this.casters.get(casterName));

            return now - caster.getLastShoutOut() > TWELVE_HOURS;
        }
        return false;
    }

    public final void removeCaster(final String casterName) {
        this.casters.remove(casterName);
    }

    public final void setCaster(final String casterName, final long lastShoutOut) {
        final CasterMessages.Caster caster = CasterMessages.Caster.newBuilder()
                .setName(casterName)
                .setLastShoutOut(lastShoutOut)
                .build();
        if (isExistingCaster(casterName)) {
            this.casters.replace(casterName, caster.toByteArray());
        } else {
            this.casters.put(casterName, caster.toByteArray());
        }
    }

    private final boolean isExistingCaster(final String casterName) {
        return this.casters.containsKey(casterName);
    }

    public final void sendCasterShoutOut(final MessageEvent messageEvent, final String casterName) {
        final StringBuilder builder = new StringBuilder();

        builder.append("Fellow caster in the stream! ");
        builder.append("Everyone, please give a warm welcome to @%s. ");
        builder.append("It would be great if you checked them out ");
        builder.append("and gave them a follow too. https://twitch.tv/%s");
        messageEvent.sendResponse(String.format(builder.toString(), casterName, casterName));
        setCaster(casterName, System.currentTimeMillis());
    }

}
