package com.kaappi.scriptkiddiebulletin

fun timestampToTimeString(timestamp: Int): String {
    val minutes = (System.currentTimeMillis() / 1000 - timestamp) / 60
    return when {
        (minutes >= 120) -> " ${minutes / 60} hours ago"
        (minutes >= 60) -> " 1 hour ago"
        else -> " $minutes minutes ago"
    }
}