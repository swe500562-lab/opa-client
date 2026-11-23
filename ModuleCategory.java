package de.opaclient.module;

public enum ModuleCategory {
    BASE_FINDS("Base Finds"),
    RENDER("Render"),
    HUD("HUD"),
    PLAYER("Player"),
    WORLD("World"),
    MISC("Misc"),
    COSMETICS("Cosmetics");

    private final String displayName;

    ModuleCategory(String displayName) {
        this.displayName = displayName;
    }

    public String displayName() {
        return displayName;
    }
}
