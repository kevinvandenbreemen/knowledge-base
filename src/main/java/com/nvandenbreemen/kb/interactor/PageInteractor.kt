package com.nvandenbreemen.kb.interactor

import com.nvandenbreemen.kb.data.Page
import com.nvandenbreemen.kb.repository.PageRepository
import com.nvandenbreemen.kb.view.PageView

class PageInteractor(private val pageRepository: PageRepository) {

    fun displayPage(id: Int, pageView: PageView) {
        pageRepository.lookup(id).run { pageView.display(this) }
    }

    fun updatePage(id: Int, title: String, body: String, pageView: PageView) {
        val updated = Page(id, title, body)
        pageRepository.update(id, updated)
        displayPage(id, pageView)
    }

    fun editPage(id: Int, pageView: PageView) {
        pageRepository.lookup(id).run {
            pageView.edit(this)
        }
    }

}