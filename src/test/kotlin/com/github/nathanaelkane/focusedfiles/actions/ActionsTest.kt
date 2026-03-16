package com.github.nathanaelkane.focusedfiles.actions

import com.intellij.openapi.actionSystem.ActionManager
import com.intellij.testFramework.fixtures.BasePlatformTestCase

class ActionsTest : BasePlatformTestCase() {

    fun testShowModifiedFilesAction_isRegistered() {
        val action = ActionManager.getInstance().getAction("FocusedFiles.ShowModified")
        assertNotNull("ShowModifiedFilesAction must be registered", action)
        assertInstanceOf(action, ShowModifiedFilesAction::class.java)
    }

    fun testShowBranchFilesAction_isRegistered() {
        val action = ActionManager.getInstance().getAction("FocusedFiles.ShowBranch")
        assertNotNull("ShowBranchFilesAction must be registered", action)
        assertInstanceOf(action, ShowBranchFilesAction::class.java)
    }

    fun testShowBranchAndModifiedFilesAction_isRegistered() {
        val action = ActionManager.getInstance().getAction("FocusedFiles.ShowBranchAndModified")
        assertNotNull("ShowBranchAndModifiedFilesAction must be registered", action)
        assertInstanceOf(action, ShowBranchAndModifiedFilesAction::class.java)
    }
}
