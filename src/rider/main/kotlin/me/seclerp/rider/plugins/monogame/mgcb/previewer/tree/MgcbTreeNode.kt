package me.seclerp.rider.plugins.monogame.mgcb.previewer.tree

import javax.swing.Icon
import javax.swing.tree.DefaultMutableTreeNode

abstract class MgcbTreeNode(name: String, val icon: Icon) : DefaultMutableTreeNode(name)