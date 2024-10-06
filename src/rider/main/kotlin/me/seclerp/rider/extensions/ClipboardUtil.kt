package me.seclerp.rider.extensions

import java.awt.Toolkit
import java.awt.datatransfer.StringSelection

internal object ClipboardUtil {
    fun copyToClipboard(text: String) {
        val selection = StringSelection(text)
        val clipboard = Toolkit.getDefaultToolkit().systemClipboard
        clipboard.setContents(selection, null)
    }
}