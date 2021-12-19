package ru.emkn.kotlin.sms

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.unit.dp

enum class State {
    Groups, Distance, Commands, Participants, Marks
}

class UI(private val state: MutableState<State>) {

    @Composable
    fun navigation() = Row(Modifier.fillMaxWidth()) {
        Button(modifier = Modifier.width(240.dp),
            onClick = {state.value = State.Groups}) {
            Text("Список групп")
        }
        Button(modifier = Modifier.width(240.dp),
            onClick = {state.value = State.Distance}) {
            Text("Список дистанций")
        }
        Button(modifier = Modifier.width(240.dp),
            onClick = {state.value = State.Commands}) {
            Text("Список команд")
        }
        Button(modifier = Modifier.width(240.dp),
            onClick = {state.value = State.Participants}) {
            Text("Список участников")
        }
        Button(modifier = Modifier.width(240.dp),
            onClick = {state.value = State.Marks}) {
            Text("Список отметок")
        }
    }
    @Composable
    fun innerBody() = Row() {
        return when(state.value) {
            State.Groups -> groups()
            State.Distance -> dist()
            State.Commands -> comm()
            State.Participants -> partic()
            State.Marks -> marks()
        }
    }
    @Composable
    fun groups() = Text("Groups")

    @Composable
    fun dist() = Text("Dis")

    @Composable
    fun comm() = Text("Comm")

    @Composable
    fun partic() = Text("Partic")

    @Composable
    fun marks() = Text("Marks")
}