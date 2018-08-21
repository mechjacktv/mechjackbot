package com.mechjacktv.mechjackbot.chatbot.command;

import com.mechjacktv.mechjackbot.BotConfiguration;
import com.mechjacktv.mechjackbot.ChatUser;
import com.mechjacktv.mechjackbot.MessageEvent;

import javax.inject.Inject;

public final class CommandUtils {

    private final String botOwner;

    @Inject
    public CommandUtils(final BotConfiguration botConfiguration) {
        this.botOwner = botConfiguration.getChannel().toLowerCase();
    }

    public final boolean channelOwner(final MessageEvent messageEvent) {
        final ChatUser chatUser = messageEvent.getChatUser();
        final String chatUsername = chatUser.getUsername().toLowerCase();

        return botOwner.equals(chatUsername);
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
