# Shark Launcher UI Architecture 0.0.0.3

The 0.0.0.3 prototype separates the Compose UI into three layers:

- `SharkTheme.kt`: one Material 3 source of truth for colors, typography, shapes, and motion easing.
- `SharkUiModel.kt`: navigation and action models without rendering logic.
- `SharkLauncherScreen.kt`: responsive screen and reusable components; the legacy `SharkLauncherHome.kt` is now only a Java-facing adapter.

The screen uses a navigation rail at 600dp and above, a bottom navigation bar on compact phones, one explicit vertical scroll container, fixed icon bounds, and animated content transitions. The visual direction is original, informed by Zalith Launcher 2's reusable Material 3 patterns and Prism Launcher-style desktop information hierarchy.
