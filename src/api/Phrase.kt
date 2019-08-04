package com.ktor.api

import com.ktor.API_VERSION
import com.ktor.model.Request
import com.ktor.repository.Repository
import io.ktor.application.call
import io.ktor.auth.authenticate
import io.ktor.request.*
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.post

const val PHRASE_ENDPOINT = "$API_VERSION/phrase"

fun Route.phrase(db: Repository) {

    authenticate("auth") {
        post(PHRASE_ENDPOINT) {
            val request = call.receive<Request>()
            val phrase = db.add(request.emoji, request.phrase)
            call.respond(phrase)
        }
    }
}
