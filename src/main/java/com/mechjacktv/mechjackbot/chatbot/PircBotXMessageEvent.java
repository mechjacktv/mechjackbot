package com.mechjacktv.mechjackbot.chatbot;

import com.mechjacktv.mechjackbot.ChatBot;
import com.mechjacktv.mechjackbot.ChatUser;
import com.mechjacktv.mechjackbot.MessageEvent;
import org.pircbotx.hooks.types.GenericMessageEvent;

public class PircBotXMessageEvent implements MessageEvent {

    private final GenericMessageEvent genericMessageEvent;

    public PircBotXMessageEvent(final GenericMessageEvent genericMessageEvent) {
        this.genericMessageEvent = genericMessageEvent;
    }

    @Override
    public ChatBot getChatBot() {
        return new PircBotXChatBot(genericMessageEvent.getBot());
    }

    @Override
    public ChatUser getChatUser() {
        return new PircBotXChatUser(genericMessageEvent.getUser());
    }

    @Override
    public String getMessage() {
        return this.genericMessageEvent.getMessage();
    }

    @Override
    public void respond(String message) {
        this.genericMessageEvent.respondWith(String.format("/me MrDestructoid <( %s )", message));
    }
}
