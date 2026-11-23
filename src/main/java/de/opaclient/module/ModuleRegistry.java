package de.opaclient.module;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class ModuleRegistry {
    private static final List<ClientModule> MODULES = new ArrayList<>();
    private static boolean registered;

    private ModuleRegistry() {}

    public static void registerDefaults() {
        if (registered) return;
        registered = true;
        add("Storage ESP", "Markiert nur lokal geladene Kisten, Fässer und Shulkerboxen.", ModuleCategory.BASE_FINDS);
        add("Spawner Notifier", "Listet lokal geladene Spawner in der Nähe.", ModuleCategory.BASE_FINDS);
        add("Spawner Name Tags", "Zeigt den Mob-Typ nur an, wenn die Spawner-Daten lokal vorliegen.", ModuleCategory.BASE_FINDS);
        add("Chunk Finder", "Zeigt die Koordinaten deines aktuellen Chunks.", ModuleCategory.BASE_FINDS);
        add("Activity Chunks", "Zeigt nur Spieler, die deinem Client aktuell bekannt sind.", ModuleCategory.BASE_FINDS);
        add("Player Chunks", "Zeigt Chunk-Koordinaten aktuell clientseitig geladener Spieler.", ModuleCategory.BASE_FINDS);
        add("Block ESP", "Zeigt Informationen zum Block, den du gerade ansiehst.", ModuleCategory.BASE_FINDS);

        add("Full Bright", "Erhöht lokal die Gamma-Einstellung.", ModuleCategory.RENDER);
        add("Ambience", "Lokale Anzeigeoption (in Entwicklung).", ModuleCategory.RENDER);
        add("Zoom", "Lokaler Zoom (in Entwicklung).", ModuleCategory.RENDER);
        add("Block Outlines", "Vanilla-Blockauswahl bleibt aktiv.", ModuleCategory.RENDER);
        add("General HUD", "Schaltet OPA-HUD-Elemente frei.", ModuleCategory.HUD);
        add("Array List", "Zeigt aktivierte OPA-Module.", ModuleCategory.HUD);
        add("Armor HUD", "Eigene Rüstungsanzeige (in Entwicklung).", ModuleCategory.HUD);
        add("Target HUD", "Zeigt das aktuell anvisierte Wesen.", ModuleCategory.HUD);
        add("Watermark", "OPA-Client-Kennzeichnung.", ModuleCategory.HUD);
        add("Notifications", "Lokale Statusanzeige (in Entwicklung).", ModuleCategory.HUD);
        add("Keybinds", "Tastenübersicht (in Entwicklung).", ModuleCategory.HUD);
        add("Coordinates", "Zeigt deine eigenen Koordinaten.", ModuleCategory.HUD);
        add("Panic", "Schaltet alle OPA-Module aus.", ModuleCategory.MISC);
        add("Add Friends", "Lokale Freundesliste (in Entwicklung).", ModuleCategory.MISC);
        add("Cosmetics", "Kosmetische Darstellung (in Entwicklung).", ModuleCategory.COSMETICS);
        add("Backpacks", "Kosmetische Rucksack-Darstellung (in Entwicklung).", ModuleCategory.COSMETICS);
        add("Wings", "Kosmetische Flügel-Darstellung (in Entwicklung).", ModuleCategory.COSMETICS);
    }

    private static void add(String name, String description, ModuleCategory category) {
        MODULES.add(new ClientModule(name, description, category));
    }

    public static List<ClientModule> all() { return Collections.unmodifiableList(MODULES); }
    public static List<ClientModule> inCategory(ModuleCategory category) {
        return MODULES.stream().filter(m -> m.category() == category).toList();
    }
    public static ClientModule get(String name) {
        return MODULES.stream().filter(m -> m.name().equals(name)).findFirst().orElse(null);
    }
    public static void disableAll() { MODULES.forEach(m -> m.setEnabled(false)); }
}
