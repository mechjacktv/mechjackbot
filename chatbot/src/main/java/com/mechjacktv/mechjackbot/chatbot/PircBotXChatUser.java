package com.mechjacktv.mechjackbot.chatbot;

import org.pircbotx.User;

import com.mechjacktv.mechjackbot.ChatUser;
import com.mechjacktv.mechjackbot.ChatUsername;

final class PircBotXChatUser implements ChatUser {

    private final User user;

    PircBotXChatUser(final User user) {
        this.user = user;
    }

    @Override
    public ChatUsername getUsername() {
        return ChatUsername.of(this.user.getNick());
    }

}
