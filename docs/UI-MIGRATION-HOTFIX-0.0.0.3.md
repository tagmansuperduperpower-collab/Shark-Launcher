# Shark Launcher 0.0.0.3 — Modern UI Hotfix / Half Update

## Completed in this hotfix

- The launcher home/dashboard is fully rendered by Jetpack Compose Material 3.
- The old unused launcher fragment XML layouts were removed.
- The dashboard now uses centralized theme tokens, responsive navigation, fixed icon bounds, one scroll owner, and animated transitions.
- Java `HomeActions` is retained only as a compatibility boundary for the existing launcher services; rendering is no longer handled by the legacy home XML.

## Migration status

This is intentionally labelled a **half update**. Operational screens still using XML are retained temporarily because they are active runtime dependencies: Microsoft/local login, installers, profile editing, file selection, custom controls, runtime selection, game launch surfaces, dialogs, and native/gameplay views. Deleting those layouts before their Compose replacements are tested would create regressions.

They will be migrated screen-by-screen into Compose Material 3 components in subsequent hotfixes. No claim is made that every legacy XML file has already been removed.
