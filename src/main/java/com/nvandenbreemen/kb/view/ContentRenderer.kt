package com.nvandenbreemen.kb.view

import com.nvandenbreemen.kb.data.Page

/**
 *
 */
object ContentRenderer {

    fun render(page: Page): String {
        return """
            <h1>${page.title}</h1>
            <p>${page.content}</p>
        """.trimIndent()
    }

}