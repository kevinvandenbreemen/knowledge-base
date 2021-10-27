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

    private fun controlBar(page: Page?) : String {
        return """
<div class="$CONTROL_SECTION">
${page?.let { "<a href=\"/edit/page/${page.id}\" accesskey=\"e\">EDIT</a>" } ?: "" }
<a href="/search/page" accesskey="f">Find Pages</a>
<a href="/create/page" accesskey="n">CREATE PAGE</a>
</div>
        """.trimIndent()

    }

    fun render(page: Page): String {

        val markdownRenderBuilder = Parser.builder().build()
        val document: Node = markdownRenderBuilder.parse(page.content)
        val renderer = HtmlRenderer.builder().build()
        val rendered = renderer.render(document);

        return """
            ${controlBar(page)}
            <h1>${page.title}</h1>
            <p>$rendered</p>
            <div class="$CONTROL_SECTION"><a href="/edit/page/${page.id}" accesskey="e">EDIT</a></div>
        """.trimIndent()
    }

    fun edit(page: Page, tags: String): String {
        return """
<html>
<body onload='focus()'>
<script>
function focus() {
    document.getElementById("title").focus();
}
</script>
${controlBar(null)}
<form action="/page/${page.id}" method="post">
<input type="hidden" name="_method" value="put" />
<h2>Title</h2>
<input type="text" name="title" id="title" value="${page.title}" />
<h3>Tags</h3>
<input name="tags" value="$tags" />
<hr/>
<textarea name="body" rows="10" cols="80" >
${page.content}
</textarea>
<br/>
<div class="$CONTROL_SECTION"><input type="submit" value="Submit" accesskey="s" /></div>
</form>
</body>
</html>
        """.trimIndent()
    }

    fun createPage(): String {
        return """
<html>
<body onload='focus()'>
<script>
function focus() {
    document.getElementById("title").focus();
}
</script>
${controlBar(null)}
<form action="/create/page" method="post">
<h2>Title</h2>
<input name="title" id="title" />
<hr/>
<textarea name="body" rows="10" cols="80" >

</textarea>
<br/>
<div class="$CONTROL_SECTION"><input type="submit" value="Submit" /></div>
</form>
</body>
</html>
        """.trimIndent()
    }

    fun searchScreen(): String {
        return """
${controlBar(null)}
<body onload='focus()'>
<script>
function focus() {
    document.getElementById("query").focus();
}
</script>
<form action="/search/page/like" method="get">
Query:  <input name="query" id="query"/>
<div class="$CONTROL_SECTION"><input type="submit" value="Submit" /></div>
</form>
        """.trimIndent()
    }

    fun searchResults(pages: List<Page>): String {
        return """
${controlBar(null)}
<ul>
    ${pages.map { """
        <li /><a href="/page/${it.id}">${it.title}</a>
        """ }.joinToString(separator = "<br/>")}
</ul>
        """.trimIndent()
    }

    fun displayError(error: String): String {
        return error
    }

}