package com.mechjacktv.mechjackbot.chatbot.command.caster;

import com.mechjacktv.mechjackbot.Command;
import com.mechjacktv.mechjackbot.MessageEvent;
import com.mechjacktv.mechjackbot.chatbot.command.AbstractCommand;
import com.mechjacktv.mechjackbot.chatbot.command.CommandUtils;
import com.mechjacktv.mechjackbot.chatbot.command.GlobalCoolDown;
import com.mechjacktv.mechjackbot.chatbot.command.RestrictToPrivileged;

import javax.inject.Inject;

public class DelCasterCommand extends AbstractCommand {

    private final CommandUtils commandUtils;
    private final CasterService casterService;

    @Inject
    public DelCasterCommand(final CasterService casterService, final CommandUtils commandUtils) {
        super("!delcaster", commandUtils);
        this.casterService = casterService;
        this.commandUtils = commandUtils;
    }

    @Override
    public String getDecription() {
        return "Removes a caster from the caster list.";
    }

    @Override
    @RestrictToPrivileged
    @GlobalCoolDown
    public void handleMessage(MessageEvent messageEvent) {
        final String message = messageEvent.getMessage();
        final String[] messageParts = message.split("\\s+");

        if (messageParts.length == 2) {
            final String casterName = this.commandUtils.sanitizeViewerName(messageParts[1]);

            this.casterService.removeCaster(casterName);
            messageEvent.sendResponse(String.format("Removed @%s from casters list", casterName));
        } else {
            this.commandUtils.sendUsage(messageEvent, String.format("%s <casterName>", getCommandTrigger()));
        }
    }
}
