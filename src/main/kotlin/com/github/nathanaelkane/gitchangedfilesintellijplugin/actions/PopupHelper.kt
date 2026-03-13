package com.github.nathanaelkane.gitchangedfilesintellijplugin.actions

import com.intellij.ide.util.gotoByName.ChooseByNamePopup
import com.intellij.ide.util.gotoByName.ChooseByNamePopupComponent
import com.intellij.openapi.application.ModalityState
import com.intellij.openapi.fileEditor.OpenFileDescriptor
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.github.nathanaelkane.gitchangedfilesintellijplugin.ChangedFilesModel

fun showChangedFilesPopup(project: Project, files: List<VirtualFile>, promptText: String) {
    val model = ChangedFilesModel(project, files, promptText)

    val popup = ChooseByNamePopup.createPopup(project, model, null as com.intellij.psi.PsiElement?)

    popup.invoke(object : ChooseByNamePopupComponent.Callback() {
        override fun elementChosen(element: Any) {
            val file = element as? VirtualFile ?: return
            OpenFileDescriptor(project, file).navigate(true)
        }
    }, ModalityState.current(), false)
}
