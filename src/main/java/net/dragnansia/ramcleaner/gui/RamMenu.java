package net.dragnansia.ramcleaner.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import org.lwjgl.glfw.GLFW;

public class RamMenu extends Screen {
    private final int Width = 176;
    private final int Height = 79;

    private KeyBinding keyToClose;

    public RamMenu(KeyBinding keyBinding) {
        super(new TranslationTextComponent("ramcleaner.settings.menu"));
        keyToClose = keyBinding;
    }

    @Override
    protected void init() {
        int x = (width - Width) / 2;
        int y = (height - Height) / 2;

        int bsw = 80, bsh = 20;

        addButton(new Button(x + (Width / 2 - (bsw / 2)), y + (Height / 2) + (bsh - 4), bsw, bsh,
            new StringTextComponent(I18n.format("ramcleaner.menu.clear")), button -> System.gc()));
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        RenderSystem.blendColor(1, 1, 1, 1);
        minecraft.getTextureManager().bindTexture(new ResourceLocation("ramcleaner:textures/gui/menu.png"));
        int x = (width - Width) / 2;
        int y = (height - Height) / 2;
        blit(matrixStack, x, y, 0, 0, Width, Height);

        Runtime runtime = Runtime.getRuntime();
        FontRenderer fontRenderer = minecraft.fontRenderer;

        long max = runtime.maxMemory() / 1048576;
        long total = runtime.totalMemory() / 1048576;
        long free = runtime.freeMemory() / 1048576;
        long totalFree = (free + (max - total));

        String f = I18n.format("ramcleaner.command.game.free");
        String u = I18n.format("ramcleaner.command.game.used");
        String t = I18n.format("ramcleaner.command.game.total");
        String pu = I18n.format("ramcleaner.command.game.pct_used");

        fontRenderer.drawString(matrixStack, t + ": " + total + "Mib", x + 10, y + 10, 10);
        fontRenderer.drawString(matrixStack, u + ": " + (max - totalFree) + "Mib", x + 10, y + 20, 10);
        fontRenderer.drawString(matrixStack, f + ": " + totalFree + "Mib", x + 10, y + 30, 10);
        fontRenderer.drawString(matrixStack, pu + ": " + (totalFree * 100 / max) + "%", x + 10, y + 40, 10);

        super.render(matrixStack, mouseX, mouseY, partialTicks);
    }

    @Override
    public boolean keyPressed(int p_231046_1_, int p_231046_2_, int p_231046_3_) {
        if (p_231046_1_ == GLFW.GLFW_KEY_ESCAPE || keyToClose.getKey().getKeyCode() == p_231046_1_) {
            closeScreen();
            return true;
        }

        return super.keyPressed(p_231046_1_, p_231046_2_, p_231046_3_);
    }
}
