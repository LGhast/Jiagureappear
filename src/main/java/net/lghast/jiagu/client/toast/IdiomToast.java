package net.lghast.jiagu.client.toast;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.toasts.Toast;
import net.minecraft.client.gui.components.toasts.ToastComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class IdiomToast implements Toast {
    private static final ResourceLocation TEXTURE = ResourceLocation.withDefaultNamespace("toast/recipe");

    private final Component title;
    private final Component description;
    private final ItemStack icon;

    public IdiomToast(Component title, Component description, ItemStack icon) {
        this.title = title;
        this.description = description;
        this.icon = icon;
    }

    @Override
    public Toast.@NotNull Visibility render(GuiGraphics graphics, @NotNull ToastComponent toastComponent, long timeSinceLastVisible) {
        graphics.blitSprite(TEXTURE, 0, 0, this.width(), this.height());

        if (!icon.isEmpty()) {
            graphics.renderFakeItem(icon, 8, 8);
        }

        Font font = toastComponent.getMinecraft().font;
        graphics.drawString(font, this.title, 30, 7, -11534256, false);
        graphics.drawString(font, this.description, 30, 18, -16777216, false);

        return timeSinceLastVisible >= 5000L ? Toast.Visibility.HIDE : Toast.Visibility.SHOW;
    }
}
