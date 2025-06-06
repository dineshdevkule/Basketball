package com.dinesh.basketball.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.dinesh.basketball.state.UiState
import java.time.ZonedDateTime

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun GamesScreenWithViewModel(state: UiState) {
    GamesScreen(state)
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun GamesScreen(state: UiState) {
    when (state) {
        is UiState.Loading -> CircularProgressIndicator(
            modifier = Modifier
                .fillMaxSize()
                .wrapContentSize()
        )

        is UiState.Error -> Text("Error: ${state.message}")

        is UiState.Success -> {
            val sortedSchedule = state.schedule.sortedBy { ZonedDateTime.parse(it.dateTime) }

            var searchQuery by remember { mutableStateOf("") }

            val listState = rememberLazyListState()

            val filteredSchedule = remember(searchQuery, sortedSchedule) {
                if (searchQuery.isBlank()) {
                    sortedSchedule
                } else {
                    sortedSchedule.filter { game ->
                                game.awayTeamName.contains(searchQuery, ignoreCase = true) ||
                                game.homeTeamName.contains(searchQuery, ignoreCase = true) ||
                                game.awayTeamCity.contains(searchQuery, ignoreCase = true) ||
                        game.homeTeamCity.contains(searchQuery, ignoreCase = true)
                    }
                }
            }

            Column(modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)) {

                SearchBar(query = searchQuery, onQueryChanged = { searchQuery = it })

                if (searchQuery.isNotBlank()) {

                    LazyColumn(state = listState) {
                        items(filteredSchedule) { game ->
                            ScheduleItem(game = game)
                        }
                    }
                }


            }
        }
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(
    query: String,
    onQueryChanged: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "Search..."
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChanged,
        placeholder = { Text(placeholder) },
        leadingIcon = {
            Icon(imageVector = Icons.Default.Search, contentDescription = "Search Icon")
        },
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        singleLine = true,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            containerColor = Color.White,
            focusedBorderColor = Color.Gray,
            unfocusedBorderColor = Color.LightGray
        )
    )
}