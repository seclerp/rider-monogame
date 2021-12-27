package me.seclerp.rider.plugins.monogame.mgcb.previewer.properties

import com.intellij.util.ui.ColumnInfo

class ValueColumn : ColumnInfo<Pair<String, String>, String>("Value") {
    override fun valueOf(item: Pair<String, String>?): String? {
        return item?.second
    }
}