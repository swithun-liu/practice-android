package compose.project.demo

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

import composedemo.composeapp.generated.resources.Res
import composedemo.composeapp.generated.resources.compose_multiplatform
import composedemo.composeapp.generated.resources.eg
import kotlinx.datetime.Clock
import kotlinx.datetime.IllegalTimeZoneException
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.resources.DrawableResource

@Composable
@Preview
fun App() {
    MaterialTheme {
        val navigationController = rememberNavController()
        NavHost(
            navController = navigationController,
            startDestination ="first_screen",
            enterTransition = { slideIn(initialOffset = { IntOffset(it.width, 0) }) },
            exitTransition = { slideOut(targetOffset = { IntOffset(0, 0) } ) }
        ) {
            composable("first_screen") {
                FirstScreen(navigationController)
            }
            composable("second_screen") {
                SecondScreen(navigationController)
            }
            composable("text_field_test") {
                TextFieldTest()
            }
            composable("text_android_view") {
                TextFieldTest()
            }
        }
    }
}

@Composable
fun FirstScreen(
    navigationController: NavHostController,
    viewModel: FirstScreenViewModel = viewModel { FirstScreenViewModel() }
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Top) {
        Button(onClick = { viewModel.changeIsShowContent() }) {
            Text("Click me!")
        }
        Button(onClick = {
            navigationController.navigate("second_screen")
        }) {
            Text("nav to 2")
        }
        Button(onClick = {
            navigationController.navigate("text_field_test")
        }) {
            Text("nav to TextField")
        }
        TextField(value = uiState.value.location, onValueChange = { viewModel.changeLocation(it) })
        AnimatedVisibility(uiState.value.showContent) {
            val greeting = remember { Greeting().greet() }
            Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                Image(painterResource(Res.drawable.compose_multiplatform), null)
                Text("Compose: $greeting")
            }
        }
    }
}

@Composable
fun SecondScreen(navigationController: NavHostController) {
    var showContent by remember { mutableStateOf(false) }
    var locaton by remember { mutableStateOf("Europe/Paris") }
    var timeAtLocation by remember { mutableStateOf("No location selected") }
    var showCountries by remember { mutableStateOf(false) }
    Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        Text(timeAtLocation)
        TextField(value = locaton, onValueChange = { locaton = it })
        Button(onClick = { timeAtLocation = currentTimeAt(locaton, TimeZone.currentSystemDefault()) ?: "Invalid Location" }) {
            Text("Show Time At Location")
        }
        Button(onClick = { navigationController.popBackStack() }) {
            Text("Back")
        }
        Button(modifier = Modifier.padding(start = 20.dp, top = 10.dp),
            onClick = { showCountries = !showCountries }) {
            Text("Select Location")
        }
        Row(modifier = Modifier.padding(start = 20.dp)) {
            DropdownMenu(
                expanded = showCountries,
                onDismissRequest = { showCountries = false }
            ) {
                countries().forEach { (name, zone, image) ->
                    DropdownMenuItem(
                        onClick = {
                            timeAtLocation = currentTimeAt(name, zone)
                            showCountries = false
                        }
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Image(
                                painterResource(image),
                                modifier = Modifier.size(50.dp).padding(end = 10.dp),
                                contentDescription = "$name flag"
                            )
                            Text(name)
                        }
                    }
                }
            }
        }
        Text(
            text = "Today's date is ${todaysDate()}",
            modifier = Modifier.padding(20.dp),
            fontSize = 24.sp,
            textAlign = TextAlign.Center
        )
        Button(onClick = { showContent = !showContent }) {
            Text("Click me!")
        }
        AnimatedVisibility(showContent) {
            val greeting = remember { Greeting().greet() }
            Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                Image(painterResource(Res.drawable.compose_multiplatform), null)
                Text("Compose: $greeting")
            }
        }
    }
}


data class Country(val name: String, val zone: TimeZone, val image: DrawableResource)

fun countries() = listOf(
    Country("Japan", TimeZone.of("Asia/Tokyo"), Res.drawable.eg),
    Country("France", TimeZone.of("Europe/Paris"), Res.drawable.eg),
    Country("Mexico", TimeZone.of("America/Mexico_City"), Res.drawable.eg),
    Country("Indonesia", TimeZone.of("Asia/Jakarta"), Res.drawable.eg),
    Country("Egypt", TimeZone.of("Africa/Cairo"), Res.drawable.eg),
)

fun todaysDate(): String {
    fun LocalDateTime.format() = toString().substringBefore('T')

    val now = Clock.System.now()
    val zone = TimeZone.currentSystemDefault()
    return now.toLocalDateTime(zone).format()
}

fun currentTimeAt(location: String, zone: TimeZone): String {
    fun LocalTime.formatted() = "$hour:$minute:$second"

    return try {
        val time = Clock.System.now()
//        val zone = TimeZone.of(location)
        val localTime = time.toLocalDateTime(zone).time
        "The time in $location is ${localTime.formatted()}"
    } catch (ex: IllegalTimeZoneException) {
        "Invalidate"
    }
}