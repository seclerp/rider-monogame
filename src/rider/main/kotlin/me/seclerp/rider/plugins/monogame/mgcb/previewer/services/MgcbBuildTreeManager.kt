package me.seclerp.rider.plugins.monogame.mgcb.previewer.services

import com.intellij.openapi.components.Service
import com.intellij.openapi.util.IconLoader
import me.seclerp.rider.plugins.monogame.mgcb.previewer.BuildEntry
import me.seclerp.rider.plugins.monogame.mgcb.previewer.MgcbModel
import me.seclerp.rider.plugins.monogame.mgcb.previewer.tree.*
import me.seclerp.rider.plugins.monogame.substringAfterLast
import me.seclerp.rider.plugins.monogame.substringBeforeLast

@Service
class MgcbBuildTreeManager {
    fun createEmpty(): MgcbTree {
        val tree = MgcbTree()
        tree.cellRenderer = MgcbNodeRenderer()
        tree.isRootVisible = true

        return tree
    }

    fun updateTree(model: MgcbModel, tree: MgcbTree) {
        val buildPaths = model.buildEntries

        // 1. Get all parent folders per each path
        val parentsPerPath = buildPaths.map { getParentsHierarchy(it) }

        // 2. Merge + distinct
        val flatten = parentsPerPath.flatten().distinctBy { it.first }

        // 3. Get full parent paths
        val pathsWithParents = flatten.map { Triple(getParentPath(it.first), it.first, it.second) }

        // 4. Add each item to the corresponding parent in dict
        val nodeCache = buildNodeCache(pathsWithParents)

        // 5. Process every tree node and create corresponding tree
        val topLevelNodes = pathsWithParents
            .filter { it.first == "" }
            .map { createNodeFrom(it.second, nodeCache) }

        val currentSelection = tree.getSelectedUrl()
        val currentExpanded = tree.getExpandedUrls()

        tree.clear()

        for (node in topLevelNodes) {
            tree.root.add(node)
        }

        tree.reload()
        tree.restoreExpandedUrl(currentExpanded)

        if (currentSelection != null) {
            tree.restoreSelectedUrl(currentSelection)
        }
    }

    // Will transform
    // a/b/c
    //
    // To
    // a
    // a/b
    // a/b/c
    private fun getParentsHierarchy(entry: BuildEntry) : List<Pair<String, BuildEntry>> {
        val normalizedPath = entry.contentFilepath!!.trim { it.toString().matches(delimiterRegex) }
        val pathParts = normalizedPath.split(delimiterRegex).toTypedArray()

        return mutableListOf<Pair<String, BuildEntry>>().apply {
            val aggregatedPath: StringBuilder = StringBuilder(normalizedPath.length)
            for (i in 0 until pathParts.count()) {
                val part = pathParts[i]
                if (i > 0) {
                    aggregatedPath.append("/")
                }
                val newParent = aggregatedPath.append(part).toString()
                add(Pair(newParent, entry))
            }
        }
    }

    // Will transform
    // a/b/c
    //
    // To
    //
    // a/b
    private fun getParentPath(path: String): String {
        return path.substringBeforeLast(delimiterRegex, "")
    }

    private fun buildNodeCache(pathsWithParents: List<Triple<String, String, BuildEntry>>): Map<String, Pair<BuildEntry?, List<String>>> {
        val nodeCache = mutableMapOf<String, Pair<BuildEntry?, MutableList<String>>>()
        for ((parent, path, entry) in pathsWithParents) {
            if (!nodeCache.containsKey(parent)) {
                nodeCache[parent] = Pair(null, mutableListOf())
            }

            nodeCache[path] = Pair(entry, mutableListOf())
            nodeCache[parent]!!.second.add(path)
        }

        return nodeCache
    }

    private fun createNodeFrom(path: String, cache: Map<String, Pair<BuildEntry?, List<String>>>): MgcbTreeNode {
        if (cache[path] == null) {
            // TODO
            throw Exception()
        }

        val cachedValue = cache[path]!!
        if (cachedValue.second.isEmpty()) {
            val name = path.substringAfterLast(delimiterRegex)
            val icon = IconLoader.findIcon(name, javaClass)
            return MgcbBuildEntryNode(path.substringAfterLast(delimiterRegex), icon, cachedValue.first!!)
        }

        val children = cachedValue.second.map { createNodeFrom(it, cache) }
        val folderNode = MgcbFolderNode(path.substringAfterLast(delimiterRegex))
        for (child in children) {
            folderNode.add(child)
        }

        return folderNode
    }

    // Nodes to remove - exist in old but not in new
//    private fun getNodesToRemove(oldModel: MgcbModel, newModel: MgcbModel): List<BuildEntry> {
//        val result = mutableListOf<BuildEntry>()
//
//        val oldCount = oldModel.buildEntries.count()
//
//        var oldCounter = 0
//        var newCounter = 0
//
//        while (oldCounter != oldCount) {
//            val oldNode = oldModel.buildEntries[oldCounter]
//            val newNode = newModel.buildEntries.getOrNull(newCounter)
//
//            if (oldNode.contentFilepath == newNode?.contentFilepath) {
//                oldCounter++
//                newCounter++
//            } else {
//                // At that point newNode always != oldNode
//                result.add(oldNode)
//                oldCounter++
//            }
//        }
//
//        return result
//    }
//
//    private fun getNodesToAdd(oldModel: MgcbModel, newModel: MgcbModel): List<Pair<BuildEntry?, BuildEntry>> {
//        val result = mutableListOf<Pair<BuildEntry?, BuildEntry>>()
//
//        val newCount = newModel.buildEntries.count()
//
//        var oldCounter = 0
//        var newCounter = 0
//
//        var previousNewNode: BuildEntry? = null
//
//        while (newCounter != newCount) {
//            val oldNode = oldModel.buildEntries.getOrNull(oldCounter)
//            val newNode = newModel.buildEntries[newCounter]
//
//            if (oldNode?.contentFilepath == newNode.contentFilepath) {
//                oldCounter++
//                newCounter++
//            } else {
//                // At that point newNode always != oldNode
//                result.add(Pair(previousNewNode, newNode))
//                newCounter++
//            }
//
//            previousNewNode = newNode
//        }
//
//        return result
//    }

    companion object {
        private val delimiterRegex = Regex("[/\\\\]")
    }
}