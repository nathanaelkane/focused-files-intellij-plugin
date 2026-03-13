package com.github.nathanaelkane.gitchangedfilesintellijplugin.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.components.service
import com.github.nathanaelkane.gitchangedfilesintellijplugin.services.GitChangedFilesService

class ShowBranchAndModifiedFilesAction : AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        val files = project.service<GitChangedFilesService>().getBranchAndModifiedFiles()
        showChangedFilesPopup(project, files, promptText = "Branch or modified file:")
    }
}
