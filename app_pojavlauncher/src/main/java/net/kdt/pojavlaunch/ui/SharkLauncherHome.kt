package net.kdt.pojavlaunch.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import com.kdt.mcgui.mcVersionSpinner

interface HomeActions {
    fun onSettings()
    fun onInformation()
    fun onInstallJar(customArgs: Boolean)
    fun onOpenDirectory()
    fun onShareLogs()
    fun onCustomControls()
    fun onWiki()
    fun onDiscord()
    fun onEditProfile(spinner: mcVersionSpinner)
    fun onPrototypeRelease()
}

@JvmOverloads
fun attachHomeContent(view: ComposeView, actions: HomeActions) {
    view.setContent {
        SharkLauncherScreen(
            onSettings = actions::onSettings,
            onInformation = actions::onInformation,
            onInstallJar = actions::onInstallJar,
            onOpenDirectory = actions::onOpenDirectory,
            onShareLogs = actions::onShareLogs,
            onCustomControls = actions::onCustomControls,
            onWiki = actions::onWiki,
            onDiscord = actions::onDiscord,
            onEditProfile = actions::onEditProfile,
            onPrototypeRelease = actions::onPrototypeRelease
        )
    }
}

@Composable
fun SharkLauncherHome(
    onSettings: () -> Unit,
    onOpenInformation: () -> Unit,
    onInstallJar: (Boolean) -> Unit,
    onOpenDirectory: () -> Unit,
    onShareLogs: () -> Unit,
    onCustomControls: () -> Unit,
    onWiki: () -> Unit,
    onDiscord: () -> Unit,
    onEditProfile: (mcVersionSpinner) -> Unit,
    onPrototypeRelease: () -> Unit
) {
    SharkLauncherScreen(onSettings, onOpenInformation, onInstallJar, onOpenDirectory, onShareLogs, onCustomControls, onWiki, onDiscord, onEditProfile, onPrototypeRelease)
}
