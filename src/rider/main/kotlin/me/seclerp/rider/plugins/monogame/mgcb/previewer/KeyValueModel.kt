package me.seclerp.rider.plugins.monogame.mgcb.previewer

import com.intellij.util.ui.ListTableModel

class KeyValueModel(items: MutableList<Pair<String, String>>) : ListTableModel<Pair<String, String>>(
    KeyColumn(),
    ValueColumn()
) {
    init {
        addRows(items)
    }
}