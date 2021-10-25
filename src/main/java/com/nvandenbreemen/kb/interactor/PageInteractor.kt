package com.nvandenbreemen.kb.interactor

import com.nvandenbreemen.kb.data.Page
import com.nvandenbreemen.kb.repository.PageRepository
import com.nvandenbreemen.kb.view.PageSearchView
import com.nvandenbreemen.kb.view.PageView

class PageInteractor(private val pageRepository: PageRepository) {

    fun displayPage(id: Int, pageView: PageView) {
        pageRepository.lookup(id).run { pageView.display(this) }
    }

    fun updatePage(id: Int, title: String, body: String, tags: String, pageView: PageView) {
        val updated = Page(id, title, body)
        val tagsList = tags.split(Regex("[\\s,]+"))
        try {
            pageRepository.update(id, updated)
        } catch (ex: Exception) {
            pageView.showError(ex.localizedMessage)
            return
        }
        pageRepository.tags(id, tagsList)
        displayPage(id, pageView)
    }

    fun editPage(id: Int, pageView: PageView) {
        pageRepository.lookup(id).run {
            val tags = pageRepository.getTags(id).joinToString(separator = ", ")
            pageView.edit(this, tags)
        }
    }

    fun searchPages(searchTerm: String, searchView: PageSearchView) {
        pageRepository.findPages(searchTerm).also { searchView.displayResults(it) }
    }

    fun createPage(title: String, content: String, view: PageView) {
        try {
            pageRepository.storePage(Page.newPage(title, content))
        } catch (ex: Exception) {
            view.showError(ex.localizedMessage)
            return
        }
        val id = pageRepository.getLatestPageId()
        view.display(pageRepository.lookup(id))
    }

}