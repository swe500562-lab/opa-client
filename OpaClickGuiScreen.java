package de.opaclient.gui;

import de.opaclient.module.ClientModule;
import de.opaclient.module.ModuleCategory;
import de.opaclient.module.ModuleRegistry;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.util.List;

public final class OpaClickGuiScreen extends Screen {
    private ModuleCategory selected = ModuleCategory.BASE_FINDS;
    private int scroll;
    private static final int RED = 0xFFE33434;
    private static final int PANEL = 0xF0141418;
    private static final int CARD = 0xFF222229;
    private static final int MUTED = 0xFFAAAAAA;

    public OpaClickGuiScreen() { super(Text.literal("OPA CLIENT")); }

    private int left() { return Math.max(10, (width - 560) / 2); }
    private int top() { return Math.max(10, (height - 390) / 2); }
    private int right() { return Math.min(width - 10, left() + 560); }
    private int bottom() { return Math.min(height - 10, top() + 390); }
    private int listTop() { return top() + 75; }
    private int listBottom() { return bottom() - 28; }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        renderBackground(context, mouseX, mouseY, delta);
        int l = left(), t = top(), r = right(), b = bottom();
        context.fill(l, t, r, b, PANEL);
        context.fill(l, t, r, t + 42, 0xFF101014);
        context.fill(l, t, l + 4, b, RED);
        context.drawText(textRenderer, "OPA CLIENT", l + 17, t + 10, 0xFFFFFFFF, false);
        context.drawText(textRenderer, "HUGOSMP EDITION  /  1.21.11", r - 190, t + 15, RED, false);
        context.drawText(textRenderer, "MODULES", l + 17, t + 54, MUTED, false);

        int index = 0;
        for (ModuleCategory category : ModuleCategory.values()) {
            int y = t + 73 + index * 34;
            boolean active = category == selected;
            context.fill(l + 12, y, l + 142, y + 27, active ? 0xFF512126 : CARD);
            if (active) context.fill(l + 12, y, l + 15, y + 27, RED);
            context.drawText(textRenderer, category.displayName(), l + 23, y + 9, 0xFFFFFFFF, false);
            index++;
        }

        int contentLeft = l + 158;
        context.drawText(textRenderer, selected.displayName().toUpperCase(), contentLeft, t + 55, RED, false);
        context.drawText(textRenderer, "Click a module to enable / disable", contentLeft, t + 69, MUTED, false);
        List<ClientModule> modules = ModuleRegistry.inCategory(selected);
        int y = listTop();
        for (int i = scroll; i < modules.size(); i++) {
            ClientModule module = modules.get(i);
            if (y + 48 > listBottom()) break;
            context.fill(contentLeft, y, r - 14, y + 42, CARD);
            context.drawText(textRenderer, module.name(), contentLeft + 10, y + 7, 0xFFFFFFFF, false);
            String state = module.isEnabled() ? "ON" : "OFF";
            int stateWidth = textRenderer.getWidth(state);
            context.fill(r - 57, y + 7, r - 22, y + 34, module.isEnabled() ? 0xFF70252B : 0xFF393940);
            context.drawText(textRenderer, state, r - 39 - stateWidth / 2, y + 17, module.isEnabled() ? 0xFFFFFFFF : MUTED, false);
            String description = module.description();
            if (description.length() > 66) description = description.substring(0, 63) + "...";
            context.drawText(textRenderer, description, contentLeft + 10, y + 23, MUTED, false);
            y += 49;
        }
        context.drawText(textRenderer, "RIGHT SHIFT  •  ESC", l + 17, b - 16, MUTED, false);
        context.drawText(textRenderer, "Scroll: module list", r - 132, b - 16, MUTED, false);
        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button != 0) return super.mouseClicked(mouseX, mouseY, button);
        int l = left(), t = top(), r = right();
        int index = 0;
        for (ModuleCategory category : ModuleCategory.values()) {
            int y = t + 73 + index * 34;
            if (inside(mouseX, mouseY, l + 12, y, l + 142, y + 27)) {
                selected = category;
                scroll = 0;
                return true;
            }
            index++;
        }
        int y = listTop();
        List<ClientModule> modules = ModuleRegistry.inCategory(selected);
        for (int i = scroll; i < modules.size(); i++) {
            if (y + 48 > listBottom()) break;
            if (inside(mouseX, mouseY, l + 158, y, r - 14, y + 42)) {
                ClientModule module = modules.get(i);
                if (module.name().equals("Panic")) ModuleRegistry.disableAll();
                else module.toggle();
                return true;
            }
            y += 49;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        int max = Math.max(0, ModuleRegistry.inCategory(selected).size() - 5);
        scroll = Math.max(0, Math.min(max, scroll - (int) Math.signum(verticalAmount)));
        return true;
    }

    private static boolean inside(double mx, double my, int x1, int y1, int x2, int y2) {
        return mx >= x1 && mx <= x2 && my >= y1 && my <= y2;
    }
    @Override public boolean shouldPause() { return false; }
}
