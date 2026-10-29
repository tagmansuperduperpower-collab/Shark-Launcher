package net.sharklauncher.core

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

/** Shark-owned engine boundary. UI and domain code depend only on these contracts. */
interface MinecraftEngine {
    val state: StateFlow<EngineState>
    suspend fun launch(request: LaunchRequest): Result<Unit>
    suspend fun stop()
}

interface InstanceRepository {
    fun observeInstances(): Flow<List<SharkInstance>>
    suspend fun select(instanceId: String): Result<SharkInstance>
}

interface AccountGateway {
    fun observeAccounts(): Flow<List<SharkAccount>>
    suspend fun signOut(accountId: String): Result<Unit>
}

interface ContentInstaller {
    val state: StateFlow<InstallState>
    suspend fun install(request: InstallRequest): Result<Unit>
    suspend fun cancel()
}

data class LaunchRequest(
    val instanceId: String,
    val accountId: String,
    val width: Int? = null,
    val height: Int? = null
)

data class InstallRequest(
    val source: String,
    val kind: InstallKind
)

enum class InstallKind { MOD, MODPACK, RUNTIME, VERSION }

data class SharkInstance(
    val id: String,
    val name: String,
    val version: String,
    val lastPlayedEpochMs: Long? = null
)

data class SharkAccount(
    val id: String,
    val displayName: String,
    val isDemo: Boolean = false
)

sealed interface EngineState {
    data object Idle : EngineState
    data class Preparing(val instanceId: String) : EngineState
    data class Running(val instanceId: String) : EngineState
    data class Failed(val message: String, val cause: Throwable? = null) : EngineState
}

sealed interface InstallState {
    data object Idle : InstallState
    data class Running(val progress: Float, val label: String) : InstallState
    data object Completed : InstallState
    data class Failed(val message: String) : InstallState
}

/** Composition root for the Shark product layer. */
class SharkCore(
    val engine: MinecraftEngine,
    val instances: InstanceRepository,
    val accounts: AccountGateway,
    val installer: ContentInstaller
)
