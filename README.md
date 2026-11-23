# OPA CLIENT — Fabric 1.21.11

Client-side Fabric mod project for Minecraft 1.21.11 and Java 21. The project is configured for GitHub Actions: pushing the repository or manually starting the workflow builds the remapped mod JAR and uploads it as a workflow artifact.

## Build locally

1. Install JDK 21.
2. Install Gradle 9.2.1 (or use the GitHub Actions workflow).
3. Run `gradle clean build` from the project root.
4. Find the remapped mod JAR in `build/libs/`.

## Build on GitHub

1. Create a GitHub repository and upload the contents of this folder to its root.
2. Open **Actions** and enable workflows if GitHub asks.
3. Run **Build OPA Client** using **Run workflow**, or push a commit.
4. Open the completed workflow run and download the `OPA-CLIENT-Minecraft-1.21.11` artifact.
5. Extract the artifact and place `opa-client-0.1.0.jar` into your Minecraft `mods` folder alongside Fabric API.

## Controls

- `Right Shift`: open or close the ClickGUI.
- Click a category to select it.
- Click a module card to toggle it.
- Scroll over the list to see additional modules.

## Implemented features

- Black/red ClickGUI with category navigation, descriptions, module toggles, and scrolling.
- Full Bright (local gamma setting, restored when disabled).
- Watermark, Coordinates, Chunk Finder, Activity Chunks / Player Chunks (only client-known player count), Target HUD (currently crosshair target), and Array List overlays.
- Panic toggle to disable all modules.

## Modules still marked in development

Zoom, Ambience, Block Outlines customization, Armor HUD, Notifications, Keybinds, Add Friends, Cosmetics, Backpacks, and Wings are registered in the GUI but are not complete implementations yet. They are deliberately labelled as such rather than pretending to work. The Base Finds HUD entries are local readouts/lists, not 3D through-wall renderers.

## Server-data and fair-play boundary

The client does not reveal vanished/hidden players, access server-only data, bypass anti-cheat, or automate combat. Player-related indicators use only entities/players already known to the local Minecraft client.
