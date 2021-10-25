package com.nvandenbreemen.kb.view

import com.nvandenbreemen.kb.data.Page

/**
 *
 */
interface PageView {

    fun display(page: Page)

    fun edit(page: Page, tags: String)

}