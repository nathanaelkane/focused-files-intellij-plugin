package com.github.nathanaelkane.gitchangedfilesintellijplugin.services

import com.intellij.openapi.application.WriteAction
import com.intellij.openapi.components.service
import com.intellij.openapi.vcs.ProjectLevelVcsManager
import com.intellij.openapi.vcs.VcsDirectoryMapping
import com.intellij.openapi.vcs.changes.ChangeListManager
import com.intellij.openapi.vcs.changes.ChangeListManagerImpl
import com.intellij.openapi.vcs.impl.ProjectLevelVcsManagerImpl
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.testFramework.PlatformTestUtil
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import git4idea.repo.GitRepositoryManager
import java.io.File
import java.nio.file.Files

class GitChangedFilesServiceTest : BasePlatformTestCase() {

    private lateinit var repoDir: File
    private lateinit var originDir: File

    override fun setUp() {
        super.setUp()
        originDir = Files.createTempDirectory("test-git-origin").toFile()
        repoDir = Files.createTempDirectory("test-git-repo").toFile()
        setupRepo()
        registerRepoWithProject()
    }

    override fun tearDown() {
        WriteAction.runAndWait<Exception> {
            ProjectLevelVcsManager.getInstance(project).directoryMappings = emptyList()
        }
        repoDir.deleteRecursively()
        originDir.deleteRecursively()
        super.tearDown()
    }

    // --- tests ---

    fun testGetModifiedFiles_returnsUnstagedChange() {
        File(repoDir, "initial.txt").writeText("modified content")
        refreshAndWait()

        val files = project.service<GitChangedFilesService>().getModifiedFiles()
        assertTrue(files.any { it.name == "initial.txt" })
    }

    fun testGetModifiedFiles_returnsStagedFile() {
        File(repoDir, "staged.txt").writeText("staged content")
        git("add", "staged.txt")
        refreshAndWait()

        val files = project.service<GitChangedFilesService>().getModifiedFiles()
        assertTrue(files.any { it.name == "staged.txt" })
    }

    fun testGetBranchFiles_returnsFileCommittedOnBranch() {
        git("checkout", "-b", "feature")
        File(repoDir, "feature.txt").writeText("feature content")
        git("add", ".")
        git("commit", "-m", "Add feature file")
        refreshAndWait()

        val files = project.service<GitChangedFilesService>().getBranchFiles()
        assertTrue(files.any { it.name == "feature.txt" })
    }

    fun testGetBranchFiles_excludesFileFromMain() {
        git("checkout", "-b", "feature")
        File(repoDir, "feature.txt").writeText("feature content")
        git("add", ".")
        git("commit", "-m", "Add feature file")
        refreshAndWait()

        val files = project.service<GitChangedFilesService>().getBranchFiles()
        assertFalse(files.any { it.name == "initial.txt" })
    }

    fun testGetBranchAndModifiedFiles_deduplicates() {
        git("checkout", "-b", "feature")
        File(repoDir, "initial.txt").writeText("branch version")
        git("add", ".")
        git("commit", "-m", "Modify initial.txt on branch")

        File(repoDir, "initial.txt").writeText("also modified locally")
        refreshAndWait()

        val files = project.service<GitChangedFilesService>().getBranchAndModifiedFiles()
        assertEquals(1, files.count { it.name == "initial.txt" })
    }

    fun testGetBranchAndModifiedFiles_includesBothSources() {
        git("checkout", "-b", "feature")
        File(repoDir, "branch.txt").writeText("branch content")
        git("add", ".")
        git("commit", "-m", "Add branch file")

        File(repoDir, "initial.txt").writeText("modified content")
        refreshAndWait()

        val files = project.service<GitChangedFilesService>().getBranchAndModifiedFiles()
        assertTrue(files.any { it.name == "branch.txt" })
        assertTrue(files.any { it.name == "initial.txt" })
    }

    // --- helpers ---

    private fun setupRepo() {
        git("init", "-b", "main")
        git("config", "user.email", "test@test.com")
        git("config", "user.name", "Test")

        File(repoDir, "initial.txt").writeText("initial content")
        git("add", ".")
        git("commit", "-m", "Initial commit")

        ProcessBuilder("git", "init", "--bare", "-b", "main")
            .directory(originDir).start().waitFor()
        git("remote", "add", "origin", originDir.absolutePath)
        git("push", "--set-upstream", "origin", "main")
        git("remote", "set-head", "origin", "main")
    }

    private fun registerRepoWithProject() {
        val repoRoot = LocalFileSystem.getInstance().refreshAndFindFileByIoFile(repoDir)!!
        WriteAction.runAndWait<Exception> {
            ProjectLevelVcsManager.getInstance(project).directoryMappings =
                listOf(VcsDirectoryMapping(repoRoot.path, "Git"))
        }
        (ProjectLevelVcsManager.getInstance(project) as ProjectLevelVcsManagerImpl).waitForInitialized()

        val deadline = System.currentTimeMillis() + 10_000
        while (GitRepositoryManager.getInstance(project).repositories.isEmpty()
            && System.currentTimeMillis() < deadline
        ) {
            PlatformTestUtil.dispatchAllEventsInIdeEventQueue()
            Thread.sleep(50)
        }
        check(GitRepositoryManager.getInstance(project).repositories.isNotEmpty()) {
            "GitRepositoryManager did not discover the test repo within 10 seconds"
        }
    }

    private fun refreshAndWait() {
        LocalFileSystem.getInstance().refresh(false)
        (ChangeListManager.getInstance(project) as ChangeListManagerImpl).waitUntilRefreshed()
    }

    private fun git(vararg args: String) {
        val process = ProcessBuilder("git", *args)
            .directory(repoDir)
            .redirectErrorStream(true)
            .start()
        val output = process.inputStream.bufferedReader().readText()
        check(process.waitFor() == 0) { "git ${args.toList()} failed:\n$output" }
    }
}
