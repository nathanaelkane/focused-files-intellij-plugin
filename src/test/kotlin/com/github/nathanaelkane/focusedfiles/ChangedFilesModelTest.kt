package com.github.nathanaelkane.focusedfiles

import com.intellij.testFramework.fixtures.BasePlatformTestCase
import com.intellij.openapi.vfs.VirtualFile

class ChangedFilesModelTest : BasePlatformTestCase() {

    private fun file(path: String): VirtualFile =
        myFixture.addFileToProject(path, "").virtualFile

    private fun ChangedFilesModel.matchByName(name: String): Array<Any> =
        getElementsByName(name, false, name)

    fun testGetNames_returnsFileNames() {
        val model = ChangedFilesModel(project, listOf(file("foo/Bar.kt"), file("baz/Qux.kt")), "")

        val names = model.getNames().toList()
        assertContainsElements(names, "Bar.kt", "Qux.kt")
    }

    fun testGetNames_withNoFiles_returnsEmptyArray() {
        val model = ChangedFilesModel(project, emptyList(), "")
        assertEmpty(model.getNames().toList())
    }

    fun testGetElementsByName_returnsSingleMatch() {
        val file = file("foo/Bar.kt")
        val model = ChangedFilesModel(project, listOf(file), "")

        val elements = model.matchByName("Bar.kt")
        assertEquals(1, elements.size)
        assertEquals(file, elements[0])
    }

    fun testGetElementsByName_returnsAllFilesWithSameName() {
        val model = ChangedFilesModel(project, listOf(file("foo/Bar.kt"), file("baz/Bar.kt")), "")

        val elements = model.matchByName("Bar.kt")
        assertEquals(2, elements.size)
    }

    fun testGetElementsByName_returnsEmptyForUnknownName() {
        val model = ChangedFilesModel(project, listOf(file("Foo.kt")), "")

        val elements = model.matchByName("Unknown.kt")
        assertEmpty(elements.toList())
    }

    fun testGetElementName_returnsFileName() {
        val file = file("path/to/MyFile.kt")
        val model = ChangedFilesModel(project, listOf(file), "")

        assertEquals("MyFile.kt", model.getElementName(file))
    }

    fun testGetFullName_containsFullPath() {
        val file = file("path/to/MyFile.kt")
        val model = ChangedFilesModel(project, listOf(file), "")

        val fullName = model.getFullName(file)
        assertTrue(fullName.endsWith("MyFile.kt"))
        assertTrue(fullName.contains("path/to"))
    }

    fun testWillOpenEditor_returnsTrue() {
        assertTrue(ChangedFilesModel(project, emptyList(), "").willOpenEditor())
    }
}
