package com.nvandenbreemen.kb.data

import com.vandenbreemen.kevincommon.db.DatabaseSchema
import com.vandenbreemen.kevincommon.db.SQLiteDAO

/**
 * The main schema of the app
 */
object Database {

    private const val DB_FILENAME = "knowledgebase.dat"
    private const val TEST_DB_NAME = "knowledgebase.test"
    private var testMode: Boolean = false

    val dao: SQLiteDAO by lazy {
        if(testMode) {
            SQLiteDAO(TEST_DB_NAME)
        } else {
            SQLiteDAO(DB_FILENAME)
        }
    }

    fun setup() {

        val schema = DatabaseSchema(dao)

        //  Database setup here
        schema.addDatabaseChange(1, "CREATE TABLE page(id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT, content TEXT)")

        val welcomeText = """
            Welcome to the KnowledgeBase app, a simple app that allows you to manage a complex base of knowledge about a subject and index it according to your needs.
        """.trimIndent()
        schema.addDatabaseChange(2, "INSERT INTO page(title, content) VALUES ('Startup Page', '$welcomeText')")

    }

    fun enterTestMode() {
        testMode = true
    }

}