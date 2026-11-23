package de.opaclient;

import de.opaclient.gui.OpaClickGuiScreen;
import de.opaclient.module.ClientModule;
import de.opaclient.module.ModuleRegistry;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.minecraft.block.entity.BarrelBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.block.entity.MobSpawnerBlockEntity;
import net.minecraft.block.entity.ShulkerBoxBlockEntity;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import org.lwjgl.glfw.GLFW;

import java.util.Comparator;
import java.util.List;

public final class OpaClient implements ClientModInitializer {
    public static final String MOD_ID = "opaclient";
    private static KeyBinding openGuiKey;
    private static double oldGamma = 1.0;
    private static boolean gammaCaptured;

    @Override
    public void onInitializeClient() {
        ModuleRegistry.registerDefaults();
        KeyBinding.Category opaCategory = KeyBinding.Category.create(Identifier.of(MOD_ID, "main"));
        openGuiKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.opaclient.open_gui", InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_RIGHT_SHIFT, opaCategory
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (openGuiKey.wasPressed()) {
                if (client.currentScreen == null) client.setScreen(new OpaClickGuiScreen());
                else if (client.currentScreen instanceof OpaClickGuiScreen) client.setScreen(null);
            }
            ClientModule fullBright = ModuleRegistry.get("Full Bright");
            if (client.options != null && fullBright != null) {
                if (fullBright.isEnabled()) {
                    if (!gammaCaptured) {
                        oldGamma = client.options.getGamma().getValue();
                        gammaCaptured = true;
                    }
                    client.options.getGamma().setValue(16.0);
                } else if (gammaCaptured) {
                    client.options.getGamma().setValue(oldGamma);
                    gammaCaptured = false;
                }
            }
        });

        HudElementRegistry.addLast(Identifier.of(MOD_ID, "client_hud"), (drawContext, tickCounter) -> {
            var client = net.minecraft.client.MinecraftClient.getInstance();
            if (client.player == null || client.world == null) return;
            int x = 8, y = 8;
            if (enabled("Watermark") || enabled("General HUD")) {
                drawContext.fill(x - 3, y - 3, x + 108, y + 13, 0xA8101014);
                drawContext.fill(x - 3, y - 3, x - 1, y + 13, 0xFFE33434);
                drawContext.drawText(client.textRenderer, "OPA CLIENT", x + 2, y, 0xFFFFFFFF, false);
                y += 20;
            }
            if (enabled("Coordinates") || enabled("General HUD")) {
                String coords = String.format("XYZ: %.1f / %.1f / %.1f", client.player.getX(), client.player.getY(), client.player.getZ());
                drawContext.fill(x - 3, y - 3, x + client.textRenderer.getWidth(coords) + 7, y + 13, 0xA8101014);
                drawContext.drawText(client.textRenderer, coords, x + 2, y, 0xFFFFFFFF, false);
                y += 19;
            }
            if (enabled("Chunk Finder") || enabled("Player Chunks")) {
                String chunk = "Chunk: " + (client.player.getBlockX() >> 4) + ", " + (client.player.getBlockZ() >> 4);
                drawContext.fill(x - 3, y - 3, x + client.textRenderer.getWidth(chunk) + 7, y + 13, 0xA8101014);
                drawContext.drawText(client.textRenderer, chunk, x + 2, y, 0xFFFF6666, false);
                y += 19;
            }
            if (enabled("Activity Chunks") || enabled("Player Chunks")) {
                var knownPlayers = client.world.getPlayers().stream().filter(p -> p != client.player).limit(5).toList();
                drawContext.fill(x - 3, y - 3, x + 190, y + 28 + knownPlayers.size() * 14, 0xA8101014);
                drawContext.drawText(client.textRenderer, "CLIENT-KNOWN PLAYERS", x + 2, y, 0xFFFF6666, false);
                y += 14;
                for (var player : knownPlayers) {
                    String line = player.getName().getString() + "  [" + (player.getBlockX() >> 4) + ", " + (player.getBlockZ() >> 4) + "]";
                    drawContext.drawText(client.textRenderer, line, x + 2, y, 0xFFFFFFFF, false);
                    y += 14;
                }
                if (knownPlayers.isEmpty()) drawContext.drawText(client.textRenderer, "Keine weiteren geladenen Spieler", x + 2, y, 0xFFAAAAAA, false);
                y += 14;
            }
            if (enabled("Storage ESP")) {
                List<BlockEntity> storages = client.world.getBlockEntities().stream()
                        .filter(be -> be instanceof ChestBlockEntity || be instanceof BarrelBlockEntity || be instanceof ShulkerBoxBlockEntity)
                        .filter(be -> distanceSquared(client, be) <= 32 * 32)
                        .sorted(Comparator.comparingDouble(be -> distanceSquared(client, be))).limit(5).toList();
                drawContext.fill(x - 3, y - 3, x + 210, y + 14 + storages.size() * 14, 0xA8101014);
                drawContext.drawText(client.textRenderer, "LOADED STORAGE OBJECTS", x + 2, y, 0xFFFF6666, false);
                y += 14;
                for (BlockEntity be : storages) {
                    String line = be.getCachedState().getBlock().getName().getString() + "  " + be.getPos().getX() + " " + be.getPos().getY() + " " + be.getPos().getZ();
                    drawContext.drawText(client.textRenderer, line, x + 2, y, 0xFFFFFFFF, false);
                    y += 14;
                }
            }
            if (enabled("Spawner Notifier") || enabled("Spawner Name Tags")) {
                List<MobSpawnerBlockEntity> spawners = client.world.getBlockEntities().stream()
                        .filter(be -> be instanceof MobSpawnerBlockEntity).map(be -> (MobSpawnerBlockEntity) be)
                        .filter(be -> distanceSquared(client, be) <= 48 * 48)
                        .sorted(Comparator.comparingDouble(be -> distanceSquared(client, be))).limit(5).toList();
                if (!spawners.isEmpty()) {
                    drawContext.fill(x - 3, y - 3, x + 220, y + 14 + spawners.size() * 14, 0xA8101014);
                    drawContext.drawText(client.textRenderer, "LOADED SPAWNERS", x + 2, y, 0xFFFF6666, false);
                    y += 14;
                    for (MobSpawnerBlockEntity spawner : spawners) {
                        var rendered = spawner.getLogic().getRenderedEntity(client.world, spawner.getPos());
                        String mob = rendered == null ? "Unbekannt" : rendered.getName().getString();
                        String line = mob + " Spawner  " + spawner.getPos().getX() + " " + spawner.getPos().getY() + " " + spawner.getPos().getZ();
                        drawContext.drawText(client.textRenderer, line, x + 2, y, 0xFFFFFFFF, false);
                        y += 14;
                    }
                }
            }
            if (enabled("Block ESP") && client.crosshairTarget instanceof BlockHitResult hit) {
                String block = "Block: " + client.world.getBlockState(hit.getBlockPos()).getBlock().getName().getString();
                drawContext.fill(x - 3, y - 3, x + client.textRenderer.getWidth(block) + 7, y + 13, 0xA8101014);
                drawContext.drawText(client.textRenderer, block, x + 2, y, 0xFFFFFFFF, false);
            }
            if (enabled("Target HUD") && client.crosshairTarget instanceof EntityHitResult hit) {
                String target = "Target: " + hit.getEntity().getName().getString();
                int ty = client.getWindow().getScaledHeight() - 28;
                drawContext.fill(x - 4, ty - 4, x + client.textRenderer.getWidth(target) + 8, ty + 14, 0xB0101014);
                drawContext.drawText(client.textRenderer, Text.literal(target), x + 2, ty, 0xFFFFFFFF, false);
            }
            if (enabled("Array List")) {
                int right = client.getWindow().getScaledWidth() - 8, ay = 8;
                for (ClientModule module : ModuleRegistry.all()) {
                    if (!module.isEnabled() || module.name().equals("Array List")) continue;
                    int width = client.textRenderer.getWidth(module.name());
                    drawContext.fill(right - width - 9, ay - 2, right + 2, ay + 12, 0xA8101014);
                    drawContext.fill(right - 2, ay - 2, right + 2, ay + 12, 0xFFE33434);
                    drawContext.drawText(client.textRenderer, module.name(), right - width - 6, ay, 0xFFFFFFFF, false);
                    ay += 15;
                }
            }
        });
    }

    private static double distanceSquared(net.minecraft.client.MinecraftClient client, BlockEntity be) {
        return client.player.squaredDistanceTo(be.getPos().getX() + 0.5, be.getPos().getY() + 0.5, be.getPos().getZ() + 0.5);
    }

    private static boolean enabled(String name) {
        ClientModule module = ModuleRegistry.get(name);
        return module != null && module.isEnabled();
    }
}
