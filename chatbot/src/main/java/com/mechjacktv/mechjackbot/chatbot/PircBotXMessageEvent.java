package com.mechjacktv.mechjackbot.chatbot;

import com.mechjacktv.mechjackbot.ChatBot;
import com.mechjacktv.mechjackbot.ChatUser;
import com.mechjacktv.mechjackbot.MessageEvent;
import com.mechjacktv.util.ExecutionUtils;
import org.pircbotx.hooks.types.GenericMessageEvent;

public final class PircBotXMessageEvent implements MessageEvent {

    private final ExecutionUtils executionUtils;
    private final GenericMessageEvent genericMessageEvent;

    PircBotXMessageEvent(final ExecutionUtils executionUtils, final GenericMessageEvent genericMessageEvent) {
        this.executionUtils = executionUtils;
        this.genericMessageEvent = genericMessageEvent;
    }

    @Override
    public ChatBot getChatBot() {
        return new PircBotXChatBot(executionUtils, genericMessageEvent.getBot());
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
    public void sendResponse(String message) {
        this.genericMessageEvent.respondWith(String.format("/me MrDestructoid <( %s )", message));
    }

}
