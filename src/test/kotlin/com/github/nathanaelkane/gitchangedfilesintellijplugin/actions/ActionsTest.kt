package com.github.nathanaelkane.gitchangedfilesintellijplugin.actions

import com.intellij.openapi.actionSystem.ActionManager
import com.intellij.testFramework.fixtures.BasePlatformTestCase

class ActionsTest : BasePlatformTestCase() {

    fun testShowModifiedFilesAction_isRegistered() {
        val action = ActionManager.getInstance().getAction("GitChangedFiles.ShowModified")
        assertNotNull("ShowModifiedFilesAction must be registered", action)
        assertInstanceOf(action, ShowModifiedFilesAction::class.java)
    }

    fun testShowBranchFilesAction_isRegistered() {
        val action = ActionManager.getInstance().getAction("GitChangedFiles.ShowBranch")
        assertNotNull("ShowBranchFilesAction must be registered", action)
        assertInstanceOf(action, ShowBranchFilesAction::class.java)
    }
}
