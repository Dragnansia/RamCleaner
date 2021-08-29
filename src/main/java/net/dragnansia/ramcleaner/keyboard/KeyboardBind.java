package net.dragnansia.ramcleaner.keyboard;

import net.dragnansia.ramcleaner.gui.RamMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.client.settings.KeyModifier;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import org.lwjgl.glfw.GLFW;

import static net.dragnansia.ramcleaner.command.RamCommand.RamMessage;

public class KeyboardBind {

    private final KeyBinding cleanRam;
    private final KeyBinding seeInformation;
    private final KeyBinding openMenu;

    public KeyboardBind() {
        String cleanRamName = I18n.format("ramcleaner.settings.clear");
        String seeInformationName = I18n.format("ramcleaner.settings.info");
        String openMenuName = I18n.format("ramcleaner.settings.menu");

        cleanRam = new KeyBinding(
            cleanRamName,
            KeyConflictContext.UNIVERSAL,
            KeyModifier.ALT,
            InputMappings.Type.KEYSYM,
            GLFW.GLFW_KEY_F4,
            "RamCleaner"
        );

        seeInformation = new KeyBinding(
            seeInformationName,
            KeyConflictContext.UNIVERSAL,
            KeyModifier.NONE,
            InputMappings.Type.KEYSYM,
            -1,
            "RamCleaner"
        );

        openMenu = new KeyBinding(
            openMenuName,
            KeyConflictContext.UNIVERSAL,
            KeyModifier.NONE,
            InputMappings.Type.KEYSYM,
            -1,
            "RamCleaner"
        );

        ClientRegistry.registerKeyBinding(openMenu);
        ClientRegistry.registerKeyBinding(cleanRam);
        ClientRegistry.registerKeyBinding(seeInformation);
    }

    @SubscribeEvent
    public void onEvent(InputEvent.KeyInputEvent event) {
        if (cleanRam.isPressed()) {
            Runtime runtime = Runtime.getRuntime();

            long ramBefore = runtime.totalMemory() / 1048576;
            System.gc();
            long ramAfter = runtime.totalMemory() / 1048576;

            String text = I18n.format("ramcleaner.command.game.clear");
            text += (ramBefore - ramAfter) + "Mib." + I18n.format("ramcleaner.command.game.clear_2");

            assert Minecraft.getInstance().player != null;
            Minecraft.getInstance().ingameGUI.getChatGUI().printChatMessage(
                ITextComponent.getTextComponentOrEmpty(text)
            );
        }

        if (seeInformation.isPressed()) {
            Runtime runtime = Runtime.getRuntime();

            long max = runtime.maxMemory() / 1048576;
            long total = runtime.totalMemory() / 1048576;
            long free = runtime.freeMemory() / 1048576;
            long totalFree = (free + (max - total));

            String text = RamMessage(total, max, totalFree).getText();

            assert Minecraft.getInstance().player != null;
            Minecraft.getInstance().ingameGUI.getChatGUI().printChatMessage(
                ITextComponent.getTextComponentOrEmpty(text)
            );
        }

        if (openMenu.isPressed() && Minecraft.getInstance().world != null) {
        }
    }
}
