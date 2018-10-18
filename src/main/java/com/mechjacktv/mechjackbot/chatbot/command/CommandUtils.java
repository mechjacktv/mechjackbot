package com.mechjacktv.mechjackbot.chatbot.command;

import com.mechjacktv.mechjackbot.BotConfiguration;
import com.mechjacktv.mechjackbot.ChatUser;
import com.mechjacktv.mechjackbot.MessageEvent;

import javax.inject.Inject;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class CommandUtils {

    private static final long COOLDOWN_PERIOD = 5000;

    private final String botOwner;
    private final Map<String, Long> commandLastCalled;
    private final Map<String, Pattern> commandTriggerPatterns;

    @Inject
    public CommandUtils(final BotConfiguration botConfiguration) {
        this.botOwner = botConfiguration.getChannel().toLowerCase();
        this.commandLastCalled = Collections.synchronizedMap(new HashMap<>());
        this.commandTriggerPatterns = Collections.synchronizedMap(new HashMap<>());
    }

    public final boolean isChannelOwner(final MessageEvent messageEvent) {
        final ChatUser chatUser = messageEvent.getChatUser();
        final String chatUsername = chatUser.getUsername().toLowerCase();

        return botOwner.equals(chatUsername);
    }

    public final boolean isCommandTrigger(final String commandTrigger, final MessageEvent messageEvent) {
        final String message = messageEvent.getMessage();
        final Pattern commandTriggerPattern = getCommandTriggerPattern(commandTrigger);
        final Matcher commandTriggerMatcher = commandTriggerPattern.matcher(message);

        return commandTriggerMatcher.matches();
    }

    private final Pattern getCommandTriggerPattern(final String commandTrigger) {
        if(this.commandTriggerPatterns.containsKey(commandTrigger)) {
            return this.commandTriggerPatterns.get(commandTrigger);
        }

        final String commandTriggerRegex = commandTrigger + "(\\s+.+)?";
        final Pattern commandTriggerPattern = Pattern.compile(commandTriggerRegex);

        this.commandTriggerPatterns.put(commandTrigger, commandTriggerPattern);
        return commandTriggerPattern;
    }

    public final boolean isCooledDownGlobally(final Class<?> commandClass) {
        return isCooledDownGlobally(commandClass.getCanonicalName());
    }

    public final boolean isCooledDownGlobally(final String commandTrigger) {
        final Long now = System.currentTimeMillis();
        final Long lastCalled = commandLastCalled.get(commandTrigger);

        if(lastCalled == null || now - lastCalled > COOLDOWN_PERIOD) {
            commandLastCalled.put(commandTrigger, now);
            return true;
        }
        return false;
    }

    public final boolean isPrivilegedUser(final MessageEvent messageEvent) {
        // TODO implement mod check
        return isChannelOwner(messageEvent);
    }

    public final boolean isRegularUser(final MessageEvent messageEvent) {
        // TODO implement regular check
        return isPrivilegedUser(messageEvent);
    }


    public final String sanitizeUsername(final String username) {
        String sanitizedUsername = username.trim().toLowerCase();

        if(sanitizedUsername.startsWith("@")) {
            sanitizedUsername = sanitizedUsername.substring(1);
        }
        return sanitizedUsername;
    }

}
