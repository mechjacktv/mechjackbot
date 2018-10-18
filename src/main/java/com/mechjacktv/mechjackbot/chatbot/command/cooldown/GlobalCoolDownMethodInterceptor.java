package com.mechjacktv.mechjackbot.chatbot.command.cooldown;

import com.mechjacktv.mechjackbot.Command;
import com.mechjacktv.mechjackbot.MessageEvent;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class GlobalCoolDownMethodInterceptor implements MethodInterceptor {

    private static final long COOLDOWN_PERIOD = 5000;

    private final Map<String, Long> commandLastCalled;

    public GlobalCoolDownMethodInterceptor() {
        this.commandLastCalled = Collections.synchronizedMap(new HashMap<>());
    }

    @Override
    public Object invoke(final MethodInvocation invocation) throws Throwable {
        final Object thisInstance = invocation.getThis();

        if(Command.class.isAssignableFrom(thisInstance.getClass())) {
            final Command thisCommand = (Command) thisInstance;

            if(thisCommand.isHandledMessage((MessageEvent) invocation.getArguments()[0])) {
                if(this.isCooledDown(invocation.getThis().getClass().getCanonicalName())) {
                    return invocation.proceed();
                }
            }
        }
        throw new IllegalStateException("`@GlobalCoolDown` MUST only be placed on implementors of `Command`");
    }

    private final boolean isCooledDown(final String commandTrigger) {
        final Long now = System.currentTimeMillis();
        final Long lastCalled = commandLastCalled.get(commandTrigger);

        if(lastCalled == null || now - lastCalled > COOLDOWN_PERIOD) {
            commandLastCalled.put(commandTrigger, now);
            return true;
        }
        return false;
    }

}
