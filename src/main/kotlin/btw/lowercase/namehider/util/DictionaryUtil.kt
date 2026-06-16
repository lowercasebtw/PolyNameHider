package btw.lowercase.namehider.util

import btw.lowercase.namehider.NameHider

object DictionaryUtil {
    private val words = ArrayList<String>()

    fun load() {
        val stream = NameHider::class.java.getResourceAsStream("/dictionary.txt") ?: return
        stream.bufferedReader().forEachLine { words.add(it.trim()) }
        stream.close()
    }

    fun random() = words.random()
}