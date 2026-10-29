# Shark Launcher 0.0.0.3 — Systematic UI Migration Matrix

The launcher is being migrated from Java/XML to a Compose-first Material 3 architecture. XML is not deleted by filename pattern; each active screen must pass a parity gate first.

| Area | Current implementation | Compose target | Gate before XML deletion |
|---|---|---|---|
| Home dashboard | Compose | `ui/SharkLauncherScreen.kt` | Build + responsive layout |
| Settings/preferences | Java fragments + preference XML | `ui/screens/settings` | Every preference reads/writes the same key |
| Login/auth | Java fragments/WebView | `ui/screens/auth` | Local and Microsoft flows complete |
| Profile editor | Java fragment/XML | `ui/screens/profile` | Save, select, delete, and validation |
| Version/instance install | Java fragments/XML | `ui/screens/installer` | Download, progress, cancel, error recovery |
| Mods/JARs | Java adapters/XML | `ui/screens/mods` | List, filter, install, loading, empty state |
| File selector | Java fragment/XML | `ui/screens/files` | SAF permissions and navigation |
| Custom controls | Java activity/XML/native views | Compose shell + native interop | Touch, keyboard, gamepad, save/load |
| Runtime selector | Java adapters/XML | `ui/screens/runtime` | Select, add, delete, and persistence |
| Gameplay/native surface | Native/TextureView host | Compose overlay only | Rendering/input/performance unchanged |
| Legacy dialogs | Java/XML | Material 3 dialogs/sheets | All callers migrated and cancellation preserved |

## Size and motion rules

- Use `SharkUiMetrics` for compact versus expanded layouts; do not hard-code screen-specific dimensions.
- Use one scroll owner per screen (`LazyColumn` or `verticalScroll`, never nested accidentally).
- Use fixed icon bounds and content descriptions for every icon.
- Use Material 3 color scheme, typography, shapes, and shared motion easing from `SharkTheme`.
- Use animated screen transitions only around state boundaries; never animate native rendering surfaces.

## Definition of done per screen

1. Compose screen exists and is reachable from the same user action.
2. All old callbacks are wired to the same service/domain code.
3. Compact phone and landscape/expanded layouts compile and are visually checked.
4. Back, cancellation, loading, error, and empty states are covered.
5. Clean build succeeds.
6. Only then is the corresponding XML removed.
