package com.ktor.api

import com.ktor.API_VERSION
import com.ktor.model.EmojiPhrase
import com.ktor.model.GetReq
import com.ktor.model.Request
import com.ktor.repository.Repository
import io.ktor.application.call
import io.ktor.auth.authenticate
import io.ktor.request.*
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.delete
import io.ktor.routing.get
import io.ktor.routing.post

const val PHRASE_ENDPOINT = "$API_VERSION/phrase"

fun Route.phrase(db: Repository) {

    authenticate("auth") {
        post(PHRASE_ENDPOINT) {
            val request = call.receive<Request>()
            val phrase = db.add(EmojiPhrase(request.emoji, request.phrase))
            call.respond(phrase)
        }

        delete(PHRASE_ENDPOINT) {
            val req = call.receive<Request>()
            db.remove(EmojiPhrase(req.emoji, req.phrase))
        }

    }

//    get(PHRASE_ENDPOINT) {
//        val req = call.receive<GetReq>()
//        val phrase = db.phrase(req.id)
//        phrase?.let { p -> call.respond(p) }
//    }
//
//    get(PHRASE_ENDPOINT) {
//        call.respond(db.phrases().toArray())
//    }
}
