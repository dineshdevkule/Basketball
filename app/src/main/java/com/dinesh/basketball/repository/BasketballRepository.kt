package com.dinesh.basketball.repository

import android.content.Context
import com.dinesh.basketball.models.Schedule
import com.dinesh.basketball.models.ScheduleListModel
import com.dinesh.basketball.models.Team
import com.dinesh.basketball.models.TeamsListModel
import com.google.gson.Gson
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class BasketballRepository @Inject constructor(@ApplicationContext private val context: Context) {

    fun loadScheduleFromAssets(): List<Schedule> {
        val jsonString = context.assets.open("Schedule.json")
            .bufferedReader()
            .use { it.readText() }

        val scheduleListModel = Gson().fromJson(jsonString, ScheduleListModel::class.java)
        return scheduleListModel.data.schedules
    }

    fun loadTeamsFromAssets(): List<Team> {
        val jsonString = context.assets.open("teams.json")
            .bufferedReader()
            .use { it.readText() }

        val teamsListModel = Gson().fromJson(jsonString, TeamsListModel::class.java)
        return teamsListModel.data.teams
    }

}