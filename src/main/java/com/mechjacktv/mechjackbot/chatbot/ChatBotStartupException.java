package com.mechjacktv.mechjackbot.chatbot;

public class ChatBotStartupException extends ChatBotException {

    public ChatBotStartupException(final Throwable cause) {
        super("Failed to connect to chat", cause);
    }

}
