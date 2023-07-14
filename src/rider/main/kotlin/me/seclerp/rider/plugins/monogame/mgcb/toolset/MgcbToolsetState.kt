package me.seclerp.rider.plugins.monogame.mgcb.toolset

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.openapi.components.service
import com.intellij.util.xmlb.XmlSerializerUtil

@State(
    name = "me.seclerp.rider.plugins.monogame.mgcb.toolset.MgcbToolsetState",
    storages = [Storage("MgcbToolsetState.xml")]
)
class MgcbToolsetState : PersistentStateComponent<MgcbToolsetState> {
    companion object {
        fun getInstance() = service<MgcbToolsetState>()
    }
    override fun getState(): MgcbToolsetState {
        return this
    }

    override fun loadState(state: MgcbToolsetState) {
        XmlSerializerUtil.copyBean(state, this)
    }
}