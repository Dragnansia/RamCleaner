package net.dragnansia.ramcleaner.keyboard;

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

public class KeyboardBind {

    private KeyBinding keyBinding;

    public KeyboardBind() {
        String bindingName = I18n.format("ramcleaner.settings.clear");

        keyBinding = new KeyBinding(
            bindingName,
            KeyConflictContext.UNIVERSAL,
            KeyModifier.ALT,
            InputMappings.Type.KEYSYM,
            GLFW.GLFW_KEY_F4,
            "RamCleaner"
        );
        ClientRegistry.registerKeyBinding(keyBinding);
    }

    @SubscribeEvent
    public void onEvent(InputEvent.KeyInputEvent event) {
        if (keyBinding.isPressed()) {
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
    }
}
