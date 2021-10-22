package com.nvandenbreemen.kb.repository

import com.nvandenbreemen.kb.data.Page
import com.vandenbreemen.kevincommon.db.SQLiteDAO

class PageRepository(private val dao: SQLiteDAO) {

    fun storePage(page: Page) {
        dao.insert("INSERT INTO page(title, content) VALUES(?, ?)", arrayOf(page.title, page.content))
    }

    /**
     * Search for pages with title or content matching the given string
     */
    fun findPages(like: String): List<Page> {
        return dao.query("SELECT id, title, content FROM page WHERE title LIKE ? OR content LIKE ?", arrayOf("%$like%", "%$like%")).map {
            Page(it["id"] as Int, it["title"] as String, it["content"] as String)
        }
    }

    fun update(id: Int, updatedCopy: Page) {
        dao.update("UPDATE page SET title=?, content=? WHERE id=?", arrayOf(updatedCopy.title, updatedCopy.content, id))
    }

    fun lookup(byId: Int): Page {
        return dao.query("SELECT id, title, content FROM page WHERE id = ?", arrayOf(byId)).map {
            Page(it["id"] as Int, it["title"] as String, it["content"] as String)
        }[0]
    }

}