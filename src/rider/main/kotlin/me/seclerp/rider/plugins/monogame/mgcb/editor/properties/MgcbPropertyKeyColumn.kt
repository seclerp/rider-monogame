package me.seclerp.rider.plugins.monogame.mgcb.editor.properties

class MgcbPropertyKeyColumn : MgcbPropertyColumn("Key") {
    override fun valueOf(item: MgcbProperty?): String? {
        return item?.key
    }
}

