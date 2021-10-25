package com.nvandenbreemen.kb.interactor

import com.nvandenbreemen.kb.data.Database
import com.nvandenbreemen.kb.data.Page
import com.nvandenbreemen.kb.repository.PageRepository
import com.nvandenbreemen.kb.view.PageSearchView
import com.nvandenbreemen.kb.view.PageView
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeNull
import org.amshove.kluent.shouldNotBeNull
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
            override fun showError(error: String) {
                TODO("Not yet implemented")
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
            override fun showError(error: String) {
                TODO("Not yet implemented")
            }
        }

        repository.storePage(Page.newPage("Test Page", "Test page 1"))
        interactor.updatePage(1, "Test Page", "Test Page 1", "Larry, Curly     Moe", view)
        interactor.editPage(1, view)

        displayedTags shouldBeEqualTo "Larry, Curly, Moe"
    }

    @Test
    fun `should search pages`() {
        repository.storePage(Page.newPage("Test Page", "Test page 1"))
        repository.storePage(Page.newPage("Another Screen", "Similar Content 1"))
        var pagesResult: List<Page>? = null

        val searchView = object : PageSearchView {
            override fun displayResults(pages: List<Page>) {
                pagesResult = pages
            }
        }

        interactor.searchPages("1", searchView)
        pagesResult.shouldNotBeNull()
        pagesResult!!.size shouldBeEqualTo 2
    }

    @Test
    fun `should create page`() {
        var displayedPage: Page? = null
        val view = object: PageView {
            override fun display(page: Page) {
                displayedPage = page
            }

            override fun edit(page: Page, tags: String) {

            }
            override fun showError(error: String) {
                TODO("Not yet implemented")
            }
        }

        interactor.createPage("new page", "new page content", view)

        displayedPage.shouldNotBeNull()
        displayedPage!!.title shouldBeEqualTo "new page"
        displayedPage!!.content shouldBeEqualTo "new page content"
    }

    @Test
    fun `should raise an error if user submits missing info for new page`() {
        var displayedPage: Page? = null
        var displayedError: String? = null
        val view = object: PageView {
            override fun display(page: Page) {
                displayedPage = page
            }

            override fun edit(page: Page, tags: String) {

            }

            override fun showError(error: String) {
                displayedError = error
            }

        }

        interactor.createPage("", "test", view)
        displayedPage.shouldBeNull()
        displayedError.shouldNotBeNull()

    }

    @Test
    fun `should raise an error if user submits missing info for existing page`() {
        var displayedPage: Page? = null
        var displayedError: String? = null
        val view = object: PageView {
            override fun display(page: Page) {
                displayedPage = page
            }

            override fun edit(page: Page, tags: String) {

            }

            override fun showError(error: String) {
                displayedError = error
            }

        }

        repository.storePage(Page.newPage("Test Page", "Test page 1"))
        interactor.updatePage(1, "", "Test Page 1", "Larry, Curly     Moe", view)

        displayedPage.shouldBeNull()
        displayedError.shouldNotBeNull()

    }

}