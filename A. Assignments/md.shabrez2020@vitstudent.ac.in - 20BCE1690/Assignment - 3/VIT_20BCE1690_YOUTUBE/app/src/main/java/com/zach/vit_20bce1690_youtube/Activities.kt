package com.zach.vit_20bce1690_youtube

sealed class Activities(val route: String) {
    object Home : Activities("home")
    object VideoScreen : Activities("video_screen")

    fun withArgs(vararg args: String): String {
        return buildString {
            append(route)
            args.forEach { arg ->
                append("/$arg")
            }
        }
    }
}
