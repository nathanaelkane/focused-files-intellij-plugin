package com.github.nathanaelkane.gitchangedfilesintellijplugin.actions

import com.intellij.ide.util.gotoByName.ChooseByNamePopup
import com.intellij.ide.util.gotoByName.ChooseByNamePopupComponent
import com.intellij.openapi.application.ModalityState
import com.intellij.openapi.fileEditor.OpenFileDescriptor
import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.progress.Task
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.github.nathanaelkane.gitchangedfilesintellijplugin.ChangedFilesModel

fun showChangedFilesPopup(project: Project, promptText: String, fileSupplier: () -> List<VirtualFile>) {
    var files: List<VirtualFile> = emptyList()

    ProgressManager.getInstance().run(object : Task.Backgroundable(project, "Collecting changed files", false) {
        override fun run(indicator: ProgressIndicator) {
            files = fileSupplier()
        }

        override fun onSuccess() {
            val model = ChangedFilesModel(project, files, promptText)

            val popup = ChooseByNamePopup.createPopup(project, model, null as com.intellij.psi.PsiElement?)

            popup.setShowListForEmptyPattern(true)

            popup.invoke(object : ChooseByNamePopupComponent.Callback() {
                override fun elementChosen(element: Any) {
                    val file = element as? VirtualFile ?: return
                    OpenFileDescriptor(project, file).navigate(true)
                }
            }, ModalityState.current(), false)
        }
    })
}
