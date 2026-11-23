package de.opaclient;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

public final class OpaClient implements ClientModInitializer {
    private static KeyBinding openGuiKey;

    @Override
    public void onInitializeClient() {
        openGuiKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.opa-client.open_gui",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_RIGHT_SHIFT,
            KeyBinding.Category.MISC
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (openGuiKey.wasPressed()) {
                if (client.currentScreen == null) {
                    client.setScreen(new OpaClickGuiScreen());
                } else if (client.currentScreen instanceof OpaClickGuiScreen) {
                    client.setScreen(null);
                }
            }
        });
    }

    private static final class OpaClickGuiScreen extends Screen {
        private OpaClickGuiScreen() {
            super(Text.literal("OPA CLIENT"));
        }

        @Override
        public void render(net.minecraft.client.gui.DrawContext context, int mouseX, int mouseY, float delta) {
            this.renderBackground(context, mouseX, mouseY, delta);
            context.drawCenteredTextWithShadow(this.textRenderer, "OPA CLIENT", this.width / 2, this.height / 2 - 30, 0xFFFF3333);
            context.drawCenteredTextWithShadow(this.textRenderer, "ClickGUI wird geladen", this.width / 2, this.height / 2 - 5, 0xFFFFFFFF);
            context.drawCenteredTextWithShadow(this.textRenderer, "Rechte Shift: Schliessen", this.width / 2, this.height / 2 + 20, 0xFFAAAAAA);
            super.render(context, mouseX, mouseY, delta);
        }

        @Override
        public boolean shouldPause() {
            return false;
        }
    }
}
