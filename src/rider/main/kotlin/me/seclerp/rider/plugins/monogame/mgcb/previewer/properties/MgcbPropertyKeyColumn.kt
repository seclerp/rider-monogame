package me.seclerp.rider.plugins.monogame.mgcb.previewer.properties

import com.intellij.util.ui.ColumnInfo

class MgcbPropertyKeyColumn : MgcbPropertyColumn("Key") {
    override fun valueOf(item: MgcbProperty?): String? {
        return item?.key
    }
}

