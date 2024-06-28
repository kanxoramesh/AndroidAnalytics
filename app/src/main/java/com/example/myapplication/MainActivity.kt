package com.example.myapplication

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myapplication.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MyApplicationTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    val context = LocalContext.current

    var event = 0
    Column {
        Text(text = "Android Analytics")
        Button(
            onClick = {
                AndroidAnalytics.getInstance().startSession()
            },
            modifier = androidx.compose.ui.Modifier.padding(8.dp)
        ) {
            Text(text = "Start Session")
        }

        Button(
            onClick = {
                AndroidAnalytics.getInstance()
                    .addEvent("Event $event", mapOf("Tap $event" to "main"))
                event++
            },
            modifier = androidx.compose.ui.Modifier.padding(8.dp)
        ) {
            Text(text = "Add Event")
        }
        Button(
            onClick = {
                event=0
                AndroidAnalytics.getInstance().endSession()

            },
            modifier = androidx.compose.ui.Modifier.padding(8.dp)
        ) {
            Text(text = "End Session")
        }
        Button(
            onClick = {
                AndroidAnalytics.getInstance().getSessionsData()?.map {
                    it.toString()
                }

            },
            modifier = androidx.compose.ui.Modifier.padding(8.dp)
        ) {
            Text(text = "List Event")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MyApplicationTheme {
        Greeting("Android")
    }
}