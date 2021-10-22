package com.nvandenbreemen.kb.interactor

import com.nvandenbreemen.kb.repository.PageRepository
import com.nvandenbreemen.kb.view.PageView

class PageInteractor(private val pageRepository: PageRepository) {

    fun displayPage(id: Int, pageView: PageView) {
        pageRepository.lookup(id).run { pageView.display(this) }
    }

}