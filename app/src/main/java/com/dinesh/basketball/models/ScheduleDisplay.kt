package com.dinesh.basketball.models

data class ScheduleDisplay(
    val homeTeamName: String,
    val homeTeamlogo: String,
    val homeTeamScore: String,
    val homeTeamCity: String,
    val awayTeamName: String,
    val awayTeamlogo: String,
    val awayTeamScore: String,
    val awayTeamCity: String,
    val awayTeamTid: String,
    val status:Int,
    val arenaCity:String,
    val cardColor:String,
    val dateTime: String
)