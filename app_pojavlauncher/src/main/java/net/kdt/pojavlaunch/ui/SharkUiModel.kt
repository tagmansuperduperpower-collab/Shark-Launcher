package net.kdt.pojavlaunch.ui

import androidx.compose.ui.graphics.vector.ImageVector

internal enum class SharkSection(val title: String) {
    DASHBOARD("Dashboard"),
    INSTANCES("Instances"),
    MODS("Mods & JARs"),
    CONTROLS("Controls"),
    SETTINGS("Settings")
}

internal data class SharkNavItem(
    val section: SharkSection,
    val icon: ImageVector
)

internal data class SharkQuickAction(
    val title: String,
    val subtitle: String,
    val icon: ImageVector,
    val action: () -> Unit
)
