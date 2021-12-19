package ru.emkn.kotlin.sms

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

class UI(private val state: MutableState<Int>) {

    @Composable
    fun navigation() = Row(Modifier.fillMaxWidth()) {
        Button(modifier = Modifier.width(240.dp),
            onClick = {state.value = 0}) {
            Text("Список групп")
        }
        Button(modifier = Modifier.width(240.dp),
            onClick = {state.value = 1}) {
            Text("Список дистанций")
        }
        Button(modifier = Modifier.width(240.dp),
            onClick = {state.value = 2}) {
            Text("Список команд")
        }
        Button(modifier = Modifier.width(240.dp),
            onClick = {}) {
            Text("Список участников")
        }
        Button(modifier = Modifier.width(240.dp),
            onClick = {}) {
            Text("Список отметок")
        }
    }
    @Composable
    fun innerBody() = Row() {
        return when(state.value) {
            0 -> groups()
            1 -> dist()
            else -> Text("state != 1 or state != 0")
        }
    }
    @Composable
    fun groups() = Text("Groups")

    @Composable
    fun dist() = Text("Dis")

}