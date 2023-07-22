package me.seclerp.rider.plugins.monogame.mgcb.previewer.properties

import com.intellij.util.ui.ColumnInfo

abstract class MgcbPropertyColumn(name: String) : ColumnInfo<MgcbProperty, String>(name) {
    private val renderer by lazy { MgcbPropertyRenderer() }
    override fun getRenderer(item: MgcbProperty?) = renderer
}