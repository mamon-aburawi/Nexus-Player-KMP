package com.nexusplayer

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform