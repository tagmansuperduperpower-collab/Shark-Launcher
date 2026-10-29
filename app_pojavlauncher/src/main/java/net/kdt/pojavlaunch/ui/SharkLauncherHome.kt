package net.kdt.pojavlaunch.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material.icons.filled.Storage
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
import androidx.compose.material3.OutlinedCard
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.kdt.mcgui.mcVersionSpinner
import net.kdt.pojavlaunch.R
import net.kdt.pojavlaunch.Tools
import net.kdt.pojavlaunch.extra.ExtraConstants
import net.kdt.pojavlaunch.extra.ExtraCore

private val SharkBackground = Color(0xFF07111F)
private val SharkPanel = Color(0xFF0E1D2E)
private val SharkPanelRaised = Color(0xFF142941)
private val SharkBorder = Color(0xFF24415D)
private val SharkCyan = Color(0xFF55D6FF)
private val SharkBlue = Color(0xFF2D8CFF)
private val SharkText = Color(0xFFF2F7FC)
private val SharkMuted = Color(0xFF8EA5BA)

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
    var selectedSection by remember { mutableStateOf(0) }
    var showInformation by remember { mutableStateOf(false) }
    var spinner by remember { mutableStateOf<mcVersionSpinner?>(null) }
    var playPressed by remember { mutableStateOf(false) }
    val playScale by animateFloatAsState(
        if (playPressed) 0.97f else 1f,
        animationSpec = spring(stiffness = 700f),
        label = "play-scale"
    )

    val navItems = listOf(
        NavItem("Dashboard", Icons.Default.SportsEsports),
        NavItem("Instances", Icons.Default.Storage),
        NavItem("Mods", Icons.Default.Code),
        NavItem("Controls", Icons.Default.Gamepad),
        NavItem("Settings", Icons.Default.Settings)
    )

    MaterialTheme {
        Surface(modifier = Modifier.fillMaxSize(), color = SharkBackground) {
            Row(modifier = Modifier.fillMaxSize()) {
                Column(
                    modifier = Modifier
                        .width(82.dp)
                        .fillMaxHeight()
                        .background(SharkPanel)
                        .padding(vertical = 18.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .background(SharkCyan, RoundedCornerShape(13.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("S", color = SharkBackground, fontWeight = FontWeight.Black, fontSize = 23.sp)
                    }
                    Spacer(Modifier.height(26.dp))
                    navItems.forEachIndexed { index, item ->
                        RailItem(
                            item = item,
                            selected = selectedSection == index,
                            onClick = {
                                selectedSection = index
                                when (index) {
                                    2 -> onInstallJar(false)
                                    3 -> onCustomControls()
                                    4 -> onSettings()
                                }
                            }
                        )
                        Spacer(Modifier.height(8.dp))
                    }
                    Spacer(Modifier.weight(1f))
                    RailItem(NavItem("About", Icons.Default.Info), false) { showInformation = true }
                }

                Scaffold(
                    modifier = Modifier.weight(1f),
                    containerColor = SharkBackground,
                    topBar = {
                        TopAppBar(
                            title = {
                                Column {
                                    Text("SHARK LAUNCHER", color = SharkText, fontSize = 18.sp, fontWeight = FontWeight.Black, letterSpacing = 2.sp)
                                    Text("PC-style Minecraft Java dashboard", color = SharkMuted, fontSize = 11.sp)
                                }
                            },
                            actions = {
                                IconButton(onClick = onWiki) { FixedIcon(Icons.Default.Language, "News") }
                                IconButton(onClick = onSettings) { FixedIcon(Icons.Default.MoreVert, "Settings") }
                            },
                            colors = TopAppBarDefaults.topAppBarColors(containerColor = SharkBackground)
                        )
                    }
                ) { padding ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(padding)
                            .padding(horizontal = 22.dp, vertical = 12.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        AnimatedVisibility(
                            visible = selectedSection == 0,
                            enter = fadeIn() + slideInHorizontally { it / 3 },
                            exit = fadeOut() + slideOutHorizontally { -it / 3 }
                        ) {
                            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                                Row(verticalAlignment = Alignment.Bottom) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text("Welcome back, player", color = SharkMuted, fontSize = 13.sp)
                                        Text("Ready to launch?", color = SharkText, fontSize = 28.sp, fontWeight = FontWeight.Bold)
                                    }
                                    StatusPill("ONLINE", SharkCyan)
                                }
                                PlayPanel(
                                    spinner = spinner,
                                    onSpinner = { spinner = it },
                                    onEditProfile = onEditProfile,
                                    onPlay = {
                                        playPressed = true
                                        ExtraCore.setValue(ExtraConstants.LAUNCH_GAME, true)
                                    },
                                    scale = playScale
                                )
                                Text("QUICK ACCESS", color = SharkMuted, fontSize = 12.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.4.sp)
                                Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                                    QuickAction("Mods & JARs", "Install", Icons.Default.Code, Modifier.weight(1f), onClick = { onInstallJar(false) })
                                    QuickAction("Game folder", "Browse", Icons.Default.Folder, Modifier.weight(1f), onClick = onOpenDirectory)
                                    QuickAction("Controls", "Configure", Icons.Default.Gamepad, Modifier.weight(1f), onClick = onCustomControls)
                                    QuickAction("Share logs", "Support", Icons.Default.Share, Modifier.weight(1f), onClick = onShareLogs)
                                }
                                PrototypeBanner(onPrototypeRelease)
                            }
                        }
                        AnimatedVisibility(visible = selectedSection != 0, enter = fadeIn(), exit = fadeOut()) {
                            SectionPlaceholder(selectedSection, onWiki, onDiscord, onOpenDirectory)
                        }
                    }
                }
            }
        }
    }

    if (showInformation) InformationDialog { showInformation = false }
}

@Composable
private fun PlayPanel(
    spinner: mcVersionSpinner?,
    onSpinner: (mcVersionSpinner) -> Unit,
    onEditProfile: (mcVersionSpinner) -> Unit,
    onPlay: () -> Unit,
    scale: Float
) {
    Card(
        modifier = Modifier.fillMaxWidth().scale(scale),
        shape = RoundedCornerShape(22.dp),
        border = BorderStroke(1.dp, SharkBorder),
        colors = CardDefaults.cardColors(containerColor = SharkPanelRaised)
    ) {
        Row(modifier = Modifier.fillMaxWidth().padding(20.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text("SELECTED INSTANCE", color = SharkCyan, fontSize = 11.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.2.sp)
                Text("Minecraft Java Edition", color = SharkText, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                Text("Choose a profile and start your world", color = SharkMuted, fontSize = 13.sp)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                    AndroidView(
                        factory = { ctx -> mcVersionSpinner(ctx).also(onSpinner) },
                        modifier = Modifier.width(210.dp).height(52.dp),
                        update = { it.reloadProfiles() }
                    )
                    OutlinedButton(onClick = { spinner?.let(onEditProfile) }, modifier = Modifier.height(48.dp)) { Text("EDIT") }
                }
            }
            Spacer(Modifier.width(18.dp))
            Button(
                onClick = onPlay,
                modifier = Modifier.width(166.dp).height(112.dp),
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(containerColor = SharkBlue)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(5.dp)) {
                    FixedIcon(Icons.Default.PlayArrow, "Play", 31.dp)
                    Text("PLAY NOW", fontWeight = FontWeight.Black, fontSize = 13.sp, letterSpacing = 1.sp)
                }
            }
        }
    }
}

@Composable
private fun QuickAction(title: String, subtitle: String, icon: ImageVector, modifier: Modifier, onClick: () -> Unit) {
    val iconTint by androidx.compose.animation.animateColorAsState(SharkCyan, label = "quick-icon")
    Card(
        modifier = modifier.height(88.dp).clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, SharkBorder),
        colors = CardDefaults.cardColors(containerColor = SharkPanel)
    ) {
        Row(modifier = Modifier.fillMaxSize().padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(38.dp).background(Color(0xFF17344D), RoundedCornerShape(11.dp)), contentAlignment = Alignment.Center) {
                Icon(icon, contentDescription = title, tint = iconTint, modifier = Modifier.size(21.dp))
            }
            Spacer(Modifier.width(9.dp))
            Column {
                Text(title, color = SharkText, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                Text(subtitle, color = SharkMuted, fontSize = 11.sp)
            }
        }
    }
}

@Composable
private fun PrototypeBanner(onClick: () -> Unit) {
    OutlinedCard(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth().height(62.dp),
        shape = RoundedCornerShape(15.dp),
        border = BorderStroke(1.dp, Color(0xFF2B5878)),
        colors = CardDefaults.outlinedCardColors(containerColor = Color(0xFF0D263B))
    ) {
        Row(modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(34.dp).background(Color(0xFF174E70), RoundedCornerShape(10.dp)), contentAlignment = Alignment.Center) {
                Icon(Icons.Default.Download, "Prototype release", tint = SharkCyan, modifier = Modifier.size(19.dp))
            }
            Spacer(Modifier.width(11.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text("Prototype update 0.0.0.2", color = SharkText, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                Text("Open the latest preview release", color = SharkMuted, fontSize = 11.sp)
            }
            Text("OPEN", color = SharkCyan, fontWeight = FontWeight.Bold, fontSize = 11.sp)
        }
    }
}

@Composable
private fun SectionPlaceholder(section: Int, onWiki: () -> Unit, onDiscord: () -> Unit, onOpenDirectory: () -> Unit) {
    val title = when (section) { 1 -> "Instances"; 2 -> "Mods & JARs"; 3 -> "Controls"; else -> "Settings" }
    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(title, color = SharkText, fontSize = 28.sp, fontWeight = FontWeight.Bold)
        Text("This panel keeps your launcher workflow focused and uncluttered.", color = SharkMuted)
        QuickAction("Open game directory", "Browse files", Icons.Default.Folder, Modifier.fillMaxWidth(), onOpenDirectory)
        QuickAction("Wiki & news", "Updates and guides", Icons.Default.Language, Modifier.fillMaxWidth(), onWiki)
        QuickAction("Discord community", "Get support", Icons.Default.Description, Modifier.fillMaxWidth(), onDiscord)
    }
}

@Composable
private fun RailItem(item: NavItem, selected: Boolean, onClick: () -> Unit) {
    val tint = if (selected) SharkCyan else SharkMuted
    Column(
        modifier = Modifier.width(70.dp).clickable(onClick = onClick).padding(vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier.size(42.dp).background(if (selected) Color(0xFF173B55) else Color.Transparent, RoundedCornerShape(13.dp)),
            contentAlignment = Alignment.Center
        ) { Icon(item.icon, item.label, tint = tint, modifier = Modifier.size(21.dp)) }
        Spacer(Modifier.height(3.dp))
        Text(item.label, color = tint, fontSize = 9.sp, maxLines = 1)
    }
}

@Composable
private fun StatusPill(text: String, color: Color) {
    Row(modifier = Modifier.border(1.dp, color.copy(alpha = .45f), RoundedCornerShape(50)).padding(horizontal = 10.dp, vertical = 6.dp), verticalAlignment = Alignment.CenterVertically) {
        Box(Modifier.size(7.dp).background(color, RoundedCornerShape(50)))
        Spacer(Modifier.width(6.dp))
        Text(text, color = color, fontSize = 10.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
    }
}

@Composable
private fun FixedIcon(icon: ImageVector, description: String, iconSize: androidx.compose.ui.unit.Dp = 20.dp) {
    Icon(icon, contentDescription = description, modifier = Modifier.size(iconSize))
}

private data class NavItem(val label: String, val icon: ImageVector)

@Composable
fun InformationDialog(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("About Shark Launcher") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text("Shark Launcher 0.0.0.2", fontWeight = FontWeight.SemiBold)
                Text("A PC-inspired Compose dashboard for Minecraft Java on Android.")
                Text("This prototype is based on the PojavLauncher codebase and remains under active development.")
            }
        },
        confirmButton = { Button(onClick = onDismiss) { Text("CLOSE") } }
    )
}
