package net.lghast.jiagu.client.screen;

import net.lghast.jiagu.common.system.menu.CharacterCrafterMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import javax.annotation.ParametersAreNonnullByDefault;

@OnlyIn(Dist.CLIENT)
@ParametersAreNonnullByDefault
public class CharacterCrafterScreen extends AbstractContainerScreen<CharacterCrafterMenu> {
    private static final ResourceLocation ASSEMBLE_TEXTURE =
            ResourceLocation.fromNamespaceAndPath("jiagureappear", "textures/gui/assemble.png");
    private static final ResourceLocation DISASSEMBLE_TEXTURE =
            ResourceLocation.fromNamespaceAndPath("jiagureappear", "textures/gui/disassemble.png");
    private static final int GUI_WIDTH = 176;
    private static final int GUI_HEIGHT = 185;
    private static final int BUTTON_X = 156;
    private static final int BUTTON_Y = 4;

    private ModeSwitchButton switchButton;

    public CharacterCrafterScreen(CharacterCrafterMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        this.imageWidth = GUI_WIDTH;
        this.imageHeight = GUI_HEIGHT;
        this.titleLabelX = 8;
        this.titleLabelY = 6;
        this.inventoryLabelX = 8;
        this.inventoryLabelY = 92;
    }

    @Override
    protected void init() {
        super.init();
        this.switchButton = new ModeSwitchButton(this.leftPos + BUTTON_X, this.topPos + BUTTON_Y,
                button -> {
                    if (this.minecraft != null && this.minecraft.gameMode != null) {
                        this.minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, 0);
                    }
                });
        this.addRenderableWidget(this.switchButton);
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
        ResourceLocation texture = this.menu.getMode() == CharacterCrafterMenu.MODE_DISASSEMBLE
                ? DISASSEMBLE_TEXTURE
                : ASSEMBLE_TEXTURE;
        graphics.blit(texture, this.leftPos, this.topPos, 0, 0, GUI_WIDTH, GUI_HEIGHT, GUI_WIDTH, GUI_HEIGHT);
    }

    @Override
    protected void renderLabels(GuiGraphics graphics, int mouseX, int mouseY) {
        Component title = this.menu.getMode() == CharacterCrafterMenu.MODE_DISASSEMBLE
                ? Component.translatable("gui.jiagureappear.character_crafter.title_disassemble")
                : Component.translatable("gui.jiagureappear.character_crafter.title_assemble");
        graphics.drawString(this.font, title, this.titleLabelX, this.titleLabelY, 0x404040, false);
        graphics.drawString(this.font, this.playerInventoryTitle,
                this.inventoryLabelX, this.inventoryLabelY, 0x404040, false);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(graphics, mouseX, mouseY, partialTick);
        super.render(graphics, mouseX, mouseY, partialTick);
        if (this.switchButton.isHovered()) {
            Component tip = this.menu.getMode() == CharacterCrafterMenu.MODE_DISASSEMBLE
                    ? Component.translatable("gui.jiagureappear.character_crafter.to_assemble")
                    : Component.translatable("gui.jiagureappear.character_crafter.to_disassemble");
            graphics.renderTooltip(this.font, tip, mouseX, mouseY);
        }
        this.renderTooltip(graphics, mouseX, mouseY);
    }

    private static class ModeSwitchButton extends Button {
        private static final ResourceLocation BUTTON_TEXTURE =
                ResourceLocation.fromNamespaceAndPath("jiagureappear", "textures/gui/menu_transferring_button.png");

        private ModeSwitchButton(int x, int y, OnPress onPress) {
            super(x, y, 16, 16, Component.empty(), onPress, Button.DEFAULT_NARRATION);
        }

        @Override
        public void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
            graphics.blit(BUTTON_TEXTURE, this.getX(), this.getY(), 0.0F, 0.0F, 16, 16, 16, 16);
            if (this.isHovered()) {
                graphics.fill(this.getX(), this.getY(), this.getX() + 16, this.getY() + 16, 0x22FFFFFF);
            }
        }
    }
}
