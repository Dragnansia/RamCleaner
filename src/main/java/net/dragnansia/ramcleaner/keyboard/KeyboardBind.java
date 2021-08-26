package net.dragnansia.ramcleaner.keyboard;

import com.sun.java.accessibility.util.java.awt.TextComponentTranslator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraft.client.gui.chat.NarratorChatListener;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public class KeyboardBind {

    private KeyBinding keyBinding;

    public KeyboardBind() {
        keyBinding = new KeyBinding("Clear Game Ram", -1, "RamCleaner");
        ClientRegistry.registerKeyBinding(keyBinding);
    }

    @SubscribeEvent
    public void onEvent(InputEvent.KeyInputEvent event) {
        if (keyBinding.isPressed()) {
            System.gc();
            String text = "Ram is normally clean.\n" +
                "No used this command to many time";

            assert Minecraft.getInstance().player != null;
            Minecraft.getInstance().ingameGUI.getChatGUI().printChatMessage(
                ITextComponent.getTextComponentOrEmpty(text)
            );
        }
    }
}
