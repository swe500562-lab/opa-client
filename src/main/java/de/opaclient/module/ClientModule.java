package de.opaclient.module;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class ClientModule {
    public static final class Setting {
        private final String name;
        private final String description;
        private final boolean booleanSetting;
        private final double min;
        private final double max;
        private final double step;
        private Object value;

        private Setting(String name, String description, Object value, double min, double max, double step, boolean booleanSetting) {
            this.name = name;
            this.description = description;
            this.value = value;
            this.min = min;
            this.max = max;
            this.step = step;
            this.booleanSetting = booleanSetting;
        }

        public static Setting bool(String name, String description, boolean value) {
            return new Setting(name, description, value, 0, 1, 1, true);
        }

        public static Setting number(String name, String description, double value, double min, double max, double step) {
            return new Setting(name, description, value, min, max, step, false);
        }

        public String name() { return name; }
        public String description() { return description; }
        public boolean isBoolean() { return booleanSetting; }
        public boolean boolValue() { return Boolean.TRUE.equals(value); }
        public int intValue() { return (int) Math.round(((Number) value).doubleValue()); }
        public double doubleValue() { return ((Number) value).doubleValue(); }

        public String displayValue() {
            if (booleanSetting) return boolValue() ? "AN" : "AUS";
            double n = doubleValue();
            return n == Math.rint(n) ? Integer.toString((int) n) : String.format(java.util.Locale.ROOT, "%.1f", n);
        }

        public void toggle() {
            if (booleanSetting) value = !boolValue();
            else {
                double next = doubleValue() + step;
                value = next > max ? min : next;
            }
        }
    }

    private final String name;
    private final String description;
    private final ModuleCategory category;
    private final List<Setting> settings = new ArrayList<>();
    private boolean enabled;

    public ClientModule(String name, String description, ModuleCategory category) {
        this.name = name;
        this.description = description;
        this.category = category;
    }

    public ClientModule addBoolean(String name, String description, boolean value) {
        settings.add(Setting.bool(name, description, value));
        return this;
    }

    public ClientModule addNumber(String name, String description, double value, double min, double max, double step) {
        settings.add(Setting.number(name, description, value, min, max, step));
        return this;
    }

    public String name() { return name; }
    public String description() { return description; }
    public ModuleCategory category() { return category; }
    public boolean isEnabled() { return enabled; }
    public void toggle() { enabled = !enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }
    public List<Setting> settings() { return Collections.unmodifiableList(settings); }
    public Setting setting(String name) {
        return settings.stream().filter(s -> s.name().equals(name)).findFirst().orElse(null);
    }
    public int intSetting(String name, int fallback) {
        Setting setting = setting(name);
        return setting == null ? fallback : setting.intValue();
    }
    public boolean boolSetting(String name, boolean fallback) {
        Setting setting = setting(name);
        return setting == null ? fallback : setting.boolValue();
    }
}