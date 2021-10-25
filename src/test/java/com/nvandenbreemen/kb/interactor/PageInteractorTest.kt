package com.nvandenbreemen.kb.interactor

import com.nvandenbreemen.kb.data.Database
import com.nvandenbreemen.kb.data.Page
import com.nvandenbreemen.kb.repository.PageRepository
import com.nvandenbreemen.kb.view.PageView
import org.amshove.kluent.shouldBeEqualTo
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.File

internal class PageInteractorTest {

    private lateinit var repository: PageRepository
    private lateinit var interactor: PageInteractor


    @BeforeEach
    fun setup() {
        Database.enterTestMode()
        Database.setup()
        repository = PageRepository(Database.dao)
        interactor = PageInteractor(repository)
    }

    @AfterEach
    fun tearDown() {
        if(!File("knowledgebase.test").delete()){
            throw RuntimeException("Failed to cleanup test data!")
        }
    }

    @Test
    fun `should handle setting tags`() {

        var displayedPage: Page? = null
        val view = object: PageView {
            override fun display(page: Page) {
                displayedPage = page
            }

            override fun edit(page: Page, tags: String) {

            }

        }

        repository.storePage(Page.newPage("Test Page", "Test page 1"))
        interactor.updatePage(1, "Test Page", "Test Page 1", "Larry, Curly     Moe", view)

        val tags = repository.getTags(1)
        tags.size shouldBeEqualTo 3
        tags shouldBeEqualTo listOf("Larry", "Curly", "Moe")

    }

    @Test
    fun `should display tags for a page`() {
        var displayedPage: Page? = null
        var displayedTags: String? = null
        val view = object: PageView {
            override fun display(page: Page) {
                displayedPage = page
            }

            override fun edit(page: Page, tags: String) {
                displayedPage = page
                displayedTags = tags
            }

        }

        repository.storePage(Page.newPage("Test Page", "Test page 1"))
        interactor.updatePage(1, "Test Page", "Test Page 1", "Larry, Curly     Moe", view)
        interactor.editPage(1, view)

        displayedTags shouldBeEqualTo "Larry, Curly, Moe"
    }

}