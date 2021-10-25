package com.nvandenbreemen.kb.repository

import com.nvandenbreemen.kb.data.Page
import com.vandenbreemen.kevincommon.db.SQLiteDAO

class PageRepository(private val dao: SQLiteDAO) {

    fun storePage(page: Page) {
        if(page.title.isBlank() || page.content.isBlank()) {
            throw Exception("Please specify title and content for new page")
        }
        dao.insert("INSERT INTO page(title, content) VALUES(?, ?)", arrayOf(page.title, page.content))
    }

    fun getLatestPageId(): Int {
        return dao.query("SELECT max(id) as id FROM page", arrayOf())[0]["id"] as Int
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
        if(updatedCopy.title.isBlank() || updatedCopy.content.isBlank()) {
            throw Exception("Please specify title and content for page")
        }
        dao.update("UPDATE page SET title=?, content=? WHERE id=?", arrayOf(updatedCopy.title, updatedCopy.content, id))
    }

    fun lookup(byId: Int): Page {
        return dao.query("SELECT id, title, content FROM page WHERE id = ?", arrayOf(byId)).map {
            Page(it["id"] as Int, it["title"] as String, it["content"] as String)
        }[0]
    }

    fun tags(pageId: Int, tags: List<String>) {

        dao.delete("DELETE FROM page_tag WHERE pageId=?", arrayOf(pageId))

        val strBuold = StringBuilder("INSERT INTO page_tag(pageId, tag) VALUES ")
        val values = mutableListOf<Any>()
        tags.iterator().run {
            do {
                strBuold.append("(?, ?)")
                values.apply {
                    add(pageId)
                    add(next())
                }

                if(hasNext()) {
                    strBuold.append(", ")
                }

            } while (hasNext())
        }

        dao.insert(strBuold.toString(), values.toTypedArray())

    }

    fun findByTag(tag: String): List<Page> {
        val pageIds = dao.query("SELECT pageId FROM page_tag WHERE tag=?", arrayOf(tag))
        return pageIds.map {
            lookup(it["pageId"] as Int)
        }
    }

    fun getTags(pageId: Int): List<String> {
        return dao.query("SELECT tag FROM page_tag WHERE pageId=?", arrayOf(pageId)).map {
            it["tag"] as String
        }
    }

}