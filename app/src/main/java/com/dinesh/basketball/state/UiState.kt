package com.dinesh.basketball.state

import com.dinesh.basketball.models.ScheduleDisplay
import com.dinesh.basketball.models.ScheduleListModel
import com.dinesh.basketball.models.TeamsListModel

sealed class UiState {
    object Loading : UiState()
    data class Success(val schedule: List<ScheduleDisplay>) : UiState()
    data class Error(val message: String) : UiState()
}
