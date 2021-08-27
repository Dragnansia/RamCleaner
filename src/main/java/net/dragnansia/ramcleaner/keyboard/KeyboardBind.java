package net.dragnansia.ramcleaner.keyboard;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public class KeyboardBind {

    private KeyBinding keyBinding;

    public KeyboardBind() {
        String bindingName = I18n.format("ramcleaner.settings.clear");

        keyBinding = new KeyBinding(bindingName, -1, "RamCleaner");
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
