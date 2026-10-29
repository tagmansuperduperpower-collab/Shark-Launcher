package net.kdt.pojavlaunch.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Gamepad
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.platform.ComposeView
import com.kdt.mcgui.mcVersionSpinner
import net.kdt.pojavlaunch.CustomControlsActivity
import net.kdt.pojavlaunch.R
import net.kdt.pojavlaunch.Tools
import net.kdt.pojavlaunch.extra.ExtraConstants
import net.kdt.pojavlaunch.extra.ExtraCore
import net.kdt.pojavlaunch.fragments.GamepadMapperFragment
import net.kdt.pojavlaunch.prefs.LauncherPreferences
import net.kdt.pojavlaunch.progresskeeper.ProgressKeeper
import java.io.File

private val SharkNavy = Color(0xFF071A2B)
private val SharkBlue = Color(0xFF0D6EFD)
private val SharkSurface = Color(0xFF102B42)

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
        SharkLauncherHome(
            actions::onSettings, actions::onInformation, actions::onInstallJar,
            actions::onOpenDirectory, actions::onShareLogs, actions::onCustomControls,
            actions::onWiki, actions::onDiscord, actions::onEditProfile, actions::onPrototypeRelease
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
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
    var spinner by remember { mutableStateOf<mcVersionSpinner?>(null) }
    var showInformation by remember { mutableStateOf(false) }

    MaterialTheme {
        Scaffold(
            containerColor = SharkNavy,
            topBar = {
                TopAppBar(
                    title = {
                        Column {
                            Text("SHARK LAUNCHER", fontWeight = FontWeight.Bold, letterSpacing = 1.5.sp)
                            Text("Minecraft Java for Android", fontSize = 12.sp, color = Color(0xFF9DB8CF))
                        }
                    },
                    actions = {
                        IconButton(onClick = { showInformation = true }) { Icon(Icons.Default.Info, "Information") }
                        IconButton(onClick = onSettings) { Icon(Icons.Default.MoreVert, "Settings") }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = SharkNavy, titleContentColor = Color.White)
                )
            }
        ) { padding ->
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding).padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    Spacer(Modifier.height(8.dp))
                    Card(
                        colors = CardDefaults.cardColors(containerColor = SharkSurface),
                        shape = RoundedCornerShape(24.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(Modifier.padding(20.dp)) {
                            Text("Ready to dive in?", color = Color.White, fontSize = 25.sp, fontWeight = FontWeight.Bold)
                            Spacer(Modifier.height(6.dp))
                            Text("Choose a profile, tune your controls, and launch your world.", color = Color(0xFFB7C9D8))
                            Spacer(Modifier.height(16.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                AndroidView(
                                    factory = { ctx -> mcVersionSpinner(ctx).also { spinner = it } },
                                    modifier = Modifier.weight(1f).height(56.dp),
                                    update = { it.reloadProfiles() }
                                )
                                Spacer(Modifier.width(8.dp))
                                OutlinedButton(onClick = { spinner?.let { onEditProfile(it) } }) { Text("Edit") }
                            }
                            Spacer(Modifier.height(14.dp))
                            Button(
                                onClick = { ExtraCore.setValue(ExtraConstants.LAUNCH_GAME, true) },
                                modifier = Modifier.fillMaxWidth().height(54.dp),
                                shape = RoundedCornerShape(16.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = SharkBlue)
                            ) {
                                Icon(Icons.Default.PlayArrow, null)
                                Spacer(Modifier.width(8.dp))
                                Text("PLAY MINECRAFT", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
                item {
                    Card(
                        onClick = onPrototypeRelease,
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF174E70)),
                        shape = RoundedCornerShape(18.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Download, null, tint = Color(0xFF8BD3FF))
                            Spacer(Modifier.width(14.dp))
                            Column(Modifier.weight(1f)) {
                                Text("Prototype release v0.0.0.1", color = Color.White, fontWeight = FontWeight.Bold)
                                Text("Download the latest test APK from GitHub", color = Color(0xFFB7DDF2), fontSize = 13.sp)
                            }
                            Text("OPEN", color = Color(0xFF8BD3FF), fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                    }
                }
                item { Text("Quick actions", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp) }
                items(
                    listOf(
                        ActionItem("Wiki & news", "Updates, guides and launcher news", Icons.Default.Language, onWiki),
                        ActionItem("Discord community", "Join the Shark Launcher community", Icons.Default.Description, onDiscord),
                        ActionItem("Custom controls", "Configure touch and gamepad controls", Icons.Default.Gamepad, onCustomControls),
                        ActionItem("Install JAR / mod", "Install a Minecraft JAR or modpack", Icons.Default.Code) { onInstallJar(false) },
                        ActionItem("Share logs", "Send the latest launcher log", Icons.Default.Share, onShareLogs),
                        ActionItem("Game directory", "Open the active profile folder", Icons.Default.Folder, onOpenDirectory)
                    )
                ) { action ->
                    Card(
                        onClick = action.onClick,
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF12334E)),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                            Icon(action.icon, null, tint = Color(0xFF63B3FF))
                            Spacer(Modifier.width(14.dp))
                            Column { Text(action.title, color = Color.White, fontWeight = FontWeight.SemiBold); Text(action.subtitle, color = Color(0xFF9DB8CF), fontSize = 13.sp) }
                        }
                    }
                }
                item { Spacer(Modifier.height(12.dp)) }
            }
        }
    }
    if (showInformation) InformationDialog { showInformation = false }
}

private data class ActionItem(val title: String, val subtitle: String, val icon: androidx.compose.ui.graphics.vector.ImageVector, val onClick: () -> Unit)

@Composable
fun InformationDialog(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("About Shark Launcher") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text("Shark Launcher is still in the prototype stage.", fontWeight = FontWeight.SemiBold)
                Text("This project is based on the PojavLauncher codebase and is not made entirely from scratch. PojavLauncher and its contributors deserve credit for the original work.")
                Text("The Shark Launcher interface and branding are a community modification. Features may change, break, or remain unfinished while development continues.")
            }
        },
        confirmButton = { Button(onClick = onDismiss) { Text("Close") } }
    )
}
