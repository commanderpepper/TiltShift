package com.commanderpepper.tiltshift

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.Composable
import androidx.compose.state
import androidx.ui.core.setContent
import androidx.ui.foundation.Text
import androidx.ui.graphics.Color
import androidx.ui.layout.Column
import androidx.ui.material.Button
import androidx.ui.tooling.preview.Preview
import com.commanderpepper.tiltshift.ui.AppTheme

val colors = listOf(Color.Red, Color.Green, Color.Blue)

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AppTheme {
                Column {
                    Tile(colors)
                    Tile(colors, 1)
                    Tile(colors, 2)
                }
            }
        }
    }
}

@Composable
fun Tile(colors: List<Color>, startingColor : Int = 0) {

    val currentColor = state { startingColor }

    Button(
            onClick = { if (currentColor.value < colors.size - 1) currentColor.value += 1 else currentColor.value = 0 },
            backgroundColor = colors[currentColor.value]
    ) {
        Text("I'm ${colors[currentColor.value]} !")
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    AppTheme {
        Tile(colors)
    }
}