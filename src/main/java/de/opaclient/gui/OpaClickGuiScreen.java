package de.opaclient.gui;

import de.opaclient.module.ClientModule;
import de.opaclient.module.ModuleCategory;
import de.opaclient.module.ModuleRegistry;
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.util.List;

public final class OpaClickGuiScreen extends Screen {
    private ModuleCategory selected = ModuleCategory.BASE_FINDS;
    private ClientModule selectedModule;
    private int scroll;
    private static final int RED = 0xFFE33434;
    private static final int BG = 0xF00B0B0F;
    private static final int PANEL = 0xFF15151B;
    private static final int CARD = 0xFF202027;
    private static final int CARD_HOVER = 0xFF2B2025;
    private static final int MUTED = 0xFF9999A5;
    private static final int WHITE = 0xFFF4F4F7;

    public OpaClickGuiScreen() {
        super(Text.literal("OPA CLIENT"));
        List<ClientModule> initial = ModuleRegistry.inCategory(selected);
        if (!initial.isEmpty()) selectedModule = initial.get(0);
    }

    private int left() { return Math.max(8, (width - 760) / 2); }
    private int top() { return Math.max(8, (height - 440) / 2); }
    private int right() { return Math.min(width - 8, left() + 760); }
    private int bottom() { return Math.min(height - 8, top() + 440); }
    private int moduleTop() { return top() + 92; }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        renderBackground(context, mouseX, mouseY, delta);
        int l = left(), t = top(), r = right(), b = bottom();
        context.fill(l, t, r, b, BG);
        context.fill(l, t, r, t + 58, PANEL);
        context.fill(l, t, l + 4, b, RED);
        context.drawText(textRenderer, "OPA", l + 18, t + 12, RED, false);
        context.drawText(textRenderer, "CLIENT", l + 52, t + 12, WHITE, false);
        context.drawText(textRenderer, "1.21.11  /  FABRIC", r - 132, t + 16, MUTED, false);
        context.fill(l + 14, t + 66, r - 14, t + 67, 0xFF303038);

        // Category sidebar
        context.fill(l + 12, t + 78, l + 150, b - 14, PANEL);
        context.drawText(textRenderer, "KATEGORIEN", l + 23, t + 88, MUTED, false);
        int ci = 0;
        for (ModuleCategory category : ModuleCategory.values()) {
            int cy = t + 111 + ci * 38;
            boolean active = category == selected;
            context.fill(l + 19, cy, l + 143, cy + 30, active ? 0xFF422126 : PANEL);
            if (active) context.fill(l + 19, cy, l + 22, cy + 30, RED);
            context.drawText(textRenderer, category.displayName(), l + 30, cy + 10, active ? WHITE : MUTED, false);
            ci++;
        }
        context.drawText(textRenderer, "RIGHT SHIFT", l + 23, b - 37, RED, false);
        context.drawText(textRenderer, "GUI schließen", l + 23, b - 24, MUTED, false);

        // Module list panel
        int ml = l + 162, mr = l + 452;
        context.fill(ml, t + 78, mr, b - 14, PANEL);
        context.drawText(textRenderer, selected.displayName().toUpperCase(), ml + 14, t + 88, RED, false);
        List<ClientModule> modules = ModuleRegistry.inCategory(selected);
        context.drawText(textRenderer, modules.size() + " MODULE", mr - 78, t + 88, MUTED, false);
        int y = moduleTop();
        for (int i = scroll; i < modules.size(); i++) {
            ClientModule module = modules.get(i);
            if (y + 55 > b - 22) break;
            boolean active = module == selectedModule;
            context.fill(ml + 10, y, mr - 10, y + 48, active ? CARD_HOVER : CARD);
            if (active) context.fill(ml + 10, y, ml + 13, y + 48, RED);
            context.drawText(textRenderer, module.name(), ml + 20, y + 8, WHITE, false);
            String desc = module.description();
            if (desc.length() > 38) desc = desc.substring(0, 35) + "...";
            context.drawText(textRenderer, desc, ml + 20, y + 25, MUTED, false);
            int sx = mr - 72;
            context.fill(sx, y + 10, sx + 27, y + 37, module.isEnabled() ? 0xFF8E3038 : 0xFF383840);
            context.drawText(textRenderer, module.isEnabled() ? "ON" : "OFF", sx + 5, y + 19, WHITE, false);
            context.fill(mr - 39, y + 10, mr - 15, y + 37, 0xFF34343D);
            context.drawText(textRenderer, "⚙", mr - 34, y + 18, active ? RED : WHITE, false);
            y += 55;
        }

        // Settings panel
        int sl = l + 464, sr = r - 14;
        context.fill(sl, t + 78, sr, b - 14, PANEL);
        context.drawText(textRenderer, "MODULE SETTINGS", sl + 14, t + 88, RED, false);
        if (selectedModule == null) {
            context.drawText(textRenderer, "Wähle ein Modul aus.", sl + 14, t + 120, MUTED, false);
        } else {
            context.drawText(textRenderer, selectedModule.name(), sl + 14, t + 111, WHITE, false);
            String state = selectedModule.isEnabled() ? "AKTIV" : "INAKTIV";
            context.drawText(textRenderer, state, sr - textRenderer.getWidth(state) - 14, t + 111,
                    selectedModule.isEnabled() ? RED : MUTED, false);
            context.fill(sl + 14, t + 129, sr - 14, t + 130, 0xFF303038);
            if (selectedModule.settings().isEmpty()) {
                context.drawText(textRenderer, "Keine Einstellungen vorhanden.", sl + 14, t + 148, MUTED, false);
            } else {
                int sy = t + 143;
                for (ClientModule.Setting setting : selectedModule.settings()) {
                    context.fill(sl + 10, sy, sr - 10, sy + 48, CARD);
                    context.drawText(textRenderer, setting.name(), sl + 19, sy + 7, WHITE, false);
                    String value = setting.displayValue();
                    int vw = textRenderer.getWidth(value);
                    context.drawText(textRenderer, value, sr - 20 - vw, sy + 7, RED, false);
                    String help = setting.description();
                    if (help.length() > 34) help = help.substring(0, 31) + "...";
                    context.drawText(textRenderer, help, sl + 19, sy + 25, MUTED, false);
                    sy += 55;
                }
            }
        }
        context.drawText(textRenderer, "Modulkarte: AN/AUS   •   Zahnrad: Settings   •   Einstellung anklicken: ändern",
                l + 165, b - 10, MUTED, false);
        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public boolean mouseClicked(Click click, boolean doubled) {
        double mx = click.x(), my = click.y();
        if (click.button() != 0) return super.mouseClicked(click, doubled);
        int l = left(), t = top(), b = bottom();
        int ci = 0;
        for (ModuleCategory category : ModuleCategory.values()) {
            int cy = t + 111 + ci * 38;
            if (inside(mx, my, l + 19, cy, l + 143, cy + 30)) {
                selected = category;
                scroll = 0;
                List<ClientModule> list = ModuleRegistry.inCategory(selected);
                selectedModule = list.isEmpty() ? null : list.get(0);
                return true;
            }
            ci++;
        }

        int ml = l + 162, mr = l + 452;
        int y = moduleTop();
        List<ClientModule> modules = ModuleRegistry.inCategory(selected);
        for (int i = scroll; i < modules.size(); i++) {
            if (y + 55 > b - 22) break;
            if (inside(mx, my, ml + 10, y, mr - 10, y + 48)) {
                ClientModule module = modules.get(i);
                selectedModule = module;
                if (mx >= mr - 75 && mx <= mr - 10) {
                    if (mx < mr - 42) module.toggle();
                } else {
                    module.toggle();
                }
                return true;
            }
            y += 55;
        }

        if (selectedModule != null) {
            int sl = l + 464, sr = right() - 14;
            int sy = t + 143;
            for (ClientModule.Setting setting : selectedModule.settings()) {
                if (inside(mx, my, sl + 10, sy, sr - 10, sy + 48)) {
                    setting.toggle();
                    return true;
                }
                sy += 55;
            }
        }
        return super.mouseClicked(click, doubled);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        int visible = Math.max(1, (bottom() - moduleTop() - 24) / 55);
        int max = Math.max(0, ModuleRegistry.inCategory(selected).size() - visible);
        scroll = Math.max(0, Math.min(max, scroll - (int) Math.signum(verticalAmount)));
        return true;
    }

    private static boolean inside(double mx, double my, int x1, int y1, int x2, int y2) {
        return mx >= x1 && mx <= x2 && my >= y1 && my <= y2;
    }

    @Override public boolean shouldPause() { return false; }
}