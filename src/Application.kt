package com.ktor

import com.ktor.api.phrase
import com.ktor.model.User
import com.ktor.repository.DatabaseFactory
import com.ktor.repository.EmojiPhrasesRepository
import com.ktor.webapp.about
import com.ktor.webapp.home
import com.ktor.webapp.phrases
import com.ryanharter.ktor.moshi.moshi
import freemarker.cache.ClassTemplateLoader
import io.ktor.application.*
import io.ktor.auth.Authentication
import io.ktor.auth.basic
import io.ktor.response.*
import io.ktor.routing.routing
import io.ktor.features.*
import io.ktor.freemarker.FreeMarker
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.resource
import io.ktor.http.content.static
import io.ktor.locations.KtorExperimentalLocationsAPI
import io.ktor.locations.Locations
import io.ktor.locations.locations

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@KtorExperimentalLocationsAPI
@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
    install(DefaultHeaders)

    install(StatusPages) {
        exception<Throwable> { e ->
            call.respondText(e.localizedMessage, ContentType.Text.Plain, HttpStatusCode.InternalServerError)
        }
    }

    install(ContentNegotiation) {
        moshi()
    }

    install(FreeMarker) {
        templateLoader = ClassTemplateLoader(this::class.java.classLoader, "templates")
    }

    install(Authentication) {
        basic (name = "auth") {
            realm = "Ktor server"
            validate { credentials -> if (credentials.password == "${credentials.name}123") User(credentials.name) else null }
        }
    }

    install(Locations)

    DatabaseFactory.init()

    val db = EmojiPhrasesRepository()

    routing {
        static("/static") {
            resource("images")
        }

        home()
        about()
        phrases(db)
        //Api
        phrase(db)
    }
}

const val API_VERSION = "/api/v1"

@KtorExperimentalLocationsAPI
suspend fun ApplicationCall.redirect(location: Any) {
    respondRedirect(application.locations.href(location))
}