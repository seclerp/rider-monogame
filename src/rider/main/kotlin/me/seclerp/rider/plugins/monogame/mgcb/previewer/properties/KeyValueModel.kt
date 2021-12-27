package me.seclerp.rider.plugins.monogame.mgcb.previewer.properties

import com.intellij.util.ui.ListTableModel

class KeyValueModel() : ListTableModel<Pair<String, String>>(
    KeyColumn(),
    ValueColumn()
)