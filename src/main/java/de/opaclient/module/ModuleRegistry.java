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

        add(new ClientModule("Storage ESP", "Zeigt lokal geladene Kisten, Fässer und Shulkerboxen.", ModuleCategory.BASE_FINDS)
                .addNumber("Reichweite", "Maximale Entfernung in Blöcken", 32, 8, 64, 8)
                .addNumber("Max. Anzeigen", "Wie viele Objekte gelistet werden", 5, 1, 15, 1)
                .addBoolean("Koordinaten", "Positionen in der HUD-Liste anzeigen", true));
        add(new ClientModule("Spawner Notifier", "Meldet Spawner, deren Daten dein Client geladen hat.", ModuleCategory.BASE_FINDS)
                .addNumber("Reichweite", "Maximale Entfernung in Blöcken", 48, 8, 96, 8)
                .addNumber("Max. Anzeigen", "Wie viele Spawner gelistet werden", 5, 1, 15, 1)
                .addBoolean("Koordinaten", "Spawner-Positionen anzeigen", true));
        add(new ClientModule("Spawner Name Tags", "Zeigt den lokal bekannten Spawner-Mobtyp als Text.", ModuleCategory.BASE_FINDS)
                .addNumber("Reichweite", "Maximale Entfernung in Blöcken", 48, 8, 96, 8)
                .addBoolean("Koordinaten", "Spawner-Positionen anzeigen", true));
        add(new ClientModule("Chunk Finder", "Zeigt die Koordinaten deines aktuellen Chunks.", ModuleCategory.BASE_FINDS)
                .addBoolean("HUD anzeigen", "Chunk-Info links oben anzeigen", true));
        add(new ClientModule("Activity Chunks", "Listet nur Spieler, die deinem Client aktuell bekannt sind.", ModuleCategory.BASE_FINDS)
                .addNumber("Spieler-Limit", "Maximale Anzahl angezeigter Spieler", 5, 1, 10, 1)
                .addBoolean("Chunk-Koordinaten", "Chunk-Positionen in der Liste anzeigen", true));
        add(new ClientModule("Player Chunks", "Zeigt Chunk-Koordinaten aktuell geladener Spieler.", ModuleCategory.BASE_FINDS)
                .addNumber("Spieler-Limit", "Maximale Anzahl angezeigter Spieler", 5, 1, 10, 1));
        add(new ClientModule("Block ESP", "Zeigt Informationen zum Block unter deinem Fadenkreuz.", ModuleCategory.BASE_FINDS)
                .addBoolean("Blockname", "Den Namen des anvisierten Blocks anzeigen", true));

        add(new ClientModule("Full Bright", "Erhöht lokal die Helligkeit.", ModuleCategory.RENDER)
                .addNumber("Helligkeit", "Lokaler Gamma-Wert", 16, 2, 16, 2));
        add(new ClientModule("Target HUD", "Zeigt den Namen des aktuell anvisierten Wesens.", ModuleCategory.RENDER)
                .addBoolean("Anzeigen", "Target-Info am unteren Bildschirmrand anzeigen", true));

        add(new ClientModule("Watermark", "Zeigt die OPA CLIENT Kennzeichnung.", ModuleCategory.HUD)
                .addBoolean("Anzeigen", "Watermark ein- oder ausblenden", true));
        add(new ClientModule("Coordinates", "Zeigt deine eigenen Koordinaten.", ModuleCategory.HUD)
                .addBoolean("Anzeigen", "Koordinaten-HUD ein- oder ausblenden", true));
        add(new ClientModule("Array List", "Zeigt rechts die aktivierten Module.", ModuleCategory.HUD)
                .addBoolean("Anzeigen", "Aktive Modulliste ein- oder ausblenden", true));
    }

    private static void add(ClientModule module) { MODULES.add(module); }
    public static List<ClientModule> all() { return Collections.unmodifiableList(MODULES); }
    public static List<ClientModule> inCategory(ModuleCategory category) {
        return MODULES.stream().filter(m -> m.category() == category).toList();
    }
    public static ClientModule get(String name) {
        return MODULES.stream().filter(m -> m.name().equals(name)).findFirst().orElse(null);
    }
    public static void disableAll() { MODULES.forEach(m -> m.setEnabled(false)); }
}