package com.mechjacktv.mechjackbot.command.caster;

import com.mechjacktv.mechjackbot.MessageEvent;
import com.mechjacktv.mechjackbot.command.AbstractCommand;
import com.mechjacktv.mechjackbot.command.CommandUtils;
import com.mechjacktv.mechjackbot.GlobalCoolDown;
import com.mechjacktv.mechjackbot.RestrictToPrivileged;

import javax.inject.Inject;

public class CasterCommand extends AbstractCommand {

    private final CasterService casterService;
    private final CommandUtils commandUtils;

    @Inject
    public CasterCommand(final CasterService casterService, final CommandUtils commandUtils) {
        super("!caster", commandUtils);
        this.casterService = casterService;
        this.commandUtils = commandUtils;
    }

    @Override
    public final String getDescription() {
        return "Shout out a caster! Add the caster to the caster list.";
    }

    @Override
    @RestrictToPrivileged
    @GlobalCoolDown
    public void handleMessage(MessageEvent messageEvent) {
        final String message = messageEvent.getMessage();
        final String[] messageParts = message.split("\\s+");

        if (messageParts.length == 2) {
            final String casterName = this.commandUtils.sanitizeViewerName(messageParts[1]);

            this.casterService.sendCasterShoutOut(messageEvent, casterName);
        } else {
            this.commandUtils.sendUsage(messageEvent, String.format("%s <casterName>", getCommandTrigger()));
        }

    }
}
