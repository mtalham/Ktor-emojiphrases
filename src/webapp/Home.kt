package com.ktor.webapp

import io.ktor.application.call
import io.ktor.freemarker.FreeMarkerContent
import io.ktor.locations.KtorExperimentalLocationsAPI
import io.ktor.locations.*
import io.ktor.response.*
import io.ktor.routing.Route

const val HOME = "/"

@KtorExperimentalLocationsAPI
@Location(HOME)
class Home

@KtorExperimentalLocationsAPI
fun Route.home() {
    get<Home> {
        call.respond(FreeMarkerContent("home.ftl", null))
    }
}