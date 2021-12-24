package ru.emkn.kotlin.sms

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.ExperimentalGraphicsApi
import androidx.compose.ui.unit.dp
import java.io.File

enum class State {
    Groups, Distance, Commands, Participants, Marks, GroupStart, GroupFinish, TeamPeople, DistanceInfo, TeamResult
}

@ExperimentalGraphicsApi
class UI(private val state: MutableState<State>) {
    var sort_key = 0
    var but = ""
    var dist = ""
    var import_var = "./applications"
    var marks_var = "./control_points"

    @Composable
    fun navigation() = Row(Modifier.fillMaxWidth()) {
        Button(modifier = Modifier.width(240.dp),
            onClick = { state.value = State.Groups }) {
            Text("Группы")
        }

        Button(modifier = Modifier.width(240.dp),
            onClick = { state.value = State.Commands }) {
            Text("Команды")
        }

        Button(modifier = Modifier.width(240.dp),
            onClick = { state.value = State.Participants }) {
            Text("Участники")
        }

        Button(modifier = Modifier.width(240.dp),
            onClick = { state.value = State.Distance }) {
            Text("Дистанции")
        }


        Button(modifier = Modifier.width(240.dp),
            onClick = { state.value = State.Marks }) {
            Text("Отметки и результаты")
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
            State.GroupStart -> groupStart(but)
            State.DistanceInfo -> distanceInfo(dist)
            State.GroupFinish -> groupFinish(but)
            State.TeamPeople -> teamPeople(but)
            State.TeamResult -> teamResults(but)
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
                buttons.forEach {
                    Row(modifier = Modifier.fillMaxSize(), Arrangement.spacedBy(4.dp)) {
                        Text(it, modifier = Modifier.width(200.dp), color = White)
                        Button(modifier = Modifier.width(240.dp),
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = White,
                                contentColor = Color.hsv(0f, 0f, 0.13f)
                            ),
                            onClick = {
                                state.value = State.GroupStart
                                but = it
                            }) {
                            Text("Стартовый протокол")
                        }
                        Button(modifier = Modifier.width(240.dp),
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = White,
                                contentColor = Color.hsv(0f, 0f, 0.13f)
                            ),
                            onClick = {
                                state.value = State.GroupFinish
                                but = it
                            }) {
                            Text("Финишный результат")
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun groupStart(but: String) {
        if (ControlPoints(but).files.isEmpty()) {
            Text("  Файл пуст")
            return
        }
        val stateVertical = rememberScrollState(0)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(stateVertical)
                .padding(end = 12.dp, bottom = 12.dp)
        ) {
            table(
                ControlPoints(but).getGroupAsListOfList()
            )
        }
    }

    @Composable
    fun groupFinish(but: String) {
        if (ControlPoints(but).files.isEmpty()) {
            Text("Файл пуст", color = Color.hsv(0f, 0f, 0.13f))
            return
        }
        val stateVertical = rememberScrollState(0)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(stateVertical)
                .padding(end = 12.dp, bottom = 12.dp)
        ) {
            table(
                ControlPoints(but).resultInGroupAsListOfList()
            )
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
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = White,
                                contentColor = Color.hsv(0f, 0f, 0.13f)
                            ),
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
                        Text(element, color = White)
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
                    colors = TextFieldDefaults.textFieldColors(textColor = White, unfocusedLabelColor = White),
                    onValueChange = {
                        text = it
                        import_var = it
                    },
                    label = { Text("Path to CSV or directory") }
                )
                Button(
                    onClick = { load() },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = White,
                        contentColor = Color.hsv(0f, 0f, 0.13f)
                    )
                ) { Text("Import") }
            }
            Column {
                buttons.forEach {
                    Row (modifier = Modifier.fillMaxSize(), Arrangement.spacedBy(4.dp)){
                        Text("  $it", modifier = Modifier.width(200.dp), color = White)
                        Button(modifier = Modifier.width(240.dp),
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = White,
                                contentColor = Color.hsv(0f, 0f, 0.13f)
                            ),
                            onClick = {
                                state.value = State.TeamPeople
                                but = it
                            }) {
                            Text("Список команды")
                        }
                        Button(modifier = Modifier.width(240.dp),
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = White,
                                contentColor = Color.hsv(0f, 0f, 0.13f)
                            ),
                            onClick = {
                                state.value = State.TeamResult
                                but = it
                            }) {
                            Text("Результат команды")
                        }
                    }
                }
            }

        }
    }

    @Composable
    fun teamPeople(but: String) {
        val stateVertical = rememberScrollState(0)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(stateVertical)
                .padding(end = 12.dp, bottom = 12.dp),
            Arrangement.spacedBy(7.dp)
        ) {
            Row {
                Text("  ГРУППА", modifier = Modifier.width(200.dp), color = White)
                Text("  ФАМИЛИЯ", modifier = Modifier.width(200.dp), color = White)
                Text("  ИМЯ", modifier = Modifier.width(200.dp), color = White)
                Text("  ГОД РОЖДЕНИЯ", modifier = Modifier.width(200.dp), color = White)
                Text("  РАЗРЯД", modifier = Modifier.width(200.dp), color = White)
            }
            table(
                sportsmenInOneTeamAsListOfList(but)
            )
        }
    }

    @Composable
    fun teamResults(but: String) {
        val stateVertical = rememberScrollState(0)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(stateVertical)
                .padding(end = 12.dp, bottom = 12.dp),
            Arrangement.spacedBy(7.dp)
        ) {
            Text("  СУММАРНЫЙ РЕЗУЛЬТАТ = ${teamResult(but)}", color = White)
            Row {
                Text("  ГРУППА", modifier = Modifier.width(200.dp), color = White)
                Text("  ФАМИЛИЯ", modifier = Modifier.width(200.dp), color = White)
                Text("  ИМЯ", modifier = Modifier.width(200.dp), color = White)
                Text("  ГОД РОЖДЕНИЯ", modifier = Modifier.width(200.dp), color = White)
                Text("  РАЗРЯД", modifier = Modifier.width(200.dp), color = White)
                Text("  РЕЗУЛЬТАТ", modifier = Modifier.width(200.dp), color = White)
            }
            table(
                sportsmenInTeamResultsFileAsListOfList(but)
            )
        }
    }

    private fun load() {
        try {
            val target = File(this.import_var)
            this.state.value = State.Marks
            this.state.value = State.Commands
            if (target.isDirectory) {
                target.listFiles()?.forEach {
                    if (it.extension == "csv") it.copyTo(File("./applications/${it.name}"))
                } ?: ""
            }
            if (target.isFile && target.extension == "csv") {
                target.copyTo(File("./applications/${target.name}"))
            }
        } catch (e: Exception) {
            println(e.message)
        }
    }

    @Composable
    fun partic() {
        val stateVertical = rememberScrollState(0)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(stateVertical)
                .padding(end = 12.dp, bottom = 12.dp),
            Arrangement.spacedBy(7.dp)
        ) {
            Row {
                Button(
                    onClick =
                    {
                        sort_key = 0
                        state.value = State.Groups
                        state.value = State.Participants
                    }, modifier = Modifier.width(200.dp)
                ) { Text("ГРУППА", modifier = Modifier.width(200.dp), color = White) }
                Button(onClick = {
                    sort_key = 1
                    state.value = State.Groups
                    state.value = State.Participants
                }, modifier = Modifier.width(200.dp)) {
                    Text(
                        "ФАМИЛИЯ",
                        modifier = Modifier.width(200.dp),
                        color = White
                    )
                }
                Button(onClick = {
                    sort_key = 2
                    state.value = State.Groups
                    state.value = State.Participants
                }, modifier = Modifier.width(200.dp)) { Text("ИМЯ", modifier = Modifier.width(200.dp), color = White) }
                Button(onClick = {
                    sort_key = 3
                    state.value = State.Groups
                    state.value = State.Participants
                }, modifier = Modifier.width(200.dp)) {
                    Text(
                        "КОМАНДА",
                        modifier = Modifier.width(200.dp),
                        color = White
                    )
                }
                Button(onClick = {
                    sort_key = 4
                    state.value = State.Groups
                    state.value = State.Participants
                }, modifier = Modifier.width(200.dp)) {
                    Text(
                        "РАЗРЯД",
                        modifier = Modifier.width(200.dp),
                        color = White
                    )
                }
                Button(onClick = {
                    sort_key = 5
                    state.value = State.Groups
                    state.value = State.Participants
                }, modifier = Modifier.width(200.dp)) {
                    Text(
                        "ГОД РОЖДЕНИЯ",
                        modifier = Modifier.width(200.dp),
                        color = White
                    )
                }
            }
            table(
                allSportsmenAsListOfList().sortedBy { it[sort_key] }
            )
        }
    }

    @Composable
    fun marks() = Box {
        val res = getGroupNames()
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
                    colors = TextFieldDefaults.textFieldColors(textColor = White, unfocusedLabelColor = White),
                    onValueChange = {
                        text = it
                        marks_var = it
                    },
                    label = { Text("Path to directory") }
                )
                Button(onClick = { load_marks() }) { Text("Import", color = White) }
                Button(onClick = {
                    val files = File("./team_results").listFiles()?.toList() ?: listOf()
                    files.forEach{
                        it.writeText("")
                    }
                    res.forEach {
                        ControlPoints(it).makeFinishResultsInFile()
                        ControlPoints(it).getTeamResults()
                    }
                    state.value = State.Groups
                }) { Text("Results!", color = White) }
            }
//            Row() {
//                Column {
//                    for (but in buttons) {
//                        Button(modifier = Modifier.width(240.dp),
//                            colors = ButtonDefaults.buttonColors(backgroundColor = Red),
//                            onClick = { state.value = State.Commands }) {
//                            Text(but)
//                        }
//                    }
//                }
//            }
        }
    }

    private fun load_marks() {
        try {
            val target = File(this.marks_var)
            this.state.value = State.Commands
            this.state.value = State.Marks
            if (target.isDirectory) {
                target.listFiles()?.forEach { group ->
                    if (group.isDirectory) {
                        group.listFiles().forEach {
                            if (it.extension == "csv") it.copyTo(File("./control_points/${group.name}/${it.name}"))
                        }
                    }
                } ?: ""
            }
        } catch (e: Exception) {
            println(e.message)
        }
    }
}

@OptIn(ExperimentalGraphicsApi::class)
@Composable
fun table(matrix: List<List<String>>) {

    @Composable
    fun generate_row(row: List<String>) = Row(modifier = Modifier.padding(0.dp), Arrangement.spacedBy(0.dp)) {
        for (note in row) {
            Text("  $note", modifier = Modifier.width(200.dp), color = White)
        }
    }
    return Column(modifier = Modifier.padding(0.dp), Arrangement.spacedBy(0.dp)) {
        for (row in matrix) {
            run { generate_row(row) }
        }
    }
}
