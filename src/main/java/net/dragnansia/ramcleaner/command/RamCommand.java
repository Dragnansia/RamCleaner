package net.dragnansia.ramcleaner.command;

import com.mojang.brigadier.CommandDispatcher;

import com.sun.management.OperatingSystemMXBean;
import net.minecraft.command.Commands;
import net.minecraft.command.CommandSource;
import net.minecraft.util.text.StringTextComponent;

import java.lang.management.ManagementFactory;

enum CurrentOS {
    Linux, Windows, MacOs, Unknown
};

public class RamCommand {

    private static CurrentOS currentOS;

    private RamCommand() {}

    private static void FindCurrentOs() {
        String os = System.getProperty("os.name").toLowerCase();
        currentOS = IsWindow(os) ? CurrentOS.Windows
            : IsUnix(os) ? CurrentOS.Linux
            : IsMac(os) ? CurrentOS.MacOs
            : CurrentOS.Unknown;
    }

    private static boolean IsUnix(String str) {
        return str.contains("nix") || str.contains("nux") || str.contains("aix");
    }

    private static boolean IsMac(String str) {
        return str.contains("mac");
    }

    private static boolean IsWindow(String str) {
        return str.contains("window");
    }

    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        FindCurrentOs();

        dispatcher.register(Commands.literal("ram")
            .requires(commandSource -> commandSource.hasPermissionLevel(2))
            .then(Commands.literal("clean")
                .then(Commands.literal("os")
                    .executes(context -> ClearOSRam(context.getSource()))
                )
                .then(Commands.literal("game")
                    .executes(context -> ClearRam(context.getSource()))
                )
            )
            .then(Commands.literal("info")
                .then(Commands.literal("os")
                    .executes(context -> RamOSUsage(context.getSource()))
                )
                .then(Commands.literal("game")
                    .executes(context -> RamGameUsage(context.getSource()))
                )
            )
            .executes(context -> RamGameUsage(context.getSource()))
        );
    }

    private static int ClearRam(CommandSource source) {
        System.gc();
        StringTextComponent text =
            new StringTextComponent(
                "Ram is normally clean.\n" +
                "No used this command to many time"
            );

        source.sendFeedback(text, true);
        return 0;
    }

    private static int ClearOSRam(CommandSource source) {
        // create function for clear ram
        switch (currentOS) {
            case Linux:
                break;
            case Windows:
                break;
            case MacOs:
                break;
            case Unknown:
                source.sendErrorMessage(
                    new StringTextComponent("The current os used is not determined")
                );
                break;
        }

        return 0;
    }

    private static int RamGameUsage(CommandSource source) {
        Runtime runtime = Runtime.getRuntime();

        long max = runtime.maxMemory() / 1048576;
        long total = runtime.totalMemory() / 1048576;
        long free = runtime.freeMemory() / 1048576;
        long totalFree = (free + (max - total));

        StringTextComponent text =
            new StringTextComponent(
                "\n" +
                "| ------- Game ------- |\n" +
                "| Free: " + totalFree + " Mio\n" +
                "| Used: " + (max - totalFree) + " Mio\n" +
                "| Total: " + total + " Mio\n" +
                "| Pct Used: " + ((max - totalFree) * 100 / max) + "%"
            );

        source.sendFeedback(text, true);
        return 0;
    }

    private static int RamOSUsage(CommandSource source) {
        OperatingSystemMXBean mxBean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();

        long total = mxBean.getTotalPhysicalMemorySize() / 1048576;
        long free = mxBean.getFreePhysicalMemorySize() / 1048576;
        long used = total - free;
        long pct = used * 100 / total;

        StringTextComponent text =
            new StringTextComponent(
                "\n" +
                "| ------- Os ------- |\n" +
                "| Free: " + free + " Mio\n" +
                "| Used: " + used + " Mio\n" +
                "| Total: " + total + " Mio\n" +
                "| Pct Used: " + pct + "%"
            );

        source.sendFeedback(text, true);
        return 0;
    }
}
