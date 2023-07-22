package me.seclerp.rider.plugins.monogame.mgcb.previewer.properties

class MgcbPropertyValueColumn : MgcbPropertyColumn("Value") {
    override fun valueOf(item: MgcbProperty?): String? {
        return item?.value
    }
}