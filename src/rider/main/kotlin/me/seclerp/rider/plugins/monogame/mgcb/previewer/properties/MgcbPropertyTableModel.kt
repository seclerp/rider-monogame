package me.seclerp.rider.plugins.monogame.mgcb.previewer.properties

import com.intellij.util.ui.ListTableModel

class MgcbPropertyTableModel : ListTableModel<MgcbProperty>(
    MgcbPropertyKeyColumn(),
    MgcbPropertyValueColumn()
)