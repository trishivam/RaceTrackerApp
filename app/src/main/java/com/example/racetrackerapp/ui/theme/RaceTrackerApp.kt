package com.example.racetrackerapp.ui.theme

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.isTraceInProgress
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.racetrackerapp.R
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

@Composable
fun RaceTrackerApp() {

    val playerOne = remember {
        RaceParticipant( name = "Player 1", progressIncrement = 1)
    }
    val playerTwo = remember {
        RaceParticipant( name = "Player 2", progressIncrement = 2)
    }

    var raceInProgress by remember { mutableStateOf(false) }

    if (raceInProgress){
        LaunchedEffect(playerOne, playerTwo) {
            coroutineScope {
                launch { playerOne.run() }
                launch { playerTwo.run() }
            }
            raceInProgress = false
        }
    }

    RaceTrackerScreen(
        playerOne = playerOne,
        playerTwo = playerTwo,
        isRunning = raceInProgress,
        onRunStateChange = { raceInProgress = it }
    )
}

@Composable
fun RaceTrackerScreen(
    playerOne: RaceParticipant,
    playerTwo: RaceParticipant,
    isRunning: Boolean,
    onRunStateChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.run_a_race),
            style  =  MaterialTheme.typography.headlineLarge,
            )
        Column (
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ){
            Icon(
                painter = painterResource(R.drawable.walk),
                contentDescription = "Ic_Walk"
            )
            statusIndicator(
                participentName = playerOne.name,
                currentProgress = playerOne.currentProgress,
                maxProgress = playerOne.maxProgress.toString()+"%",
                progressFcator = playerOne.progressFactor
            )
            Spacer(modifier = Modifier.size(24.dp))

            statusIndicator(
                participentName = playerTwo.name,
                currentProgress = playerTwo.currentProgress,
                maxProgress = playerTwo.maxProgress.toString()+"%",
                progressFcator = playerTwo.progressFactor
            )
            Spacer(modifier = Modifier.size(24.dp))

            RaceControl(
                isRunning = isRunning,
                onRunStateChange = onRunStateChange,
                onReset = {
                    playerOne.reset()
                    playerTwo.reset()
                    onRunStateChange(false)
                }
            )

        }
    }
}
@Composable
private fun statusIndicator(
    participentName: String,
    currentProgress: Int,
    maxProgress: String,
    progressFcator: Float,
    modifier: Modifier = Modifier
) {
    Row {
        Text(text = participentName, modifier = Modifier.padding(16.dp))

        Column(
            modifier = modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            LinearProgressIndicator(
                progress = progressFcator,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(16.dp)
                    .clip(RoundedCornerShape(24.dp))
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = currentProgress.toString() ,
                    textAlign = TextAlign.Start,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = maxProgress,
                    textAlign = TextAlign.End,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun RaceControl(
    onRunStateChange: (Boolean) -> Unit,
    onReset: () -> Unit,
    modifier: Modifier = Modifier,
    isRunning: Boolean = true,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Button(onClick = { onRunStateChange(!isRunning) }) {
            Text( if (isRunning) stringResource(R.string.pause) else stringResource(R.string.start))
        }

        Button(onClick = onReset) {
            Text(stringResource(R.string.reset))
        }
    }
}

//@Preview
//@Composable
//fun RaceTrackerAppPreview() {
//    MaterialTheme {
//        RaceTrackerApp()
//    }
//}