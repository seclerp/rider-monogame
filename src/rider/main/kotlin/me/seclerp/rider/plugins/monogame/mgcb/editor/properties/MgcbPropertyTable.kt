package me.seclerp.rider.plugins.monogame.mgcb.editor.properties

import com.intellij.ui.table.TableView

class MgcbPropertyTable(model: MgcbPropertyTableModel) : TableView<MgcbProperty>(model) {
    override fun createDefaultTableHeader() = JBTableHeader()
}

