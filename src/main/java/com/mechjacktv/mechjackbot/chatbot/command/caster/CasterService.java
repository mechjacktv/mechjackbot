package com.mechjacktv.mechjackbot.chatbot.command.caster;

import com.mechjacktv.mechjackbot.MessageEvent;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class CasterService {

    private static final String CASTERS_LOCATION = System.getProperty("user.home") + "/.mechjackbot_casters.config";
    private static final long TWENTY_FOUR_HOURS = 1000 * 60 * 60 * 24;

    private final Properties casters;

    public CasterService() throws IOException {
        this.casters = new Properties();
        if (!createCastersFile()) {
            try (final FileInputStream castersFile = new FileInputStream(CASTERS_LOCATION)) {
                this.casters.load(castersFile);
            }
        }
    }

    private final boolean createCastersFile() throws IOException {
        return new File(CASTERS_LOCATION).createNewFile();
    }

    public boolean isCasterDue(final String casterName) {
        if (casters.containsKey(casterName)) {
            final long now = System.currentTimeMillis();
            final long lastShoutOut = Long.parseLong(casters.getProperty(casterName));

            return now - lastShoutOut > TWENTY_FOUR_HOURS;
        }
        return false;
    }

    public final void removeCaster(final String casterName) {
        casters.remove(casterName);
        saveCasters();
    }

    public final void setCaster(final String casterName, final long lastShoutOut) {
        casters.setProperty(casterName, Long.toString(lastShoutOut));
        saveCasters();
    }

    private final void saveCasters() {
        try (final FileOutputStream castersFile = new FileOutputStream(CASTERS_LOCATION)) {
            casters.store(castersFile, "");
        } catch (final IOException e) {
            e.printStackTrace();
        }
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
