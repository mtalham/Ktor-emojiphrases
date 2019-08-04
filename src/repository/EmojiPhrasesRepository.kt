package com.ktor.repository

import com.ktor.model.EmojiPhrase
import com.ktor.model.EmojiPhrases
import com.ktor.repository.DatabaseFactory.dbQuery
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.lang.IllegalArgumentException

class EmojiPhrasesRepository : Repository {
    override suspend fun add(emojiValue: String, phraseValue: String) {
        transaction {
            EmojiPhrases.insert {
                it[emoji] = emojiValue
                it[phrase] = phraseValue
            }
        }
    }

    override suspend fun phrase(id: Int): EmojiPhrase? = dbQuery {
        EmojiPhrases.select {
            (EmojiPhrases.id eq id)
        }.mapNotNull { it.toEmojiPhrase() }.singleOrNull()
    }

    override suspend fun phrase(id: String): EmojiPhrase? =
        phrase(id.toInt())

    override suspend fun phrases(): List<EmojiPhrase> = dbQuery {
        EmojiPhrases.selectAll().mapNotNull { it.toEmojiPhrase() }
    }

    override suspend fun remove(id: Int): Boolean {
        if (phrase(id) == null) throw IllegalArgumentException("No phrase found for id: $id")
        return dbQuery {
            EmojiPhrases.deleteWhere { EmojiPhrases.id eq id } > 0
        }
    }

    override suspend fun remove(id: String): Boolean = remove(id.toInt())

    override suspend fun clear() {
        EmojiPhrases.deleteAll()
    }

    private fun ResultRow.toEmojiPhrase(): EmojiPhrase =
        EmojiPhrase(
            id = this[EmojiPhrases.id].value,
            emoji = this[EmojiPhrases.emoji],
            phrase = this[EmojiPhrases.phrase]
        )
}