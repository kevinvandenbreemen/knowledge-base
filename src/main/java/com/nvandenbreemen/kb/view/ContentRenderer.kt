package com.nvandenbreemen.kb.view

import com.nvandenbreemen.kb.data.Page

/**
 *
 */
object ContentRenderer {

    private const val CONTROL_SECTION = "_control"

    fun render(page: Page): String {
        return """
            <div class="$CONTROL_SECTION"><a href="/edit/page/${page.id}" accesskey="e">EDIT</a></div>
            <h1>${page.title}</h1>
            <p>${page.content}</p>
            <div class="$CONTROL_SECTION"><a href="/edit/page/${page.id}" accesskey="e">EDIT</a></div>
        """.trimIndent()
    }

    fun edit(page: Page): String {
        return """
            <form action="/page/${page.id}" method="post">
            <input type="hidden" name="_method" value="put" />
            <h2>Title</h2>
            <input name="title" value="${page.title}" />
            <br/>
            <textarea name="body" rows="10" cols="20" >
            ${page.content}
            </textarea>
            <br/>
            <div class="$CONTROL_SECTION"><input type="submit" value="Submit" /></div>
            </form>
        """.trimIndent()
    }

}