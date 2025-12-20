package net.lghast.jiagu.client.screen;

import net.lghast.jiagu.common.system.menu.YaowangGourdMenu;
import net.lghast.jiagu.network.CraftPrescriptionPayload;
import net.lghast.jiagu.utils.lzh.LzhMappings;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.PacketDistributor;

import javax.annotation.ParametersAreNonnullByDefault;

@OnlyIn(Dist.CLIENT)
@ParametersAreNonnullByDefault
public class YaowangGourdScreen extends AbstractContainerScreen<YaowangGourdMenu> {
    private static final int GUI_WIDTH = 176;
    private static final int GUI_HEIGHT = 176;

    private final ResourceLocation guiTexture =
            ResourceLocation.fromNamespaceAndPath("jiagureappear", "textures/gui/yaowang_gourd.png");

    public YaowangGourdScreen(YaowangGourdMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        this.imageWidth = GUI_WIDTH;
        this.imageHeight = GUI_HEIGHT;
        this.inventoryLabelY = 10000;
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;

        graphics.blit(guiTexture, x, y, 0, 0, this.imageWidth, this.imageHeight, this.imageWidth, this.imageHeight);

        renderInscriptionText(graphics, x, y);

        if (menu.getBlockEntity().canCraftPrescription()) {
            renderPrescriptionItem(graphics, x, y);
        }
    }

    private void renderInscriptionText(GuiGraphics graphics, int x, int y) {
        String inscription = menu.getBlockEntity().getCurrentInscription();
        if (!inscription.isEmpty()) {
            int textWidth = this.font.width(inscription);
            int textX = x + (GUI_WIDTH - textWidth) / 2;
            int textY = y + 51;

            graphics.drawString(this.font, inscription, textX, textY, 0x404040, false);
        }
    }

    private void renderPrescriptionItem(GuiGraphics graphics, int x, int y) {
        String inscription = menu.getBlockEntity().getCurrentInscription();
        ItemStack prescription = LzhMappings.getPrescriptionWith(inscription);

        if (!prescription.isEmpty()) {
            int itemX = x + 125;
            int itemY = y + 28;

            graphics.renderItem(prescription, itemX, itemY);
            graphics.renderItemDecorations(this.font, prescription, itemX, itemY);
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0) {
            int guiLeft = (this.width - this.imageWidth) / 2;
            int guiTop = (this.height - this.imageHeight) / 2;

            if (isMouseOverPrescriptionArea(mouseX, mouseY, guiLeft, guiTop)) {
                if (menu.getBlockEntity().canCraftPrescription()) {
                    PacketDistributor.sendToServer(new CraftPrescriptionPayload(menu.getBlockEntity().getBlockPos()));
                    return true;
                }
            }
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    private boolean isMouseOverPrescriptionArea(double mouseX, double mouseY, int guiLeft, int guiTop) {
        int prescriptionX = guiLeft + 125;
        int prescriptionY = guiTop + 28;

        return mouseX >= prescriptionX && mouseX < prescriptionX + 16 &&
                mouseY >= prescriptionY && mouseY < prescriptionY + 16;
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(graphics, mouseX, mouseY, partialTick);
        super.render(graphics, mouseX, mouseY, partialTick);
        this.renderTooltip(graphics, mouseX, mouseY);

        int guiLeft = (this.width - this.imageWidth) / 2;
        int guiTop = (this.height - this.imageHeight) / 2;

        if (isMouseOverPrescriptionArea(mouseX, mouseY, guiLeft, guiTop) &&
                menu.getBlockEntity().canCraftPrescription()) {

            String inscription = menu.getBlockEntity().getCurrentInscription();
            ItemStack prescription = LzhMappings.getPrescriptionWith(inscription);
            graphics.renderTooltip(this.font, prescription, mouseX, mouseY);
        }
    }
}
