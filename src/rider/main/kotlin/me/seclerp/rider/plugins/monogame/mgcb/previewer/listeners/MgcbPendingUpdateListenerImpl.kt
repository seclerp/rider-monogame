package me.seclerp.rider.plugins.monogame.mgcb.previewer.listeners

import com.intellij.openapi.application.readAction
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiManager
import com.jetbrains.rider.util.idea.getService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import me.seclerp.rider.plugins.monogame.mgcb.previewer.MgcbPreviewerTopics
import me.seclerp.rider.plugins.monogame.mgcb.previewer.services.MgcbAnalyzer
import me.seclerp.rider.plugins.monogame.mgcb.psi.MgcbFile

class MgcbPendingUpdateListenerImpl(
    private val project: Project,
    private val scope: CoroutineScope
) : MgcbPendingUpdateListener {
    private val publisher = project.messageBus.syncPublisher(MgcbPreviewerTopics.MGCB_PROCESSED_UPDATE_TOPIC)
    private val mgcbAnalyzer = project.getService<MgcbAnalyzer>()

    override fun handle(file: VirtualFile) {
        scope.launch {
            val mgcbFile = readAction { PsiManager.getInstance(project).findFile(file) as MgcbFile }
            val mgcbModel = readAction { mgcbAnalyzer.analyzeFile(mgcbFile) }
            publisher.handle(file, mgcbModel)
        }
    }
}