package com.example.manualdi

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import android.view.animation.OvershootInterpolator
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.animation.doOnEnd
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.manualdi.appsearch.TodoAppSearchScreen
import com.example.manualdi.presentation.MainViewModel
import com.example.manualdi.ui.theme.ManualDITheme

const val TIMER_SCREEN = "timer_screen"
const val APP_SEARCH_SCREEN = "app_search_screen"

class MainActivity : ComponentActivity() {

    private val viewModel by viewModels<MainViewModel>(
        factoryProducer = {
            viewModelFactory {
                MainViewModel(ManualDIApp.appModule.authRepository)
            }
        }
    )
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen().apply {
            setKeepOnScreenCondition {
                !viewModel.abstractNetworkCallResponseChecker.value
            }
            setOnExitAnimationListener { screen ->
                val zoomX = ObjectAnimator.ofFloat(
                    screen.iconView,
                    View.SCALE_X,
                    0.4f,
                    0.0f
                )
                zoomX.interpolator = OvershootInterpolator()
                zoomX.duration = 500L
                zoomX.doOnEnd { screen.remove() }
                val zoomY = ObjectAnimator.ofFloat(
                    screen.iconView,
                    View.SCALE_Y,
                    0.4f,
                    0.0f
                )
                zoomY.interpolator = OvershootInterpolator()
                zoomY.duration = 500L
                zoomY.doOnEnd { screen.remove() }

                zoomX.start()
                zoomY.start()
            }
        }

        setContent {
            ManualDITheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = TIMER_SCREEN
                ) {
                    composable(route = TIMER_SCREEN) {
                        MainScreen(viewModel = viewModel, navController)
                    }
                    composable(route = APP_SEARCH_SCREEN) {
                        TodoAppSearchScreen(navController)
                    }
                }


            }
        }
    }
}

@Composable
fun MainScreen(
    viewModel: MainViewModel,
    navController: NavController
) {
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
        Button(
            onClick = { navController.navigate(APP_SEARCH_SCREEN) },
            modifier = Modifier.width(200.dp)
        ) {
            Text(text = "GO TO APP_SEARCH", fontSize = 14.sp, fontWeight = FontWeight.Light)
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ManualDITheme {

    }
}