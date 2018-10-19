package com.mechjacktv.mechjackbot.chatbot.command.caster;

import com.mechjacktv.mechjackbot.Command;
import com.mechjacktv.mechjackbot.MessageEvent;
import com.mechjacktv.mechjackbot.chatbot.command.AbstractCommand;
import com.mechjacktv.mechjackbot.chatbot.command.CommandUtils;
import com.mechjacktv.mechjackbot.chatbot.command.GlobalCoolDown;
import com.mechjacktv.mechjackbot.chatbot.command.RestrictToPrivileged;

import javax.inject.Inject;

public class AddCasterCommand extends AbstractCommand {

    private final CommandUtils commandUtils;
    private final CasterService casterService;

    @Inject
    public AddCasterCommand(final CasterService casterService, final CommandUtils commandUtils) {
        super("!addcaster", commandUtils);
        this.casterService = casterService;
        this.commandUtils = commandUtils;
    }

    @Override
    public String getDescription() {
        return "Adds a caster to the caster list.";
    }

    @Override
    @RestrictToPrivileged
    @GlobalCoolDown
    public void handleMessage(MessageEvent messageEvent) {
        final String message = messageEvent.getMessage();
        final String[] messageParts = message.split("\\s+");

        if (messageParts.length == 2) {
            final String casterName = this.commandUtils.sanitizeViewerName(messageParts[1]);

            this.casterService.setCaster(casterName, 0);
            messageEvent.sendResponse(String.format("Added @%s to casters list", casterName));
        } else {
            this.commandUtils.sendUsage(messageEvent, String.format("%s <casterName>", getCommandTrigger()));
        }
    }
}
