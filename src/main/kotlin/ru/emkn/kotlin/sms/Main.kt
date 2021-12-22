package ru.emkn.kotlin.sms

import androidx.compose.foundation.layout.*
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVParser
import org.apache.commons.csv.CSVPrinter
import org.apache.commons.csv.CSVRecord
import java.io.File
import mu.KotlinLogging

import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState

val logger = KotlinLogging.logger {  }

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Sport Management System Yanix",
        state = rememberWindowState(width = 1200.dp, height = 760.dp)
    ) {
        val state = remember { mutableStateOf(State.Groups) }
        val ui = UI(state)
        MaterialTheme() {
            Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
                ui.navigation()
                ui.innerBody()
            }
        }
    }
    //ControlPoints("Ж14").makeFinishResults()
}