package com.mechjacktv.mechjackbot.chatbot.command;

import com.mechjacktv.mechjackbot.BotConfiguration;
import com.mechjacktv.mechjackbot.ChatUser;
import com.mechjacktv.mechjackbot.MessageEvent;

import javax.inject.Inject;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class CommandUtils {

    private static final long COOLDOWN_PERIOD = 5000;

    private final String botOwner;
    private final Map<String, Long> commandLastCalled;

    @Inject
    public CommandUtils(final BotConfiguration botConfiguration) {
        this.botOwner = botConfiguration.getChannel().toLowerCase();
        this.commandLastCalled = Collections.synchronizedMap(new HashMap<>());
    }

    public final boolean channelOwner(final MessageEvent messageEvent) {
        final ChatUser chatUser = messageEvent.getChatUser();
        final String chatUsername = chatUser.getUsername().toLowerCase();

        return botOwner.equals(chatUsername);
    }

    public final boolean isCooleddown(final String commandTrigger) {
        final Long now = System.currentTimeMillis();
        final Long lastCalled = commandLastCalled.get(commandTrigger);

        if(lastCalled == null || now - lastCalled > COOLDOWN_PERIOD) {
            commandLastCalled.put(commandTrigger, now);
            return true;
        }
        return false;
    }

    public final boolean privilegedUser(final MessageEvent messageEvent) {
        return channelOwner(messageEvent);
    }


    public final String sanitizeUsername(final String username) {
        String sanitizedUsername = username.trim().toLowerCase();

        if(sanitizedUsername.startsWith("@")) {
            sanitizedUsername = sanitizedUsername.substring(1);
        }
        return sanitizedUsername;
    }

}
