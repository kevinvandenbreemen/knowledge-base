package com.nvandenbreemen.kb.data

data class Page(
    val id: Int = 0,
    val title: String, val content: String) {
    companion object {
        fun newPage(title: String, content: String): Page {
            return Page(0, title, content)
        }
    }
}