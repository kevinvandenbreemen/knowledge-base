package com.nvandenbreemen.kb.repository

import com.nvandenbreemen.kb.data.Database
import com.nvandenbreemen.kb.data.Page
import org.amshove.kluent.shouldBeEmpty
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldHaveSize
import org.amshove.kluent.shouldNotBeEmpty
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.File

internal class PageRepositoryTest {

    private lateinit var repository: PageRepository

    @BeforeEach
    fun setup() {
        Database.enterTestMode()
        Database.setup()
        repository = PageRepository(Database.dao)
    }

    @AfterEach
    fun tearDown() {
        if(!File("knowledgebase.test").delete()){
            throw RuntimeException("Failed to cleanup test data!")
        }
    }

    @Test
    fun `should store new page`() {
        val newPage = Page.newPage("Test Page", "This is a test page")
        repository.storePage(newPage)

        val pages = repository.findPages("Test Page")
        pages.shouldNotBeEmpty()
        pages[0].title shouldBeEqualTo "Test Page"
        pages[0].content shouldBeEqualTo "This is a test page"

    }
    @Test
    fun `should query page by similarity`() {
        val newPage = Page.newPage("Test Page", "This is a test page")
        repository.storePage(newPage)

        val pages = repository.findPages("Test")
        pages.shouldNotBeEmpty()
        pages[0].title shouldBeEqualTo "Test Page"
        pages[0].content shouldBeEqualTo "This is a test page"

    }

    @Test
    fun `should query page by similarity - suffix`() {
        val newPage = Page.newPage("Test Page", "This is a test page")
        repository.storePage(newPage)

        val pages = repository.findPages("Page")
        pages.shouldNotBeEmpty()
        pages[0].title shouldBeEqualTo "Test Page"
        pages[0].content shouldBeEqualTo "This is a test page"

    }

    @Test
    fun `should query page by similarity - ignore case`() {
        val newPage = Page.newPage("Test Page", "This is a test page")
        repository.storePage(newPage)

        val pages = repository.findPages("pAGe")
        pages.shouldNotBeEmpty()
        pages[0].title shouldBeEqualTo "Test Page"
        pages[0].content shouldBeEqualTo "This is a test page"

    }

    @Test
    fun `should handle no result found for title search`() {
        val newPage = Page.newPage("Test Page", "This is a test page")
        repository.storePage(newPage)

        repository.findPages("nonExistent").shouldBeEmpty()
    }

    @Test
    fun `should update a page`() {
        val newPage = Page.newPage("Test Page", "This is a test page")
        repository.storePage(newPage)

        val page = repository.findPages("Page")[0]

        val updatedCopy = Page.newPage(page.title, "${page.content} __ faw;elrkajsdf;lkasdf;laksdf")
        repository.update(page.id, updatedCopy)
        repository.findPages("Page")[0].content.shouldBeEqualTo("This is a test page __ faw;elrkajsdf;lkasdf;laksdf")
    }

    @Test
    fun `should query pages by content`() {

        repository.storePage(Page.newPage("Test Page", "Test page 1"))
        repository.storePage(Page.newPage("Other Content", "page test 2"))
        repository.storePage(Page.newPage("Something Else", "Jabberwocky was here"))

        repository.findPages("test").apply {
            shouldHaveSize(2)
            this[0].title shouldBeEqualTo "Test Page"
            this[1].title shouldBeEqualTo "Other Content"
        }
    }

    @Test
    fun `should fetch page by id`() {
        repository.storePage(Page.newPage("Test Page", "Test page 1"))
        val page = repository.lookup(1)
        page.id shouldBeEqualTo 1
    }

    @Test
    fun `should provide for specifying tags for a page`() {
        repository.storePage(Page.newPage("Test Page", "Test page 1"))
        repository.storePage(Page.newPage("Other Page", "OTP"))

        repository.tags(1, listOf("test", "tag", "smurf"))
        val pages = repository.findByTag("smurf")
        pages.shouldNotBeEmpty()
        pages.size shouldBeEqualTo 1
    }

    @Test
    fun `should provide for updating tags for a page`() {
        repository.storePage(Page.newPage("Test Page", "Test page 1"))
        repository.storePage(Page.newPage("Other Page", "OTP"))

        repository.tags(1, listOf("test", "tag", "smurf"))
        repository.tags(1, listOf("test", "tag", "smurf", "kitten"))
        val pages = repository.findByTag("smurf")
        pages.shouldNotBeEmpty()
        pages.size shouldBeEqualTo 1
    }

    @Test
    fun `should get tags for a page`() {
        repository.storePage(Page.newPage("Test Page", "Test page 1"))
        repository.storePage(Page.newPage("Other Page", "OTP"))

        repository.tags(1, listOf("test", "tag", "smurf"))
        val tags = repository.getTags(1)
        tags.shouldNotBeEmpty()
        tags shouldBeEqualTo listOf("test", "tag", "smurf")
    }

}