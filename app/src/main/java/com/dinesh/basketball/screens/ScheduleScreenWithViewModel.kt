package com.dinesh.basketball.screens

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.ImageLoader
import coil3.compose.rememberAsyncImagePainter
import coil3.svg.SvgDecoder
import com.dinesh.basketball.R
import com.dinesh.basketball.models.ScheduleDisplay
import com.dinesh.basketball.state.UiState
import kotlinx.coroutines.launch
import java.time.YearMonth
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ScheduleScreenWithViewModel(state: UiState) {
    ScheduleScreen(state)
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ScheduleScreen(state: UiState) {
    when (state) {
        is UiState.Loading -> CircularProgressIndicator(modifier = Modifier
            .fillMaxSize()
            .wrapContentSize())
        is UiState.Error -> Text("Error: ${state.message}")
        is UiState.Success -> {
            // Sort schedule by date
            val sortedSchedule = state.schedule.sortedBy { ZonedDateTime.parse(it.dateTime) }

            // Group by month
            val scheduleByMonth = sortedSchedule.groupBy {
                YearMonth.from(ZonedDateTime.parse(it.dateTime))
            }

            showLogs("scheduleByMonth = $scheduleByMonth")


            // Map month to its first index in the sorted schedule for scroll by month
            val monthStartIndexes = scheduleByMonth.entries
                .associate { it.key to sortedSchedule.indexOfFirst { game ->
                    YearMonth.from(ZonedDateTime.parse(game.dateTime)) == it.key
                } }


            showLogs("monthStartIndexes = $monthStartIndexes")


            // Get the list of months, sorted in chronological order
            val months = scheduleByMonth.keys.sorted()


            showLogs("months = $months")


            // LazyList state for scrolling
            val listState = rememberLazyListState()
            val coroutineScope = rememberCoroutineScope()



            // Find the next game after current date
            var nextGameIndex = sortedSchedule.indexOfFirst {
                try {
                    ZonedDateTime.parse(it.dateTime).isAfter(ZonedDateTime.now())
                } catch (e: Exception) {
                    false
                }
            }.coerceAtLeast(0)



            // show scroll to bottom
            if (nextGameIndex == 0){
                nextGameIndex = sortedSchedule.size
            }

            showLogs("nextGameIndex = $nextGameIndex")


            // Automatically scroll to the next game
            LaunchedEffect(nextGameIndex) {
                listState.scrollToItem(nextGameIndex)
                showLogs("nextGameIndex = $nextGameIndex")
            }

            // Get current month based on today's date
            val currentMonthFromDate = YearMonth.from(ZonedDateTime.now())

            // Find the initial index for the current month
            val currentMonthIndex = remember {
                months.indexOfFirst { it == currentMonthFromDate }.takeIf { it >= 0 } ?: 0
            }


            showLogs("currentMonthIndex = $currentMonthIndex")


            // Track the currently visible month based on scroll position
            var displayedMonthIndex by remember { mutableStateOf(currentMonthIndex) }

            showLogs("displayedMonthIndex = $displayedMonthIndex")


            // Update displayed month dynamically as the list scrolls
            LaunchedEffect(listState.firstVisibleItemIndex) {
                val firstVisibleIndex = listState.firstVisibleItemIndex
                if (firstVisibleIndex in sortedSchedule.indices) {
                    val visibleMonth = YearMonth.from(ZonedDateTime.parse(sortedSchedule[firstVisibleIndex].dateTime))
                    displayedMonthIndex = months.indexOf(visibleMonth)
                }
            }




            Column {
                Box(
                    modifier = Modifier
                        .background(Color.DarkGray)
                        .padding(10.dp)
                        .fillMaxWidth()
                        .wrapContentHeight()
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        // Previous month arrow
                        Image(
                            painter = painterResource(id = R.drawable.arrow_up),
                            contentDescription = "",
                            modifier = Modifier
                                .clickable {
                                    if (displayedMonthIndex > 0) {
                                        displayedMonthIndex--
                                        val monthToScroll = months.getOrNull(displayedMonthIndex)
                                        monthToScroll?.let {
                                            val startIndex = monthStartIndexes[it]
                                            startIndex?.let { index ->
                                                coroutineScope.launch() {
                                                    listState.animateScrollToItem(index)
                                                    showLogs("Scrolling to previous month: $it, index = $index")
                                                }
                                            }
                                        }
                                    }
                                }
                                .padding(end = 10.dp)
                                .size(20.dp)
                        )

                        // Display month and year in "MMMM yyyy" format
                        Text(
                            fontSize = 14.sp,
                            color = Color.White,
                            text = months.getOrNull(displayedMonthIndex)?.format(DateTimeFormatter.ofPattern("MMMM yyyy")) ?: ""
                        )

                        // Next month arrow
                        Image(
                            painter = painterResource(id = R.drawable.arrow_up),
                            contentDescription = "",
                            modifier = Modifier
                                .clickable {
                                    if (displayedMonthIndex < months.lastIndex) {
                                        displayedMonthIndex++
                                        val monthToScroll = months.getOrNull(displayedMonthIndex)
                                        monthToScroll?.let {
                                            val startIndex = monthStartIndexes[it]
                                            startIndex?.let { index ->
                                                coroutineScope.launch {
                                                    listState.animateScrollToItem(index)
                                                    showLogs("Scrolling to next month: $it, index = $index")
                                                }
                                            }
                                        }
                                    }
                                }
                                .padding(start = 10.dp)
                                .size(20.dp)
                                .rotate(180f)
                        )
                    }
                }

                // LazyColumn displaying the sorted schedule
                LazyColumn(state = listState) {
                    items(sortedSchedule) {
                        ScheduleItem(game = it)
                    }
                }
            }
        }
    }
}




@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ScheduleItem(game: ScheduleDisplay){

    val parsedColor = try {
        Color(android.graphics.Color.parseColor("#${game.cardColor}"))
    } catch (e: IllegalArgumentException) {
        Color.DarkGray
    }

    Box(
        modifier = Modifier
            .padding(top = 16.dp, start = 16.dp, end = 16.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(parsedColor)
            .padding(8.dp)
            .fillMaxWidth()
    ){

        Column(horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()) {


            val isHomeTeam = if (game.awayTeamTid.equals("1610612748")) true else false

            var headerText = ""

            val appTeam = if (isHomeTeam) "HOME" else "AWAY"

            when(game.status){
                1 ->{
                    headerText =appTeam + " | "+ formatGameDate(game.dateTime,"EEE MMM dd") + " | "+formatGameDate(game.dateTime,"h.mm a")
                }
                2 ->{
                    headerText = formatGameDate(game.dateTime,"HH.mm.ss")
                }
                3 ->{
                    headerText = appTeam + " | "+ formatGameDate(game.dateTime,"EEE MMM dd") + " | FINAL"
                }
            }

            Text(
                text = headerText,
                color = Color.White,
                fontSize = 10.sp,
            )

            Row(verticalAlignment = Alignment.CenterVertically) {

                Column(horizontalAlignment = Alignment.CenterHorizontally) {


                    ImageLogo(game.awayTeamlogo)

                    if (game.status != 1) {
                        TextTeamName(game.awayTeamName)
                    }
                }

                if (game.status == 1) {
                    TextTeamName(game.awayTeamName)
                }else{
                    TextScore(game.awayTeamScore)
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {

                    if (game.status == 2) {
                        Box(modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color.Black)
                            .padding(start = 8.dp, end = 8.dp)

                        ){
                            Text(text = "LIVE",
                                color = Color.White,
                                fontSize = 9.sp,
                            )
                        }
                    }

                    Text(text = if (isHomeTeam) "vs" else "@",
                        color = Color.White,
                        fontSize = 12.sp,
                        modifier = Modifier
                            .padding(4.dp)
                    )
                }


                if (game.status == 1) {
                    TextTeamName(game.homeTeamName)
                }else{
                    TextScore(game.homeTeamScore)
                }



                Column(horizontalAlignment = Alignment.CenterHorizontally) {


                    ImageLogo(game.homeTeamlogo)

                    if (game.status != 1) {
                        TextTeamName(game.homeTeamName)
                    }
                }
            }

            if (game.status == 1) {

                Box(
                    modifier = Modifier
                        .padding(10.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.White)
                        .fillMaxWidth()
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.align(Alignment.Center)) {
                        Text(
                            text = "BUY TICKETS ON",
                            color = Color.Black,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )


                        Text(
                            text = "ticketmaster",
                            color = Color.Black,
                            fontSize = 10.sp,
                            fontStyle = FontStyle.Italic,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .padding(start = 8.dp)
                        )
                    }
                }
            }

        }
    }

}

@Composable
fun TextTeamName(name:String){
    Text(
        text = name,
        color = Color.White,
        fontSize = 14.sp,
        fontStyle = FontStyle.Italic,
        fontWeight = FontWeight.Bold
    )
}

@Composable
fun TextScore(score:String){
    Text(
        text = score,
        color = Color.White,
        fontSize = 18.sp,
        fontWeight = FontWeight.SemiBold,
        modifier = Modifier
            .padding(8.dp)
    )
}
@Composable
fun ImageLogo(url:String){
    val context = LocalContext.current
    val imageLoader = ImageLoader.Builder(context)
        .components {
            add(SvgDecoder.Factory())
        }
        .build()

    Image(
        painter = rememberAsyncImagePainter(
            model = url,
            imageLoader = imageLoader
        ),
        contentDescription = null,
        modifier = Modifier
            .padding(4.dp)
            .size(50.dp)
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun PreviewScheduleItem() {
    val sampleGame = ScheduleDisplay(
        homeTeamName = "Lakers",
        homeTeamlogo = "https://cdn.nba.com/logos/nba/1610612764/primary/L/logo.svg",
        homeTeamScore = "100",
        homeTeamCity = "Miami",
        awayTeamName = "Warriors",
        awayTeamlogo = "https://cdn.nba.com/logos/nba/1610612764/primary/L/logo.svg",
        awayTeamScore = "150",
        awayTeamCity = "Miami",
        dateTime = "2025-04-13T17:00:00.000Z",
        arenaCity = "Miami",
        cardColor = "E31837",
        awayTeamTid = "1610612748",
        status = 1
    )
    ScheduleItem(game = sampleGame)
}

@RequiresApi(Build.VERSION_CODES.O)
fun formatGameDate(input: String,outputDateFormat:String): String {
    val inputFormatter = DateTimeFormatter.ISO_ZONED_DATE_TIME
    val outputFormatter = DateTimeFormatter.ofPattern(outputDateFormat, Locale.ENGLISH)

    val date = ZonedDateTime.parse(input, inputFormatter)
    return outputFormatter.format(date).uppercase() // to match "SAT JUL 01"
}

fun showLogs(msg:String) {

    Log.e("zzz",msg)
}




