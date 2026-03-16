package com.github.nathanaelkane.focusedfiles.services

import com.intellij.openapi.components.Service
import com.intellij.openapi.project.Project
import com.intellij.openapi.vcs.changes.ChangeListManager
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.openapi.vfs.VirtualFile
import git4idea.commands.Git
import git4idea.commands.GitCommand
import git4idea.commands.GitLineHandler
import git4idea.repo.GitRepository
import git4idea.repo.GitRepositoryManager

@Service(Service.Level.PROJECT)
class GitChangedFilesService(private val project: Project) {

    fun getModifiedFiles(): List<VirtualFile> {
        val clm = ChangeListManager.getInstance(project)
        val changed = clm.allChanges.mapNotNull { it.virtualFile }
        val untracked = clm.unversionedFilesPaths.mapNotNull { it.virtualFile }
        return (changed + untracked).distinctBy { it.path }.sortedBy { it.name }
    }

    fun getBranchFiles(): List<VirtualFile> =
        GitRepositoryManager.getInstance(project).repositories
            .flatMap { repo -> branchFilesForRepo(repo) }
            .sortedBy { it.name }

    fun getBranchAndModifiedFiles(): List<VirtualFile> =
        (getBranchFiles() + getModifiedFiles()).distinctBy { it.path }.sortedBy { it.name }

    private fun branchFilesForRepo(repo: GitRepository): List<VirtualFile> {
        val baseCommit = mergeBase(repo) ?: return emptyList()
        val handler = GitLineHandler(project, repo.root, GitCommand.DIFF)
        handler.addParameters("--name-only", baseCommit, "HEAD")
        val result = Git.getInstance().runCommand(handler)
        if (!result.success()) return emptyList()
        return result.output
            .filter { it.isNotBlank() }
            .mapNotNull { relativePath ->
                LocalFileSystem.getInstance().findFileByPath("${repo.root.path}/$relativePath")
            }
    }

    private fun mergeBase(repo: GitRepository): String? {
        val handler = GitLineHandler(project, repo.root, GitCommand.MERGE_BASE)
        handler.addParameters("origin/HEAD", "HEAD")
        val result = Git.getInstance().runCommand(handler)
        return if (result.success()) result.output.firstOrNull()?.trim() else null
    }
}
