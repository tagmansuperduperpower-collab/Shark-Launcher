package net.sharklauncher.ui

import net.sharklauncher.core.EngineState
import net.sharklauncher.core.InstallState
import net.sharklauncher.core.SharkAccount
import net.sharklauncher.core.SharkInstance

sealed interface SharkScreen {
    data object Home : SharkScreen
    data object Instances : SharkScreen
    data object Mods : SharkScreen
    data object Accounts : SharkScreen
    data object Settings : SharkScreen
    data object Controls : SharkScreen
    data object Files : SharkScreen
    data object Installer : SharkScreen
}

data class SharkAppState(
    val screen: SharkScreen = SharkScreen.Home,
    val instances: List<SharkInstance> = emptyList(),
    val accounts: List<SharkAccount> = emptyList(),
    val selectedInstanceId: String? = null,
    val engine: EngineState = EngineState.Idle,
    val installer: InstallState = InstallState.Idle,
    val message: String? = null
)

sealed interface SharkIntent {
    data class Navigate(val screen: SharkScreen) : SharkIntent
    data class SelectInstance(val instanceId: String) : SharkIntent
    data object Launch : SharkIntent
    data object Stop : SharkIntent
    data object ClearMessage : SharkIntent
}
