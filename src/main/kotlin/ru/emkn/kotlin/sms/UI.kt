package ru.emkn.kotlin.sms

import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.unit.dp
import java.io.File
import androidx.compose.ui.graphics.Color

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
    fun groups() {
        val buttons = getGroupNames()
        Column(Modifier.fillMaxWidth()) {
            for (but in buttons){
                Button(modifier = Modifier.width(240.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Red),
                    onClick = { state.value = State.Groups }) {
                    Text(but)
                }
            }
        }
        /*VerticalScrollbar(
            modifier = Modifier.align(Alignment.CenterEnd).fillMaxHeight(),
            adapter = rememberScrollbarAdapter(
                scrollState = state
            )
        )*/
    }

    @Composable
    fun dist() = Text("Dis")

    @Composable
    fun comm() = Text("Comm")

    @Composable
    fun partic() = Text("Partic")

    @Composable
    fun marks() = Text("Marks")
}

fun getGroupNames(): List<String>{
    val  ans = mutableListOf<String>()
    for (file in File("C:\\Users\\79068\\IdeaProjects\\oop-2021-sport-management-system-yanix\\groups").listFiles().toList()){
        ans.add(file.name.substring(0,file.name.length-4))
    }
    return ans
}