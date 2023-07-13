package me.seclerp.rider.plugins.monogame.mgcb.toolset

import org.jetbrains.annotations.NonNls

class DotNetToolsVersion private constructor(private val version: String) : Comparable<DotNetToolsVersion?> {
    companion object {
        @NonNls
        val SEMVER_REGEX =
            Regex("(\\d+)\\.(\\d+)\\.(\\d+)(?:-([\\dA-Za-z-]+(?:\\.[\\dA-Za-z-]+)*))?(?:\\+[\\dA-Za-z-]+)?")

        fun parse(version: String): DotNetToolsVersion? {
            val match = SEMVER_REGEX.find(version) ?: return null

            if (match.groups.size < 3) {
                return null
            }

            return DotNetToolsVersion(version)
        }
    }

    override operator fun compareTo(that: DotNetToolsVersion?): Int {
        if (that == null) return 1
        val thisParts = version.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val thatParts = version.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val length = Math.max(thisParts.size, thatParts.size)
        for (i in 0 until length) {
            val thisPart = if (i < thisParts.size) thisParts[i].toInt() else 0
            val thatPart = if (i < thatParts.size) thatParts[i].toInt() else 0
            if (thisPart < thatPart) return -1
            if (thisPart > thatPart) return 1
        }
        return 0
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null) return false
        return if (this.javaClass != other.javaClass) false else this.compareTo(other as DotNetToolsVersion) == 0
    }

    override fun toString() = version
    override fun hashCode() = version.hashCode()
}