package me.seclerp.rider.plugins.monogame

fun String.substringAfter(regex: Regex, missingDelimiterValue: String? = null): String {
    val firstMatch = regex.find(this) ?: return missingDelimiterValue ?: this

    return substring(firstMatch.range.first + 1)
}

fun String.substringBefore(regex: Regex, missingDelimiterValue: String? = null): String {
    val firstMatch = regex.find(this) ?: return missingDelimiterValue ?: this

    return substring(0, firstMatch.range.last)
}