package com.github.nathanaelkane.focusedfiles.actions

import com.intellij.openapi.actionSystem.ActionManager
import com.intellij.testFramework.fixtures.BasePlatformTestCase

class ActionsTest : BasePlatformTestCase() {

    fun testShowGitModifiedFilesAction_isRegistered() {
        val action = ActionManager.getInstance().getAction("FocusedFiles.ShowGitModifiedFiles")
        assertNotNull("ShowGitModifiedFilesAction must be registered", action)
        assertInstanceOf(action, ShowGitModifiedFilesAction::class.java)
    }

    fun testShowGitBranchFilesAction_isRegistered() {
        val action = ActionManager.getInstance().getAction("FocusedFiles.ShowGitBranchFiles")
        assertNotNull("ShowGitBranchFilesAction must be registered", action)
        assertInstanceOf(action, ShowGitBranchFilesAction::class.java)
    }

    fun testShowGitBranchAndModifiedFilesAction_isRegistered() {
        val action = ActionManager.getInstance().getAction("FocusedFiles.ShowGitBranchAndModifiedFiles")
        assertNotNull("ShowGitBranchAndModifiedFilesAction must be registered", action)
        assertInstanceOf(action, ShowGitBranchAndModifiedFilesAction::class.java)
    }
}
