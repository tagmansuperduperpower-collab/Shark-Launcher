package net.sharklauncher.ui

import net.sharklauncher.core.EngineState

/** Pure state transition rules; side effects stay in ViewModels/use cases. */
fun reduceSharkState(state: SharkAppState, intent: SharkIntent): SharkAppState = when (intent) {
    is SharkIntent.Navigate -> state.copy(screen = intent.screen, message = null)
    is SharkIntent.SelectInstance -> state.copy(selectedInstanceId = intent.instanceId, message = null)
    SharkIntent.ClearMessage -> state.copy(message = null)
    SharkIntent.Launch -> if (state.selectedInstanceId == null) {
        state.copy(message = "Select an instance first")
    } else {
        state.copy(engine = EngineState.Preparing(state.selectedInstanceId))
    }
    SharkIntent.Stop -> state.copy(engine = EngineState.Idle)
}
