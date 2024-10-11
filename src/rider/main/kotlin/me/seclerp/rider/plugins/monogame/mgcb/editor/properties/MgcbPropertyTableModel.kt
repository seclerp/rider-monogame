package me.seclerp.rider.plugins.monogame.mgcb.editor.properties

import com.intellij.util.ui.ListTableModel

class MgcbPropertyTableModel : ListTableModel<MgcbProperty>(
    MgcbPropertyKeyColumn(),
    MgcbPropertyValueColumn()
)