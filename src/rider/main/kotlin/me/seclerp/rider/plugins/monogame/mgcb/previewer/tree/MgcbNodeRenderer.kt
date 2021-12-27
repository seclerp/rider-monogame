package me.seclerp.rider.plugins.monogame.mgcb.previewer.tree

import com.intellij.ide.util.treeView.NodeRenderer
import javax.swing.JTree

class MgcbNodeRenderer : NodeRenderer() {
    override fun customizeCellRenderer(
        tree: JTree,
        value: Any?,
        selected: Boolean,
        expanded: Boolean,
        leaf: Boolean,
        row: Int,
        hasFocus: Boolean
    ) {
        super.customizeCellRenderer(tree, value, selected, expanded, leaf, row, hasFocus)

        if (value is MgcbTreeNode) {
            icon = fixIconIfNeeded(value.icon, selected, hasFocus)
        }
    }
}