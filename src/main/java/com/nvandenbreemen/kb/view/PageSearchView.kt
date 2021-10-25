package com.nvandenbreemen.kb.view

import com.nvandenbreemen.kb.data.Page

interface PageSearchView {

    fun displayResults(pages: List<Page>)

}