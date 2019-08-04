package com.ktor.repository

import com.ktor.model.EmojiPhrase
import java.lang.IllegalArgumentException
import java.util.concurrent.atomic.AtomicInteger

class RepositoryImpl : Repository {
    private val idCounter = AtomicInteger()
    private val phrases= ArrayList<EmojiPhrase>()

    override suspend fun add(phrase: EmojiPhrase): EmojiPhrase {
        if (phrases.contains(phrase)) {
            return phrases.find { it == phrase }!!
        }
        phrase.id = idCounter.incrementAndGet()
        phrases.add(phrase)
        return phrase
    }

    override suspend fun phrase(id: Int): EmojiPhrase? = phrase(id.toString())

    override suspend fun phrase(id: String): EmojiPhrase? =
        phrases.find { it.id.toString() == id } ?:
        throw IllegalArgumentException("No phrase found with id: $id")

    override suspend fun phrases(): ArrayList<EmojiPhrase> = phrases

    override suspend fun remove(phrase: EmojiPhrase) {
        if (!phrases.contains(phrase)) {
            throw IllegalArgumentException("Phrase does not exist ${phrase.id}")
        }
        phrases.remove(phrase)
    }

    override suspend fun remove(id: Int): Boolean = phrases.remove(phrase(id))

    override suspend fun remove(id: String): Boolean = phrases.remove(phrase(id))

    override suspend fun clear() = phrases.clear()
}