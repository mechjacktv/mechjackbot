package com.mechjacktv.mechjackbot.chatbot;

public class ChatBotStartupException extends ChatBotException {

	private static final long serialVersionUID = -5027414115096871357L;

	public ChatBotStartupException(final Throwable cause) {
        super("Failed to connect to chat", cause);
    }

}
