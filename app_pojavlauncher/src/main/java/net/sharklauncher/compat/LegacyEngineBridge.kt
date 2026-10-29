package net.sharklauncher.compat

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import net.sharklauncher.core.EngineState
import net.sharklauncher.core.LaunchRequest
import net.sharklauncher.core.MinecraftEngine

/**
 * Temporary boundary for the retained Minecraft engine.
 * The implementation can delegate to the existing launcher engine, while Shark UI
 * never imports legacy Activity, Fragment, XML, or Pojav package classes.
 */
class LegacyEngineBridge(
    private val launchBlock: suspend (LaunchRequest) -> Result<Unit>,
    private val stopBlock: suspend () -> Unit
) : MinecraftEngine {
    private val mutableState = MutableStateFlow<EngineState>(EngineState.Idle)
    override val state: StateFlow<EngineState> = mutableState

    override suspend fun launch(request: LaunchRequest): Result<Unit> {
        mutableState.value = EngineState.Preparing(request.instanceId)
        val result = launchBlock(request)
        mutableState.value = result.fold(
            onSuccess = { EngineState.Running(request.instanceId) },
            onFailure = { EngineState.Failed(it.message ?: "Unable to launch", it) }
        )
        return result
    }

    override suspend fun stop() {
        stopBlock()
        mutableState.value = EngineState.Idle
    }
}
