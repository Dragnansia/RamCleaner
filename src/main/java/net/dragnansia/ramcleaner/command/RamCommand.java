package net.dragnansia.ramcleaner.command;

import com.mojang.brigadier.CommandDispatcher;

import net.minecraft.client.resources.I18n;
import net.minecraft.command.Commands;
import net.minecraft.command.CommandSource;
import net.minecraft.util.text.StringTextComponent;


public class RamCommand {

    private RamCommand() {}

    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        dispatcher.register(Commands.literal("ram")
            .requires(commandSource -> commandSource.hasPermissionLevel(2))
            .then(Commands.literal("clean")
                .executes(context -> ClearRam(context.getSource()))
            )
            .then(Commands.literal("info")
                .executes(context -> RamUsage(context.getSource()))
            )
            .executes(context -> RamUsage(context.getSource()))
        );
    }

    private static int ClearRam(CommandSource source) {
        Runtime runtime = Runtime.getRuntime();

        long ramBefore = runtime.totalMemory() / 1048576;
        System.gc();
        long ramAfter = runtime.totalMemory() / 1048576;

        StringTextComponent text =
            new StringTextComponent(
                I18n.format("ramcleaner.command.game.clear") +
                (ramBefore - ramAfter) + "Mib." +
                I18n.format("ramcleaner.command.game.clear_2")
            );

        source.sendFeedback(text, true);
        return 0;
    }

    private static int RamUsage(CommandSource source) {
        Runtime runtime = Runtime.getRuntime();

        long max = runtime.maxMemory() / 1048576;
        long total = runtime.totalMemory() / 1048576;
        long free = runtime.freeMemory() / 1048576;
        long totalFree = (free + (max - total));

        StringTextComponent text = RamMessage(total, max, totalFree);

        source.sendFeedback(text, true);
        return 0;
    }

    public static StringTextComponent RamMessage(long total, long max, long totalFree) {
        String f = I18n.format("ramcleaner.command.game.free");
        String u = I18n.format("ramcleaner.command.game.used");
        String t = I18n.format("ramcleaner.command.game.total");
        String pu = I18n.format("ramcleaner.command.game.pct_used");

        return new StringTextComponent(
            "\n" +
            "| ------- Minecraft ------- |\n" +
            "| " + f + ": " + totalFree + " Mio\n" +
            "| " + u + ": " + (max - totalFree) + " Mio\n" +
            "| " + t + ": " + total + " Mio\n" +
            "| " + pu + ": " + ((max - totalFree) * 100 / max) + "%"
        );
    }
}
