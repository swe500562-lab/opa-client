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

        // Base Finds: strictly limited to data already available to the local client.
        add("Storage ESP", "Lokale Markierung bekannter Speicher-Blockobjekte (in Entwicklung).", ModuleCategory.BASE_FINDS);
        add("Spawner Notifier", "Hinweis bei lokal anvisierten Spawner-Blockobjekten (in Entwicklung).", ModuleCategory.BASE_FINDS);
        add("Spawner Name Tags", "Mob-Typ nur wenn die Spawner-Daten dem Client vorliegen (in Entwicklung).", ModuleCategory.BASE_FINDS);
        add("Chunk Finder", "Zeigt die Koordinaten des aktuell betretenen Chunks.", ModuleCategory.BASE_FINDS);
        add("Activity Chunks", "Zeigt nur Spieler, die dem Client aktuell bekannt sind.", ModuleCategory.BASE_FINDS);
        add("Player Chunks", "Zeigt Chunk-Koordinaten aktuell clientseitig geladener Spieler.", ModuleCategory.BASE_FINDS);
        add("Block ESP", "Lokale Blockmarkierungen (in Entwicklung).", ModuleCategory.BASE_FINDS);

        add("Full Bright", "Erhöht die lokale Gamma-Einstellung.", ModuleCategory.RENDER);
        add("Ambience", "Lokale Anzeigeoption (in Entwicklung).", ModuleCategory.RENDER);
        add("Zoom", "Lokaler Zoom (in Entwicklung).", ModuleCategory.RENDER);
        add("Block Outlines", "Vanilla-Blockauswahl bleibt aktiv.", ModuleCategory.RENDER);
        add("General HUD", "Schaltet die OPA-HUD-Elemente frei.", ModuleCategory.HUD);
        add("Array List", "Zeigt aktivierte OPA-Module.", ModuleCategory.HUD);
        add("Armor HUD", "Anzeige der eigenen Rüstung (in Entwicklung).", ModuleCategory.HUD);
        add("Target HUD", "Zeigt das aktuell anvisierte Wesen.", ModuleCategory.HUD);
        add("Watermark", "OPA-Client-Kennzeichnung.", ModuleCategory.HUD);
        add("Notifications", "Lokale Statusanzeige (in Entwicklung).", ModuleCategory.HUD);
        add("Keybinds", "Anzeige der konfigurierten Tastenbelegungen (in Entwicklung).", ModuleCategory.HUD);
        add("Coordinates", "Anzeige der eigenen Koordinaten.", ModuleCategory.HUD);
        add("Panic", "Schaltet alle OPA-Client-Module aus.", ModuleCategory.MISC);
        add("Add Friends", "Lokale Freundesliste (in Entwicklung).", ModuleCategory.MISC);
        add("Cosmetics", "Kosmetische lokale Darstellung (in Entwicklung).", ModuleCategory.COSMETICS);
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
    public static void disableAll() { MODULES.forEach(module -> module.setEnabled(false)); }
}
