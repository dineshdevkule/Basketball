package com.dinesh.basketball.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dinesh.basketball.models.ScheduleDisplay
import com.dinesh.basketball.repository.BasketballRepository
import com.dinesh.basketball.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BasketballViewModel @Inject constructor(private  val repository: BasketballRepository):ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState

    init {
        fetchData()
    }

    private fun fetchData() {
        viewModelScope.launch {
            delay(1500)
            try {
                val schedule = repository.loadScheduleFromAssets()
                val teams = repository.loadTeamsFromAssets()

                val teamMap = teams.associateBy { it.tid }

                val displayList = schedule.map {
                    ScheduleDisplay(
                        homeTeamName = it.h.ta?: "Unknown",
                        homeTeamlogo = getTeamLogoUrl(teamMap[it.h.tid]?.tid?:""),
                        homeTeamScore = it.h.s?: "0",
                        homeTeamCity = it.h.tc?: "Unknown",
                        awayTeamName = it.v.ta?: "Unknown",
                        awayTeamlogo = getTeamLogoUrl(teamMap[it.v.tid]?.tid?:""),
                        awayTeamScore = it.v.s?: "0",
                        awayTeamCity = it.v.tc?: "Unknown",
                        awayTeamTid = it.v.tid?: "Unknown",
                        dateTime = it.gametime,
                        arenaCity = it.arena_city,
                        cardColor = teamMap[it.v.tid]?.color?:"" ,
                        status = it.st
                    )
                }

                _uiState.value = UiState.Success(displayList)
            } catch (e: Exception) {
                _uiState.value = UiState.Error("Unable to load data")
            }
        }
    }


    fun getTeamLogoUrl(teamId: String): String {
        return "https://cdn.nba.com/logos/nba/$teamId/primary/L/logo.svg"
    }
}