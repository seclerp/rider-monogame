package me.seclerp.rider.plugins.monogame.mgcb.state

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage

class MgcbEditorGlobalState {
    var installNotificationClosed: Boolean = false
}

@Service
@State(name = "EfCoreCommonOptions", storages = [Storage("MgcbEditorGlobalState.xml")])
class MgcbEditorGlobalStateService : PersistentStateComponent<MgcbEditorGlobalState> {
    private var myState = MgcbEditorGlobalState()

    override fun getState(): MgcbEditorGlobalState = myState

    override fun loadState(state: MgcbEditorGlobalState) {
        myState = state
    }
}