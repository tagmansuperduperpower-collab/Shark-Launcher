package net.kdt.pojavlaunch.ui

import androidx.compose.animation.AnimatedContent
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
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.kdt.mcgui.mcVersionSpinner
import net.kdt.pojavlaunch.extra.ExtraConstants
import net.kdt.pojavlaunch.extra.ExtraCore

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SharkLauncherScreen(
    onSettings: () -> Unit,
    onInformation: () -> Unit,
    onInstallJar: (Boolean) -> Unit,
    onOpenDirectory: () -> Unit,
    onShareLogs: () -> Unit,
    onCustomControls: () -> Unit,
    onWiki: () -> Unit,
    onDiscord: () -> Unit,
    onEditProfile: (mcVersionSpinner) -> Unit,
    onPrototypeRelease: () -> Unit
) {
    var selected by remember { mutableStateOf(SharkSection.DASHBOARD) }
    var showInformation by remember { mutableStateOf(false) }
    var spinner by remember { mutableStateOf<mcVersionSpinner?>(null) }
    var playPressed by remember { mutableStateOf(false) }
    val playScale by animateFloatAsState(if (playPressed) .96f else 1f, spring(stiffness = 700f), label = "play-scale")
    val navItems = listOf(
        SharkNavItem(SharkSection.DASHBOARD, Icons.Default.SportsEsports),
        SharkNavItem(SharkSection.INSTANCES, Icons.Default.Storage),
        SharkNavItem(SharkSection.MODS, Icons.Default.Code),
        SharkNavItem(SharkSection.CONTROLS, Icons.Default.Gamepad),
        SharkNavItem(SharkSection.SETTINGS, Icons.Default.Settings)
    )

    SharkTheme {
        Surface(Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
            BoxWithConstraints(Modifier.fillMaxSize()) {
                val wide = maxWidth >= 600.dp
                if (wide) {
                    Row(Modifier.fillMaxSize()) {
                        SharkRail(navItems, selected) { item ->
                            selected = item.section
                            dispatchSection(item.section, onInstallJar, onCustomControls, onSettings)
                        }
                        SharkScaffold(
                            selected, navItems, wide, playScale, spinner, { spinner = it },
                            { playPressed = true; ExtraCore.setValue(ExtraConstants.LAUNCH_GAME, true) },
                            onSettings, { showInformation = true }, onEditProfile, onOpenDirectory, onShareLogs,
                            onCustomControls, onWiki, onDiscord, onPrototypeRelease
                        )
                    }
                } else {
                    SharkScaffold(
                        selected, navItems, wide, playScale, spinner, { spinner = it },
                        { playPressed = true; ExtraCore.setValue(ExtraConstants.LAUNCH_GAME, true) },
                        onSettings, { showInformation = true }, onEditProfile, onOpenDirectory, onShareLogs,
                        onCustomControls, onWiki, onDiscord, onPrototypeRelease,
                        onSelectSection = { item ->
                            selected = item.section
                            dispatchSection(item.section, onInstallJar, onCustomControls, onSettings)
                        }
                    )
                }
            }
        }
    }
    if (showInformation) SharkInformationDialog(onInformation = { showInformation = false })
}

private fun dispatchSection(section: SharkSection, onInstallJar: (Boolean) -> Unit, onCustomControls: () -> Unit, onSettings: () -> Unit) {
    when (section) {
        SharkSection.MODS -> onInstallJar(false)
        SharkSection.CONTROLS -> onCustomControls()
        SharkSection.SETTINGS -> onSettings()
        else -> Unit
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SharkScaffold(
    selected: SharkSection,
    navItems: List<SharkNavItem>,
    wide: Boolean,
    playScale: Float,
    spinner: mcVersionSpinner?,
    onSpinner: (mcVersionSpinner) -> Unit,
    onPlay: () -> Unit,
    onSettings: () -> Unit,
    onInformation: () -> Unit,
    onEditProfile: (mcVersionSpinner) -> Unit,
    onOpenDirectory: () -> Unit,
    onShareLogs: () -> Unit,
    onCustomControls: () -> Unit,
    onWiki: () -> Unit,
    onDiscord: () -> Unit,
    onPrototypeRelease: () -> Unit,
    onSelectSection: (SharkNavItem) -> Unit = {}
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("SHARK LAUNCHER", style = MaterialTheme.typography.titleLarge, fontWeight = androidx.compose.ui.text.font.FontWeight.Black, letterSpacing = 2.sp)
                        Text("0.0.0.3 prototype · PC-inspired", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                },
                actions = {
                    IconButton(onClick = onInformation) { FixedIcon(Icons.Default.Info, "Information") }
                    IconButton(onClick = onSettings) { FixedIcon(Icons.Default.MoreVert, "Settings") }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
            )
        },
        bottomBar = {
            if (!wide) {
                NavigationBar(containerColor = MaterialTheme.colorScheme.surface) {
                    navItems.take(4).forEach { item ->
                        NavigationBarItem(selected = selected == item.section, onClick = { onSelectSection(item) }, icon = { FixedIcon(item.icon, item.section.title) }, label = { Text(item.section.title, maxLines = 1) })
                    }
                }
            }
        }
    ) { padding ->
        val metrics = rememberSharkMetrics(wide)
        Column(
            Modifier.fillMaxSize().padding(padding).verticalScroll(rememberScrollState()).padding(metrics.screenPadding),
            verticalArrangement = Arrangement.spacedBy(metrics.sectionGap)
        ) {
            AnimatedContent(targetState = selected, label = "section-transition") { section ->
                when (section) {
                    SharkSection.DASHBOARD -> DashboardContent(metrics, playScale, spinner, onSpinner, onPlay, onEditProfile, onOpenDirectory, onShareLogs, onCustomControls, onInstallJar = { onCustomControls() }, onWiki, onDiscord, onPrototypeRelease)
                    else -> SectionContent(section, onOpenDirectory, onWiki, onDiscord)
                }
            }
        }
    }
}

@Composable
private fun SharkRail(items: List<SharkNavItem>, selected: SharkSection, onSelect: (SharkNavItem) -> Unit) {
    NavigationRail(containerColor = MaterialTheme.colorScheme.surface, header = {
        Box(Modifier.padding(vertical = 18.dp).size(42.dp).background(MaterialTheme.colorScheme.primary, MaterialTheme.shapes.medium), contentAlignment = Alignment.Center) { Text("S", color = MaterialTheme.colorScheme.onPrimary, fontSize = 23.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.Black) }
    }) {
        items.forEach { item ->
            NavigationRailItem(selected = selected == item.section, onClick = { onSelect(item) }, icon = { FixedIcon(item.icon, item.section.title) }, label = { Text(item.section.title, maxLines = 1) })
        }
    }
}

@Composable
private fun ColumnScope.DashboardContent(
    metrics: SharkUiMetrics,
    playScale: Float, spinner: mcVersionSpinner?, onSpinner: (mcVersionSpinner) -> Unit, onPlay: () -> Unit,
    onEditProfile: (mcVersionSpinner) -> Unit, onOpenDirectory: () -> Unit, onShareLogs: () -> Unit,
    onCustomControls: () -> Unit, onInstallJar: () -> Unit, onWiki: () -> Unit, onDiscord: () -> Unit, onPrototypeRelease: () -> Unit
) {
    Row(verticalAlignment = Alignment.Bottom) {
        Column(Modifier.weight(1f)) { Text("Welcome back, player", color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.bodyMedium); Text("Ready to launch?", style = MaterialTheme.typography.headlineMedium) }
        StatusPill("ONLINE")
    }
    PlayPanel(metrics, spinner, onSpinner, onEditProfile, onPlay, playScale)
    Text("QUICK ACCESS", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        QuickAction("Mods", "Install", Icons.Default.Code, Modifier.weight(1f), onInstallJar, metrics)
        QuickAction("Folder", "Browse", Icons.Default.Folder, Modifier.weight(1f), onOpenDirectory, metrics)
        QuickAction("Controls", "Configure", Icons.Default.Gamepad, Modifier.weight(1f), onCustomControls, metrics)
        QuickAction("Logs", "Share", Icons.Default.Share, Modifier.weight(1f), onShareLogs, metrics)
    }
    PrototypeBanner(onPrototypeRelease)
    OutlinedCard(onClick = onWiki, Modifier.fillMaxWidth()) { Row(Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) { FixedIcon(Icons.Default.Language, "Wiki", 22.dp); Spacer(Modifier.width(12.dp)); Column { Text("Zalith-inspired workspace", fontWeight = androidx.compose.ui.text.font.FontWeight.Bold); Text("Organized screens, responsive layout, and Material 3 motion", color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.bodySmall) } } }
}

@Composable
private fun PlayPanel(metrics: SharkUiMetrics, spinner: mcVersionSpinner?, onSpinner: (mcVersionSpinner) -> Unit, onEditProfile: (mcVersionSpinner) -> Unit, onPlay: () -> Unit, scale: Float) {
    Card(Modifier.fillMaxWidth().scale(scale), shape = MaterialTheme.shapes.large, border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerHigh)) {
        Row(Modifier.fillMaxWidth().padding(metrics.cardPadding), verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(9.dp)) {
                Text("SELECTED INSTANCE", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary)
                Text("Minecraft Java Edition", style = MaterialTheme.typography.titleLarge)
                Text("Select a profile and start your world", color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.bodySmall)
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    AndroidView(factory = { ctx -> mcVersionSpinner(ctx).also(onSpinner) }, modifier = Modifier.width(205.dp).height(52.dp), update = { it.reloadProfiles() })
                    OutlinedButton(onClick = { spinner?.let(onEditProfile) }, modifier = Modifier.height(48.dp)) { Text("EDIT") }
                }
            }
            Spacer(Modifier.width(12.dp))
            Button(onClick = onPlay, Modifier.width(metrics.playWidth).height(metrics.playHeight), shape = MaterialTheme.shapes.large, colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)) { Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(6.dp)) { FixedIcon(Icons.Default.PlayArrow, "Play", 30.dp); Text("PLAY NOW", fontWeight = androidx.compose.ui.text.font.FontWeight.Black) } }
        }
    }
}

@Composable
private fun QuickAction(title: String, subtitle: String, icon: ImageVector, modifier: Modifier, onClick: () -> Unit, metrics: SharkUiMetrics = CompactSharkMetrics) {
    Card(modifier.height(metrics.actionHeight).clickable(onClick = onClick), shape = MaterialTheme.shapes.medium, border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow)) {
        Row(Modifier.fillMaxSize().padding(10.dp), verticalAlignment = Alignment.CenterVertically) { Box(Modifier.size(metrics.iconBox).background(MaterialTheme.colorScheme.secondaryContainer, MaterialTheme.shapes.small), contentAlignment = Alignment.Center) { FixedIcon(icon, title, 20.dp) }; Spacer(Modifier.width(8.dp)); Column { Text(title, style = MaterialTheme.typography.labelLarge); Text(subtitle, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant) } }
    }
}

@Composable
private fun PrototypeBanner(onClick: () -> Unit) { OutlinedCard(onClick = onClick, Modifier.fillMaxWidth(), border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)) { Row(Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) { Box(Modifier.size(34.dp).background(MaterialTheme.colorScheme.primaryContainer, MaterialTheme.shapes.small), contentAlignment = Alignment.Center) { FixedIcon(Icons.Default.Download, "Release", 19.dp) }; Spacer(Modifier.width(10.dp)); Column(Modifier.weight(1f)) { Text("Prototype update 0.0.0.3", fontWeight = androidx.compose.ui.text.font.FontWeight.Bold); Text("New architecture and motion system", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant) }; Text("OPEN", color = MaterialTheme.colorScheme.primary, style = MaterialTheme.typography.labelSmall) } } }

@Composable
private fun SectionContent(section: SharkSection, onOpenDirectory: () -> Unit, onWiki: () -> Unit, onDiscord: () -> Unit) { Column(verticalArrangement = Arrangement.spacedBy(12.dp)) { Text(section.title, style = MaterialTheme.typography.headlineMedium); Text("This workspace is ready for the next Compose migration step.", color = MaterialTheme.colorScheme.onSurfaceVariant); QuickAction("Game directory", "Browse files", Icons.Default.Folder, Modifier.fillMaxWidth(), onOpenDirectory); QuickAction("Wiki & news", "Updates and guides", Icons.Default.Language, Modifier.fillMaxWidth(), onWiki); QuickAction("Discord community", "Support", Icons.Default.Description, Modifier.fillMaxWidth(), onDiscord) } }

@Composable
private fun StatusPill(text: String) { Row(Modifier.border(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = .5f), CircleShape).padding(horizontal = 10.dp, vertical = 6.dp), verticalAlignment = Alignment.CenterVertically) { Box(Modifier.size(7.dp).background(MaterialTheme.colorScheme.primary, CircleShape)); Spacer(Modifier.width(6.dp)); Text(text, color = MaterialTheme.colorScheme.primary, style = MaterialTheme.typography.labelSmall) } }

@Composable
private fun FixedIcon(icon: ImageVector, description: String, size: Dp = 20.dp) { Icon(icon, description, Modifier.size(size)) }

@Composable
private fun SharkInformationDialog(onInformation: () -> Unit) { AlertDialog(onDismissRequest = onInformation, title = { Text("Shark Launcher 0.0.0.3") }, text = { Text("Half update 0.0.0.3: the launcher dashboard is now fully modern Jetpack Compose Material 3 with responsive navigation, stable icon sizing, safe scrolling, and animated transitions. Unmigrated operational XML is retained temporarily while each screen is replaced and tested.") }, confirmButton = { Button(onClick = onInformation) { Text("CLOSE") } }) }
