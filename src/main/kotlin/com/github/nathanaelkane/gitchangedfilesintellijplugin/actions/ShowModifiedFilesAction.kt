package com.github.nathanaelkane.gitchangedfilesintellijplugin.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.components.service
import com.github.nathanaelkane.gitchangedfilesintellijplugin.services.GitChangedFilesService

class ShowModifiedFilesAction : AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        val files = project.service<GitChangedFilesService>().getModifiedFiles()
        showChangedFilesPopup(project, files, promptText = "Modified file:")
    }
}
