package com.dinesh.basketball

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.dinesh.basketball.screens.GamesScreenWithViewModel
import com.dinesh.basketball.screens.ScheduleScreenWithViewModel
import com.dinesh.basketball.state.UiState
import com.dinesh.basketball.ui.theme.BasketballTheme
import com.dinesh.basketball.viewmodels.BasketballViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @RequiresApi(Build.VERSION_CODES.O)
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BasketballTheme {
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "TEAM",
                                        fontStyle = FontStyle.Italic,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                }
                            },
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = Color.Black
                            )
                        )
                    },
                    containerColor = Color.Black
                ) {
                    Box(modifier = Modifier
                        .padding(it)
                        .background(Color.Black)) {
                        App()
                    }
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun App(){

    Column(modifier = Modifier
        .fillMaxWidth()) {

        val navController = rememberNavController()
        var selectedTab = rememberSaveable { mutableStateOf("schedule") }


        Row {

            Column(
                modifier = Modifier
                    .clickable {
                        val currentRoute = navController.currentDestination?.route
                        if (currentRoute.equals("games")) {
                            navController.navigate("schedule")
                        }
                    }
                .weight(.5f)
                    .padding(4.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Schedule",
                    color = Color.White,
                    fontSize = 12.sp
                    )
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .height(2.dp)
                    .background(if (selectedTab.value.equals("schedule")) Color.Yellow else Color.Black))

            }


            Column(modifier = Modifier
                .clickable {
                    val currentRoute = navController.currentDestination?.route
                    if (currentRoute.equals("schedule")) {
                        navController.navigate("games")
                    }
                }
                .weight(.5f)
                .padding(4.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "Games",
                    color = Color.White,
                    fontSize = 12.sp)
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .height(2.dp)
                    .background(if (selectedTab.value.equals("games")) Color.Yellow else Color.Black))

            }


        }

        val viewModel: BasketballViewModel = hiltViewModel()
        val state by viewModel.uiState.collectAsState(UiState.Loading)

        NavHost(navController = navController, startDestination = "schedule"){
            composable(route = "schedule") {
                ScheduleScreenWithViewModel(state)
                selectedTab.value = "schedule"
            }
            composable(route = "games") {
                GamesScreenWithViewModel(state)
                selectedTab.value = "games"
            }
        }
    }



}

