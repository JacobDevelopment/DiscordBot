package me.jacob.discordbot.entity.command.impl.utility;

import me.jacob.discordbot.entity.command.AbstractCommand;
import me.jacob.discordbot.entity.command.CommandExecutor;
import me.jacob.discordbot.entity.command.type.Category;

import java.util.List;

public class PingCommand extends AbstractCommand {

    public PingCommand() {
        super("ping", "Shows reachability.", Category.UTILITY);
    }

    @Override
    public void runCommand(CommandExecutor executor, List<String> args) {
        final long start = System.currentTimeMillis();
        executor.getJDA().getRestPing()
                .map(String::valueOf)
                .queue(
                        (success) -> executor.replyDefault(false, "\n**Rest Ping:** %sms\n**Gateway Ping:** %sms\n**Execution Time:** %sms",
                                success, executor.getJDA().getGatewayPing(), System.currentTimeMillis() - start),
                        (error) -> executor.replyDefault(false, "\n**Gateway Ping:** %sms\n**Execution Time:** %sms",
                                executor.getJDA().getGatewayPing(), System.currentTimeMillis() - start)
                );
    }
}
