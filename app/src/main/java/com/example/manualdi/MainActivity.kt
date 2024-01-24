package com.example.manualdi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.sharp.ExitToApp
import androidx.compose.material.icons.sharp.Share
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.manualdi.presentation.MainViewModel
import com.example.manualdi.ui.theme.ManualDITheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ManualDITheme {
                val viewModel = viewModel<MainViewModel>(
                    factory = viewModelFactory {
                        MainViewModel(ManualDIApp.appModule.authRepository)
                    }
                )
                val response = viewModel.response.collectAsState()
                val timer = viewModel.timer.collectAsState()

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Yellow),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Spacer(modifier = Modifier.height(100.dp))
                    Text(text = response.value, fontWeight = FontWeight.Light, fontSize = 24.sp)
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(text = timer.value, fontWeight = FontWeight.Normal, fontSize = 18.sp)
                    Spacer(modifier = Modifier.height(120.dp))
                    Button(
                        modifier = Modifier.width(200.dp),
                        onClick = { viewModel.login() }
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(imageVector = Icons.Default.ExitToApp, contentDescription = null)
                            Spacer(modifier = Modifier.width(20.dp))
                            Text(text = "Log In", fontSize = 20.sp, fontWeight = FontWeight.Light, color = Color.Black)
                        }

                    }
                }

            }
        }
    }
}

@Composable
fun BodyScreen(
    text: String = "...",
    timer: String = "00:04"
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Cyan),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = text, fontWeight = FontWeight.Light, fontSize = 24.sp)
        Spacer(modifier = Modifier.height(12.dp))
        Text(text = timer, fontWeight = FontWeight.Normal, fontSize = 18.sp)
        Spacer(modifier = Modifier.height(12.dp))
        Button(onClick = {  }, modifier = Modifier.width(200.dp)) {
            Image(imageVector = Icons.Sharp.Share, contentDescription = null)
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ManualDITheme {
        BodyScreen()
    }
}