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
                .executes(context -> ClearRam(context.getSource()))
            )
            .then(Commands.literal("cleanos")
                .executes(context -> ClearOSRam(context.getSource()))
            )
            .then(Commands.literal("info")
                .executes(context -> RamUsage(context.getSource()))
            )
        );
    }

    private static int ClearRam(CommandSource source) {
        Runtime runtime = Runtime.getRuntime();

        long notAllocated = runtime.maxMemory() - runtime.totalMemory();
        long freePct = (runtime.freeMemory() + notAllocated) * 100 / runtime.maxMemory();

        if (freePct >= 10) {
            source.sendErrorMessage(
                new StringTextComponent("This server have more than " + freePct + "% of ram not used")
            );
            return 0;
        }

        System.gc();
        return 0;
    }

    private static int ClearOSRam(CommandSource source) {
        if (currentOS == CurrentOS.Unknown) {
            source.sendErrorMessage(
                new StringTextComponent("The current os used is not determined")
            );
            return 0;
        }

        // create function for clear ram

        return 0;
    }

    private static int RamUsage(CommandSource source) {
        OperatingSystemMXBean mxBean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();

        long total = mxBean.getTotalPhysicalMemorySize() / 1048576;
        long free = mxBean.getFreePhysicalMemorySize() / 1048576;
        long used = total - free;
        long pct = used * 100 / total;

        StringTextComponent text =
            new StringTextComponent(
                "| Free:     " + free + " Mio" +
                    "\n| Used:     " + used + " Mio" +
                    "\n| Total:    " + total + " Mio" +
                    "\n| Pct Used: " + pct + "%"
            );

        source.sendFeedback(text, true);
        return 0;
    }
}
