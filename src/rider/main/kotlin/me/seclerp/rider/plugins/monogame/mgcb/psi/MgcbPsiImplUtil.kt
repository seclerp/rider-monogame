package me.seclerp.rider.plugins.monogame.mgcb.psi

fun MgcbOption.getKey(): String? {
    val keyNode = node.findChildByType(MgcbTypes.OPTION_KEY)
    return keyNode?.text
}

fun MgcbOption.getValue(): String? {
    val valueNode = node.findChildByType(MgcbTypes.OPTION_VALUE)
    return valueNode?.text
}
