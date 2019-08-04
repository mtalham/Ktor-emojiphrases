package com.ktor.model

data class Request(val emoji: String, val phrase: String)

data class GetReq(val id: String)