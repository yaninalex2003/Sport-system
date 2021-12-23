package ru.emkn.kotlin.sms

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import java.io.File
import javax.accessibility.AccessibleEditableText

enum class State {
    Groups, Distance, Commands, Participants, Marks, GroupInfo, TeamInfo, DistanceInfo
}

class UI(private val state: MutableState<State>) {
    var but = ""
    var dist = ""
    var import_var = "./applications"
    var marks_var = "./control_points"

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
            State.DistanceInfo -> distanceInfo(dist)
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
            }
        }
    }

    @Composable
    fun groupInfo(but: String) {
        if (ControlPoints(but).files.isEmpty()) {
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
                    if (element.isNotEmpty()) {
                        Button(modifier = Modifier.width(240.dp),
                            colors = ButtonDefaults.buttonColors(backgroundColor = Red),
                            onClick = {
                                state.value = State.DistanceInfo
                                dist = element
                            }) {
                            Text(element)
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun distanceInfo(dist: String) {
        val information: List<String> = scanFile1("./distances_information/${dist}").toList()
        val stateVertical = rememberScrollState(0)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(stateVertical)
                .padding(end = 12.dp, bottom = 12.dp)
        ) {
            Column {
                for (element in information) {
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(stateVertical), Arrangement.spacedBy(5.dp)
        ) {
            var text by rememberSaveable { mutableStateOf("") }
            Row(modifier = Modifier.align(Alignment.Start).height(100.dp), Arrangement.spacedBy(10.dp)) {
                TextField(
                    modifier = Modifier.fillMaxWidth(0.5f),
                    value = text,
                    onValueChange = {
                        text = it
                        import_var = it
                    },
                    label = { Text("Path to CSV or directory") }
                )
                Button(onClick = { load() }) { Text("Import") }
            }
            Row() {
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
    }

    private fun load() {
        val dir_to = "./applications"
        val target = File(this.import_var)
        this.state.value = State.Marks
        this.state.value = State.Commands
        if (target.isDirectory) {
            target.listFiles()?.forEach {
                if (it.extension == "csv") it.copyTo(File("${dir_to}/${it.name}"))
            } ?: ""
        }
        if (target.isFile && target.extension == "csv") {
            target.copyTo(File("${dir_to}/${target.name}"))
        }
    }

    @Composable
    fun teamInfo(but: String) {
        if (ControlPoints(but).files.isEmpty()) {
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
    fun partic() {
        val stateVertical = rememberScrollState(0)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(stateVertical),
                //.padding(end = 12.dp, bottom = 12.dp),
            Arrangement.spacedBy(7.dp)
        ) {
            Row {
                Text("ГРУППА", modifier = Modifier.width(200.dp))
                Text("ФАМИЛИЯ", modifier = Modifier.width(200.dp))
                Text("ИМЯ", modifier = Modifier.width(200.dp))
                Text("КОМАНДА", modifier = Modifier.width(200.dp))
                Text("РАЗРЯД", modifier = Modifier.width(200.dp))
                Text("ГОД РОЖДЕНИЯ", modifier = Modifier.width(200.dp))
            }
            table(
                allSportsmenAsListOfList()
            )
        }
    }

    @Composable
    fun marks() = Column () {
        var text by rememberSaveable { mutableStateOf("") }
        Row(modifier = Modifier.align(Alignment.Start).height(100.dp), Arrangement.spacedBy(10.dp)) {
            TextField(
                modifier = Modifier.fillMaxWidth(0.5f),
                value = text,
                onValueChange = {
                    text = it
                    marks_var = it
                },
                label = { Text("Path to CSV or directory") }
            )
            Button(onClick = { load_marks() }) { Text("Import") }
        }
        Row() {

        }
    }
    fun load_marks() {
        val root = File(marks_var)
        if (root.isDirectory) {
            root.listFiles().forEach { group ->
                if (group.isDirectory) {
                    group.listFiles()?.forEach {
                        if (it.extension == "csv") it.copyTo(File("./control_points/${group.name}/${it.name}"))
                    } ?: ""
                }
            }
        }
    }
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
fun table(matrix: List<List<Any>>) {

    @Composable
    fun generate_row(row: List<Any>) = Row(modifier = Modifier.padding(0.dp), Arrangement.spacedBy(0.dp)) {
        for (note in row) {
            if (note is String) Text(note, modifier = Modifier.width((200*3/row.size).dp)) else note
        }
    }
    return Column(modifier = Modifier.padding(0.dp), Arrangement.spacedBy(0.dp)) {
        for (row in matrix) {
            generate_row(row)
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
