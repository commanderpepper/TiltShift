package com.commanderpepper.tiltshift

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.*
import androidx.lifecycle.lifecycleScope
import androidx.ui.core.Modifier
import androidx.ui.core.setContent
import androidx.ui.foundation.Text
import androidx.ui.graphics.Color
import androidx.ui.layout.Column
import androidx.ui.layout.Row
import androidx.ui.layout.size
import androidx.ui.material.Button
import androidx.ui.material.Surface
import androidx.ui.tooling.preview.Preview
import androidx.ui.unit.dp
import com.commanderpepper.tiltshift.ui.AppTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlin.random.Random


val colors = listOf(Color.Red, Color.Green, Color.Blue, Color.Magenta)

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val channelEvents = BroadcastChannel<Unit>(1)
        val channelEventsFlow = channelEvents.asFlow()

        setContent {
            AppTheme {
                Column {
                    ChangeColorButton(lifecycleScope) { channelEvents.send(Unit) }
                    Row{
                        repeat(16){
                            ColumnOfTiles(lifecycleScope, channelEventsFlow)
                        }
                    }
                }
            }
        }
    }


}

@Composable
fun ColumnOfTiles(coroutineScope: CoroutineScope, channelEventsFlow: Flow<Unit>) {
    Column {
        repeat(16){
            Tile(coroutineScope, colors, channelEventsFlow)
        }
    }
}

@Composable
fun ChangeColorButton(coroutineScope: CoroutineScope, changeButton: suspend () -> Unit) {
    Button(onClick = { coroutineScope.launch { changeButton.invoke() } }) {
        Text(text = "Change the colors")
    }
}

@Composable
fun Tile(
    coroutineScope: CoroutineScope,
    colors: List<Color>,
    colorEvent: Flow<Unit>,
    colorIndex: Int = Random.nextInt(0, colors.size)
) {
    val currentColorIndex = state { colorIndex }

    Surface(modifier = Modifier.size(24.dp), color = colors[currentColorIndex.value]) {
        // Listen for color events.
        colorEvent.onEach {
//            currentColorIndex.value = when {
//                currentColorIndex.value < colors.size - 1 -> currentColorIndex.value + 1
//                else -> 0
//            }
            currentColorIndex.value = Random.nextInt(0, colors.size)
        }.launchIn(coroutineScope)
    }

}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    AppTheme {
        Surface(modifier = Modifier.size(8.dp), color = Color.Red) {

        }
        Text(text = "Hello is this thing on?")
    }
}