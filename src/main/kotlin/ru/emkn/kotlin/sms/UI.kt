package ru.emkn.kotlin.sms

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.unit.dp
import java.io.File

enum class State {
    Groups, Distance, Commands, Participants, Marks, GroupInfo, TeamInfo
}

class UI(private val state: MutableState<State>) {
    var but = ""

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
            State.GroupInfo -> groupInfo(but)
            State.TeamInfo -> teamInfo(but)
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
                for (but_name in buttons) {
                    Button(modifier = Modifier.width(240.dp),
                        colors = ButtonDefaults.buttonColors(backgroundColor = Red),
                        onClick = {
                            state.value = State.GroupInfo
                            but = but_name
                        }) {
                        Text(but_name)
                    }
                }
            }
        }
    }

    @Composable
    fun groupInfo(but: String) {
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
    fun dist() {
        val distances: List<String> = scanFile1("./distances.csv").toList()
        val stateVertical = rememberScrollState(0)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(stateVertical)
                .padding(end = 12.dp, bottom = 12.dp)
        ) {
            Column {
                for (element in distances) {
                    Row {
                        Text(element)
                    }
                }
            }
        }
    }

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
    fun teamInfo(but: String){
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
    fun partic() = table(
        listOf(
            listOf("A", "B"),
            listOf("B", "c")
        )
    )

    @Composable
    fun marks() = Text("Marks")
}

fun getGroupNames(): List<String> {
    val ans = mutableListOf<String>()
    for (file in File("./groups").listFiles()?.toList() ?: listOf()) {
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

fun scanFile1(fileName: String): Array<String> {
    var fileArray: Array<String> = arrayOf()
    for (line in File(fileName).readLines()) {
        fileArray += line
    }
    return fileArray
}

@Composable
fun table(matrix: List<List<String>>) {

    @Composable
    fun generate_row(row: List<String>) = Row(modifier = Modifier.padding(5.dp), Arrangement.spacedBy(5.dp)) {
        for (note in row) {
            Button(onClick = {}) { Text(note) }
        }
    }
    return Column (modifier = Modifier.padding(5.dp), Arrangement.spacedBy(3.dp)) {
        for (row in matrix) {
            run { generate_row(row) }
        }
    }
}

/*fun getPeople(): List<Sportsman>{
    val  ans = mutableListOf<Sportsman>()
    for (file in File("./groups").listFiles().toList()){
        ans.add(file.name.substring(0,file.name.length-4))
    }
    return ans
}*/
