package com.mechjacktv.mechjackbot.command;

import com.mechjacktv.mechjackbot.*;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class CommandUtils {

    private static final String COMMAND_COOL_DOWN_PERIOD_MS = "command.coolDownPeriod.ms";
    private static final String COMMAND_COOL_DOWN_PERIOD_MS_DEFAULT = "5000";

    private final String botOwner;
    private final long commandCoolDownPeriodMs;
    private final Map<String, Long> commandLastCalled;
    private final Map<String, Pattern> commandTriggerPatterns;

    @Inject
    public CommandUtils(final AppConfiguration appConfiguration, final BotConfiguration botConfiguration) {
        this.botOwner = sanitizeViewerName(botConfiguration.getChannel());
        this.commandCoolDownPeriodMs = Long.parseLong(appConfiguration.getProperty(COMMAND_COOL_DOWN_PERIOD_MS,
                COMMAND_COOL_DOWN_PERIOD_MS_DEFAULT));
        this.commandLastCalled = new HashMap<>();
        this.commandTriggerPatterns = new HashMap<>();
    }

    public final String getSanitizedViewerName(final MessageEvent messageEvent) {
        return sanitizeViewerName(messageEvent.getChatUser().getUsername());
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
        if (this.commandTriggerPatterns.containsKey(commandTrigger)) {
            return this.commandTriggerPatterns.get(commandTrigger);
        }

        final String commandTriggerRegex = commandTrigger + "(\\s+.+)?";
        final Pattern commandTriggerPattern = Pattern.compile(commandTriggerRegex);

        this.commandTriggerPatterns.put(commandTrigger, commandTriggerPattern);
        return commandTriggerPattern;
    }

    public final boolean isGloballyCooledDown(final Command command) {
        return isGloballyCooledDown(command.getCommandTrigger());
    }

    public final boolean isGloballyCooledDown(final String commandTrigger) {
        final Long now = System.currentTimeMillis();
        final Long lastCalled = commandLastCalled.get(commandTrigger);

        if (lastCalled == null || now - lastCalled > this.commandCoolDownPeriodMs) {
            commandLastCalled.put(commandTrigger, now);
            return true;
        }
        return false;
    }

    public final boolean isPrivilegedViewer(final MessageEvent messageEvent) {
        // TODO implement mod check
        return isChannelOwner(messageEvent);
    }

    public final boolean isRegularUserViewer(final MessageEvent messageEvent) {
        // TODO implement regular check
        return isPrivilegedViewer(messageEvent);
    }


    public final String sanitizeViewerName(final String username) {
        String sanitizedUsername = username.trim().toLowerCase();

        if (sanitizedUsername.startsWith("@")) {
            sanitizedUsername = sanitizedUsername.substring(1);
        }
        return sanitizedUsername;
    }

    public final void sendUsage(final MessageEvent messageEvent, final String usage) {
        messageEvent.sendResponse(String.format("%s, usage: %s", getSanitizedViewerName(messageEvent), usage));
    }

}
