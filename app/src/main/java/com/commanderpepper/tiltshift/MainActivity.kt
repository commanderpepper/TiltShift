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
import androidx.ui.layout.size
import androidx.ui.material.Button
import androidx.ui.material.Surface
import androidx.ui.tooling.preview.Preview
import androidx.ui.unit.dp
import com.commanderpepper.tiltshift.ui.AppTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch


val colors = listOf(Color.Red, Color.Green, Color.Blue)

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val channelEvents = Channel<Unit>()
        
        setContent {
            AppTheme {
                Column {
                    ChangeColorButton(lifecycleScope){channelEvents.send(Unit)}
                    Tile(lifecycleScope, colors, channelEvents.receiveAsFlow(), 0)
                    Tile(lifecycleScope, colors, channelEvents.receiveAsFlow(), 1)
                    Tile(lifecycleScope, colors, channelEvents.receiveAsFlow(), 2)
                }
            }
        }
    }
}

@Composable
fun ChangeColorButton(coroutineScope: CoroutineScope, changeButton:  suspend () -> Unit){
    Button(onClick = { coroutineScope.launch { changeButton.invoke() } }) {
        Text(text = "Change the colors")
    }
}

@Composable
fun Tile(coroutineScope: CoroutineScope, colors: List<Color>, colorEvent: Flow<Unit>, colorIndex: Int) {
    val currentColorIndex = state {colorIndex}

    Surface(modifier = Modifier.size(64.dp), color = colors[currentColorIndex.value]) {
        colorEvent.onEach {
            currentColorIndex.value = when {
                currentColorIndex.value < colors.size - 1 -> currentColorIndex.value + 1
                else -> 0
            }
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