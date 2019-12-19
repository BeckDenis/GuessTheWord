package com.example.android.guesstheword


import com.example.android.guesstheword.database.Player

fun formatPlayers(players: List<Player>): String {
    val sb = StringBuilder()
    sb.apply {
        players.forEach {
            append("<br>")
            append(it.playerId)
            append(it.name)
            append(it.score)
        }
    }
    return sb.toString()
}