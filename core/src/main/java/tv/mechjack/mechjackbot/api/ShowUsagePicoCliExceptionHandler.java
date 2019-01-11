package tv.mechjack.mechjackbot.api;

import picocli.CommandLine.ExecutionException;
import picocli.CommandLine.IExceptionHandler2;
import picocli.CommandLine.ParameterException;
import picocli.CommandLine.ParseResult;

public class ShowUsagePicoCliExceptionHandler implements IExceptionHandler2<Boolean> {

  private final RespondingChatCommand chatCommand;
  private final ChatMessageEvent chatMessageEvent;

  public ShowUsagePicoCliExceptionHandler(final RespondingChatCommand chatCommand,
      final ChatMessageEvent chatMessageEvent) {
    this.chatCommand = chatCommand;
    this.chatMessageEvent = chatMessageEvent;
  }

  @Override
  public Boolean handleParseException(final ParameterException ex, final String[] args) {
    this.chatCommand.sendUsage(this.chatMessageEvent);
    return false;
  }

  @Override
  public Boolean handleExecutionException(final ExecutionException ex, final ParseResult parseResult) {
    this.chatCommand.sendUsage(this.chatMessageEvent);
    return false;
  }

}
