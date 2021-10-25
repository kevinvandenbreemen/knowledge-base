package com.nvandenbreemen.kb.view

import com.nvandenbreemen.kb.data.Page
import org.commonmark.node.Node
import org.commonmark.parser.Parser
import org.commonmark.renderer.html.HtmlRenderer




/**
 *
 */
object ContentRenderer {

    private const val CONTROL_SECTION = "_control"

    fun render(page: Page): String {

        val markdownRenderBuilder = Parser.builder().build()
        val document: Node = markdownRenderBuilder.parse(page.content)
        val renderer = HtmlRenderer.builder().build()
        val rendered = renderer.render(document);

        return """
            <div class="$CONTROL_SECTION"><a href="/edit/page/${page.id}" accesskey="e">EDIT</a></div>
            <h1>${page.title}</h1>
            <p>$rendered</p>
            <div class="$CONTROL_SECTION"><a href="/edit/page/${page.id}" accesskey="e">EDIT</a></div>
        """.trimIndent()
    }

    fun edit(page: Page, tags: String): String {
        return """
<form action="/page/${page.id}" method="post">
<input type="hidden" name="_method" value="put" />
<h2>Title</h2>
<input name="title" value="${page.title}" />
<h3>Tags</h3>
<input name="tags" value="$tags" />
<hr/>
<textarea name="body" rows="10" cols="80" >
${page.content}
</textarea>
<br/>
<div class="$CONTROL_SECTION"><input type="submit" value="Submit" /></div>
</form>
        """.trimIndent()
    }

}