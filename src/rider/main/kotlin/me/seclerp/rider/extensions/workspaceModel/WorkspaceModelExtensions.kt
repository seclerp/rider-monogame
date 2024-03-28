@file:Suppress("UnstableApiUsage")

package me.seclerp.rider.extensions.workspaceModel

import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.platform.backend.workspace.WorkspaceModel
import com.intellij.platform.backend.workspace.virtualFile
import com.jetbrains.rider.projectView.workspace.ProjectModelEntity
import com.jetbrains.rider.projectView.workspace.containingProjectEntity
import com.jetbrains.rider.projectView.workspace.getContentRootUrl
import com.jetbrains.rider.projectView.workspace.getProjectModelEntities

fun WorkspaceModel.containingProjectEntity(file: VirtualFile, project: Project): ProjectModelEntity? {
    return getProjectModelEntities(file, project)
        .firstOrNull()
        ?.containingProjectEntity()
}

fun WorkspaceModel.containingProjectDirectory(file: VirtualFile, project: Project): VirtualFile? {
    return containingProjectEntity(file, project)
        ?.getContentRootUrl(WorkspaceModel.getInstance(project).getVirtualFileUrlManager())
        ?.virtualFile
}