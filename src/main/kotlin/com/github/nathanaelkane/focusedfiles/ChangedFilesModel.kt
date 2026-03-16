package com.github.nathanaelkane.focusedfiles

import com.intellij.ide.util.gotoByName.SimpleChooseByNameModel
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.ui.ColoredListCellRenderer
import com.intellij.ui.SimpleTextAttributes
import javax.swing.JList
import javax.swing.ListCellRenderer

class ChangedFilesModel(
    private val project: Project,
    private val files: List<VirtualFile>,
    promptText: String,
) : SimpleChooseByNameModel(project, promptText, "No changed files found") {

    override fun getNames(): Array<String> = files.map { it.name }.toTypedArray()

    override fun getElementsByName(name: String, pattern: String): Array<Any> =
        files.filter { it.name == name }.toTypedArray()

    override fun getElementName(element: Any): String =
        (element as? VirtualFile)?.name ?: ""

    override fun willOpenEditor(): Boolean = true

    override fun useMiddleMatching(): Boolean = true

    override fun getFullName(element: Any): String =
        (element as? VirtualFile)?.path ?: super.getFullName(element) ?: ""

    override fun getListCellRenderer(): ListCellRenderer<*> =
        object : ColoredListCellRenderer<Any>() {
            override fun customizeCellRenderer(
                list: JList<out Any>,
                value: Any?,
                index: Int,
                selected: Boolean,
                hasFocus: Boolean,
            ) {
                val file = value as? VirtualFile ?: return
                icon = file.fileType.icon
                append(file.name)

                val basePath = project.basePath
                val parentPath = file.parent?.path ?: ""
                val displayPath = if (basePath != null && parentPath.startsWith(basePath)) {
                    parentPath.removePrefix(basePath).trimStart('/')
                } else {
                    parentPath
                }
                if (displayPath.isNotEmpty()) {
                    append("  $displayPath", SimpleTextAttributes.GRAYED_ATTRIBUTES)
                }
            }
        }
}
