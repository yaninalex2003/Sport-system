package ru.emkn.kotlin.sms

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.unit.dp
import java.io.File
import androidx.compose.ui.graphics.Color
import org.jetbrains.skia.paragraph.TextBox

enum class State {
    Groups, Distance, Commands, Participants, Marks, QWE
}

class UI(private val state: MutableState<State>) {
    var but: String = "Ж14"
    @Composable
    fun navigation() = Row(Modifier.fillMaxWidth()) {
        Button(modifier = Modifier.width(240.dp),
            onClick = { state.value = State.Groups }) {
            Text("Список групп")
        }
        Button(modifier = Modifier.width(240.dp),
            onClick = { state.value = State.Distance }) {
            Text("Список дистанций")
        }
        Button(modifier = Modifier.width(240.dp),
            onClick = { state.value = State.Commands }) {
            Text("Список команд")
        }
        Button(modifier = Modifier.width(240.dp),
            onClick = { state.value = State.Participants }) {
            Text("Список участников")
        }
        Button(modifier = Modifier.width(240.dp),
            onClick = { state.value = State.Marks }) {
            Text("Список отметок")
        }
    }

    @Composable
    fun innerBody() = Row() {
        return when (state.value) {
            State.Groups -> groups()
            State.Distance -> dist()
            State.Commands -> comm()
            State.Participants -> partic()
            State.Marks -> marks()
            State.QWE -> groupRes(but)
        }
    }

    @Composable
    fun groups() {
        val buttons = getGroupNames()
        val stateVertical = rememberScrollState(0)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(stateVertical)
                .padding(end = 12.dp, bottom = 12.dp)
        ) {
            Column {
                for (but1 in buttons) {
                    but = but1
                    Button(modifier = Modifier.width(240.dp),
                        colors = ButtonDefaults.buttonColors(backgroundColor = Red),
                        onClick = {state.value = State.QWE}) {
                        Text(but)
                    }
                }
            }
        }
    }

    @Composable
    fun groupRes(but: String) {
        if (ControlPoints(but).files.isEmpty()){
            Text("Файл пуст")
            return
        }
        val people = ControlPoints(but).makeFinishResults()
        val stateVertical = rememberScrollState(0)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(stateVertical)
                .padding(end = 12.dp, bottom = 12.dp)
        ) {
            Column {
                for (chel in people.sportsmen) {
                    Row {
                        Text(chel.name)
                        Text(chel.surname)
                    }
                }
            }
        }
    }

    @Composable
    fun dist() = Text("Dis")

    @Composable
    fun comm() {
        val buttons = getTeamNames()
        val stateVertical = rememberScrollState(0)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(stateVertical)
                .padding(end = 12.dp, bottom = 12.dp)
        ) {
            Column {
                for (but in buttons) {
                    Button(modifier = Modifier.width(240.dp),
                        colors = ButtonDefaults.buttonColors(backgroundColor = Red),
                        onClick = { state.value = State.Commands }) {
                        Text(but)
                    }
                }
            }
        }
    }

    @Composable
    fun partic() = Text("Partic")

    @Composable
    fun marks() = Text("Marks")
}

fun getGroupNames(): List<String> {
    val ans = mutableListOf<String>()
    for (file in File("./groups").listFiles().toList()) {
        ans.add(file.name.substring(0, file.name.length - 4))
    }
    return ans
}

fun getTeamNames(): List<String> {
    val ans = mutableListOf<String>()
    for (file in File("./applications").listFiles().toList()) {
        ans.add(file.name.substring(0, file.name.length - 4))
    }
    return ans
}

/*fun getPeople(): List<Sportsman>{
    val  ans = mutableListOf<Sportsman>()
    for (file in File("./groups").listFiles().toList()){
        ans.add(file.name.substring(0,file.name.length-4))
    }
    return ans
}*/
