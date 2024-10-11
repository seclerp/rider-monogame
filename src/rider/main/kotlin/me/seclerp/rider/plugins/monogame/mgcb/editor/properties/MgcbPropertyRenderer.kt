package me.seclerp.rider.plugins.monogame.mgcb.editor.properties

import com.intellij.util.ui.JBUI
import java.awt.Component
import javax.swing.JTable
import javax.swing.table.DefaultTableCellRenderer

class MgcbPropertyRenderer : DefaultTableCellRenderer() {
    override fun getTableCellRendererComponent(
        table: JTable?,
        value: Any?,
        isSelected: Boolean,
        hasFocus: Boolean,
        row: Int,
        column: Int
    ): Component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column).apply {
        border = JBUI.Borders.empty(JBUI.scale(2), JBUI.scale(8))
    }
}