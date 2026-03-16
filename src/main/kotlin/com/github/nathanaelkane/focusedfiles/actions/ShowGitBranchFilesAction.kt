package com.github.nathanaelkane.focusedfiles.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.components.service
import com.github.nathanaelkane.focusedfiles.services.GitChangedFilesService

class ShowGitBranchFilesAction : AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        showChangedFilesPopup(project, promptText = "Branch file:") {
            project.service<GitChangedFilesService>().getBranchFiles()
        }
    }
}
