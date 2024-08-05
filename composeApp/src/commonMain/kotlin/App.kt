import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.ui.tooling.preview.Preview

data class Country(val name: String, val zone: TimeZone)


val countries = listOf(
    Country("Japan 🇯🇵", TimeZone.of("Asia/Tokyo")),
    Country("France 🇫🇷", TimeZone.of("Europe/Paris")),
    Country("Mexico 🇲🇽", TimeZone.of("America/Mexico_City")),
    Country("Indonesia 🇮🇩", TimeZone.of("Asia/Jakarta")),
    Country("Egypt 🇪🇬", TimeZone.of("Africa/Cairo")),
)

/**
 * Zero-pad a number to two digits by prepending a zero eg 1 becomes 01.
 */
fun Int.zeroPadStart() = this.toString().padStart(2, '0')

/**
 * Get the current time at a given timezone.
 * @param location The name of the location as a formatted string.
 * @param zone The timezone of the location.
 * @return Formatted string with the current time at the location.
 */
fun currentTimeAt(location: String, zone: TimeZone): String {
    val time = Clock.System.now()
    val localTime = time.toLocalDateTime(zone).time
    return "The time in $location is ${localTime.hour.zeroPadStart()}:${
        localTime.minute.zeroPadStart()
    }:${localTime.second.zeroPadStart()}"
}

@Composable
@Preview
fun App() {
    MaterialTheme {
        var showCountries by remember { mutableStateOf(false) }
        var timeAtLocation by remember { mutableStateOf("No location selected") }
        var selectedCountry by remember { mutableStateOf<Country?>(null) }

        LaunchedEffect(selectedCountry) {
            selectedCountry?.let { country ->
                while (true) {
                    timeAtLocation = currentTimeAt(country.name, country.zone)
                    delay(1000L) // Update every second
                }
            }
        }

        Row(modifier = Modifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    timeAtLocation,
                    style = TextStyle(fontSize = 20.sp),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth().align(Alignment.CenterHorizontally)
                )
                Row(modifier = Modifier.padding(start = 20.dp, top = 10.dp)) {
                    DropdownMenu(
                        expanded = showCountries,
                        onDismissRequest = { showCountries = false }
                    ) {
                        countries.forEach { country ->
                            DropdownMenuItem(
                                onClick = {
                                    selectedCountry = country
                                    showCountries = false
                                }
                            ) {
                                Text(country.name)
                            }
                        }
                    }
                }

                Button(modifier = Modifier.padding(start = 20.dp, top = 10.dp),
                    onClick = { showCountries = !showCountries }) {
                    Text("Select Location")
                }
            }
        }
    }
}
