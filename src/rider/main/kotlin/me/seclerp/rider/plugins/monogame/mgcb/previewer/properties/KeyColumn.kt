package me.seclerp.rider.plugins.monogame.mgcb.previewer.properties

import com.intellij.util.ui.ColumnInfo

class KeyColumn : ColumnInfo<Pair<String, String>, String>("Key") {
    override fun valueOf(item: Pair<String, String>?): String? {
        return item?.first
    }
}

