package com.example.myapplication

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.ui.theme.MyApplicationTheme
import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MyApplicationTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    AnalyticsScreen(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun AnalyticsScreen(name: String, modifier: Modifier = Modifier) {
    var event =0
    val context = LocalContext.current

    // State for holding the list of events
    val eventsState = remember { mutableStateOf<List<String>>(emptyList()) }

    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        Text(text = "Android Analytics", style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold))

        Button(
            onClick = {
                AndroidAnalytics.getInstance().startSession()
            },
            modifier = Modifier.padding(vertical = 8.dp)
        ) {
            Text(text = "Start Session")
        }

        Button(
            onClick = {
                try {
                    AndroidAnalytics.getInstance()
                        .addEvent("Event $event", mapOf("Tap $event" to "main"))
                } catch (e: Exception) {
                    Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
                }
                event++
            },
            modifier = Modifier.padding(vertical = 8.dp)
        ) {
            Text(text = "Add Event")
        }

        Button(
            onClick = {
                event = 0
                AndroidAnalytics.getInstance().endSession()
            },
            modifier = Modifier.padding(vertical = 8.dp)
        ) {
            Text(text = "End Session")
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {


        Button(
            onClick = {
                val sessionsData = AndroidAnalytics.getInstance().getSessionsData()?.map {
                    it.getSessionData().toString()
                }
                eventsState.value = sessionsData ?: emptyList()
            },
            modifier = Modifier.padding(vertical = 8.dp)
        ) {
            Text(text = "List Event")
        }
            Spacer(modifier = Modifier.width(16.dp)) // Adds 16dp space horizontally

            Text(text = "List Count: ${eventsState.value.size}")

        }

        // List view to display events
        LazyColumn {
            items(eventsState.value.size) { event ->
                Text(text = eventsState.value[event], modifier = Modifier.padding(vertical = 4.dp))
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MyApplicationTheme {
        AnalyticsScreen("Android")
    }
}