package com.nvandenbreemen.kb.server

import com.nvandenbreemen.kb.data.Database
import com.nvandenbreemen.kb.data.Database.setup
import com.nvandenbreemen.kb.data.Page
import com.nvandenbreemen.kb.interactor.PageInteractor
import com.nvandenbreemen.kb.repository.PageRepository
import com.nvandenbreemen.kb.server.MainServer
import com.nvandenbreemen.kb.view.ContentRenderer
import com.nvandenbreemen.kb.view.PageView
import org.apache.log4j.Logger
import spark.Request
import spark.Response
import spark.Spark

class MainServer {

    companion object {

        const val PORT = 8888

        private val logger = Logger.getLogger(MainServer::class.java)
        private var server: MainServer? = null
        fun get(): MainServer? {
            if (server == null) {
                server = MainServer()
            }
            return server
        }

        @JvmStatic
        fun main(args: Array<String>) {
            try {
                get()!!.startup()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private val pageInteractor = PageInteractor(PageRepository(Database.dao))

    @Throws(Exception::class)
    fun startup() {
        setup()
        Spark.port(PORT)
        Spark.get("/") { request: Request?, response: Response? -> "Hello from Spark" }

        Spark.get("/page/:id"){request, response ->
            request.params()[":id"]?.toInt()?.let { pageId ->
                pageInteractor.displayPage(pageId, getPageView(response))
            }
            response.body()
        }

        Spark.put("/page/:id"){ request, response ->
            return@put handlePageContentPUT(request, response)
        }

        Spark.post("/page/:id"){ request, response ->
            return@post handlePageContentPUT(request, response)
        }

        Spark.get("/edit/page/:id") { request, response ->
            request.params()[":id"]?.toInt()?.let { pageId ->
                pageInteractor.editPage(pageId, getPageView(response))
            }
            response.body()
        }

        logger.info("Main Server Up and Running on port $PORT")
    }

    private fun handlePageContentPUT(request: Request, response: Response): String {
        request.queryMap().get("title")?.let { title ->
            request.queryMap().get("body")?.let { body ->
                val title = (title.value())
                val body = (body.value())
                request.params()[":id"]?.toInt()?.let { pageID ->
                    pageInteractor.updatePage(pageID, title, body, getPageView(response))

                    return response.body()
                }
            }
        }
        return ""
    }

    private fun getPageView(response: Response) = object : PageView {
        override fun display(page: Page) {
            response.body(ContentRenderer.render(page))
        }

        override fun edit(page: Page) {
            response.body(ContentRenderer.edit(page))
        }
    }


}