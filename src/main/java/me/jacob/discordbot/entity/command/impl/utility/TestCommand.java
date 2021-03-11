package me.jacob.discordbot.entity.command.impl.utility;

import me.jacob.discordbot.entity.command.AbstractCommand;
import me.jacob.discordbot.entity.command.CommandExecutor;
import me.jacob.discordbot.entity.command.type.Category;

import java.util.List;

public class TestCommand extends AbstractCommand {

    public TestCommand() {
        super("test", "Tests to see if I am working.", Category.UTILITY);
    }

    @Override
    public void runCommand(CommandExecutor executor, List<String> args) {
        executor.replyDefault(true, "I am working... I think? \uD83E\uDD14");
    }
}
