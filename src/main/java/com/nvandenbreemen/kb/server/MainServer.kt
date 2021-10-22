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

    private val pageInteractor = PageInteractor(PageRepository(Database.dao))

    @Throws(Exception::class)
    fun startup() {
        setup()
        Spark.port(8888)
        Spark.get("/") { request: Request?, response: Response? -> "Hello from Spark" }

        Spark.get("/page/:id"){request, response ->
            request.params()[":id"]?.toInt()?.let { pageId ->
                pageInteractor.displayPage(pageId, object : PageView {
                    override fun display(page: Page) {
                        response.body(ContentRenderer.render(page))
                    }
                })
            }
            response.body()
        }

        logger.info("Main Server Up and Running")
    }

    companion object {
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
}