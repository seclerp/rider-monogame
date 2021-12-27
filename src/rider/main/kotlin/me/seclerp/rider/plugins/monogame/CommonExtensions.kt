package me.seclerp.rider.plugins.monogame

import com.intellij.util.ui.ListTableModel

fun String.substringAfter(regex: Regex, missingDelimiterValue: String = this): String {
    val firstMatch = regex.find(this) ?: return missingDelimiterValue

    return substring(firstMatch.range.first + 1)
}

fun String.substringBefore(regex: Regex, missingDelimiterValue: String = this): String {
    val firstMatch = regex.find(this) ?: return missingDelimiterValue

    return substring(0, firstMatch.range.last)
}

fun String.substringBeforeLast(regex: Regex, missingDelimiterValue: String = this): String {
    val matches = regex.findAll(this)

    if (!matches.any()) {
        return missingDelimiterValue
    }

    return substring(0, matches.last().range.last)
}

fun String.substringAfterLast(regex: Regex, missingDelimiterValue: String = this): String {
    val matches = regex.findAll(this)

    if (!matches.any()) {
        return missingDelimiterValue
    }

    return substring(matches.last().range.last + 1)
}

fun <T> ListTableModel<T>.removeAllRows() {
    val cachedRowCount = rowCount
    for (row in 0 until cachedRowCount)
        removeRow(0)
}