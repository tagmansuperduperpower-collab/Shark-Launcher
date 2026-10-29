# Shark Launcher Rebuild Plan

## Product boundary

Shark Launcher owns the product surface: Compose Material 3 UI, navigation, screen state, feature flows, settings presentation, and user-facing design. The retained Minecraft engine is an implementation detail behind `net.sharklauncher.core`.

The core boundary is defined in `app_pojavlauncher/src/main/java/net/sharklauncher/core/SharkCore.kt`. It exposes `MinecraftEngine`, `InstanceRepository`, `AccountGateway`, and `ContentInstaller` without importing legacy Activity, Fragment, XML, or Pojav package classes.

`LegacyEngineBridge` is temporary compatibility code. It may delegate to the retained engine, but the Shark UI must only see the core interfaces.

## Completed phase 1

- Added Shark-owned core contracts and data models.
- Added a compatibility bridge for the retained engine.
- Added Shark-owned screen keys, app state, intents, and a pure reducer.
- Kept the existing launch path working while the new boundary is introduced.
- Did not delete active XML or native surfaces before parity.

## Next phases

1. Wire `LauncherActivity` launch behavior behind `MinecraftEngine`.
2. Add `SharkAppViewModel` using `StateFlow` and the reducer.
3. Replace dashboard callbacks with `SharkIntent` dispatch.
4. Migrate Instances/Profile and Accounts screens.
5. Migrate Settings, Installer, Mods, Files, and Controls screen-by-screen.
6. Replace `mcVersionSpinner`, adapters, and preference fragments with Compose components.
7. Remove each XML file only after the parity gate in `UI-MIGRATION-MATRIX-0.0.0.3.md`.
8. Move retained native renderer/input/gameplay code behind explicit core interfaces.

## Non-goals

- Do not rename every legacy package in one commit.
- Do not delete XML by glob.
- Do not replace OpenGL, input injection, WebView authentication, or SAF with fake Compose equivalents.
