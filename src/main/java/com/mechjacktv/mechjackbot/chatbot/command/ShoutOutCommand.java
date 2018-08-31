package com.mechjacktv.mechjackbot.chatbot.command;

import com.mechjacktv.mechjackbot.ChatUser;
import com.mechjacktv.mechjackbot.Command;
import com.mechjacktv.mechjackbot.MessageEvent;

import javax.inject.Inject;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class ShoutOutCommand implements Command {

    private static final String CASTERS_LOCATION = System.getProperty("user.home") + "/.mechjackbot_casters.config";
    private static final long TWENTY_FOUR_HOURS = 1000 * 60 * 60 * 24;

    private final CommandUtils commandUtils;
    private final Properties casters;

    @Inject
    public ShoutOutCommand(final CommandUtils commandUtils) throws IOException {
        this.commandUtils = commandUtils;
        this.casters = new Properties();
        if(!createCastersFile()) {
            try (final FileInputStream castersFile = new FileInputStream(CASTERS_LOCATION)) {
                this.casters.load(castersFile);
            }
        }
    }

    private boolean createCastersFile() throws IOException {
        return new File(CASTERS_LOCATION).createNewFile();
    }

    @Override
    public boolean handleMessage(final MessageEvent messageEvent) {
        final String message = messageEvent.getMessage();


        if (message.startsWith("!addcaster") && commandUtils.isCooleddown("!addcaster") && commandUtils.privilegedUser(messageEvent)) {
            final String[] messageParts = message.split(" ");

            if (messageParts.length > 1) {
                setCaster(commandUtils.sanitizeUsername(messageParts[1]), 0);
                messageEvent.respond(String.format("Added %s to casters list", messageParts[1]));
            }
            return true;
        } else if (message.startsWith("!delcaster") && commandUtils.isCooleddown("!delcaster") && commandUtils.privilegedUser(messageEvent)) {
            final String[] messageParts = message.split(" ");

            if (messageParts.length > 1) {
                casters.remove(commandUtils.sanitizeUsername(messageParts[1]));
                saveCasters();
                messageEvent.respond(String.format("Removed %s from casters list", messageParts[1]));
            }
            return true;
        } else if (message.startsWith("!caster") && commandUtils.isCooleddown("!caster") && commandUtils.privilegedUser(messageEvent)) {
            final String[] messageParts = message.split(" ");

            if (messageParts.length > 1) {
                final String sanitizedUsername = commandUtils.sanitizeUsername(messageParts[1]);

                setCaster(sanitizedUsername, System.currentTimeMillis());
                shoutOutCaster(messageEvent, sanitizedUsername);
            }
        } else {
            final ChatUser chatUser = messageEvent.getChatUser();
            final String chatUsername = commandUtils.sanitizeUsername(chatUser.getUsername());

            if (casters.containsKey(chatUsername)) {
                final long now = System.currentTimeMillis();
                final long lastShoutout = Long.parseLong(casters.getProperty(chatUsername));

                if (now - lastShoutout > TWENTY_FOUR_HOURS) {
                    setCaster(chatUsername, System.currentTimeMillis());
                    shoutOutCaster(messageEvent, chatUsername);
                }
            }
        }
        return false;
    }

    private void setCaster(final String username, final long lastShoutout) {
        casters.setProperty(username, Long.toString(lastShoutout));
        saveCasters();
    }

    private void saveCasters() {
        try (final FileOutputStream castersFile = new FileOutputStream(CASTERS_LOCATION)) {
            casters.store(castersFile, "");
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    private void shoutOutCaster(final MessageEvent messageEvent, final String casterName) {
        messageEvent.respond(String.format(
                "<3 <3 Caster in the house! Everyone please give %s a follow and check them out. https://twitch.tv/%s <3 <3",
                casterName, casterName));
    }

}
