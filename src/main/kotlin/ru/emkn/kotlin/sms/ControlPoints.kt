package ru.emkn.kotlin.sms

import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVParser
import org.apache.commons.csv.CSVPrinter
import org.apache.commons.csv.CSVRecord
import java.io.File

class ControlPoints(val groupname: String) {
    val files: List<File>
        get() = run {
            val dir = File("./control_points/${groupname}")
            if (dir.isDirectory) {
                return dir.listFiles()?.toList() ?: listOf()
            } else {
                return listOf()
            }
        }


    fun getGroup(): Group {
        val groupFile =
            File("./groups/${groupname}.csv")

        val ans = Group(groupname)
        val csvParser = CSVParser(
            groupFile.bufferedReader(), CSVFormat.DEFAULT
                .withFirstRecordAsHeader()
                .withIgnoreHeaderCase()
                .withTrim()
        )
        csvParser.forEach {
            ans.sportsmen.add(makeSportsmanFromStartProtocol(it))
            ans.size += 1
        }
        ans.controlPoints = files
        return ans
    }

    fun getGroupAsListOfList(): List<List<String>>{
        val group = getGroup()
        val ans = mutableListOf<List<String>>()
        group.sportsmen.forEach{
            ans.add(listOf(it.number.toString(), it.name, it.surname, it.team))
        }
        return ans
    }

    fun makeFinishResults(): Group {
        val finish = getGroup()
        for (file in files) {
            val reader = file.bufferedReader()
            val numberOfPerson = reader.readLine().split(",")[0].toInt()
            val csvParser = CSVParser(
                reader, CSVFormat.DEFAULT
                    .withIgnoreHeaderCase()
                    .withTrim()
            )
            val person = finish.sportsmen.find { it.number == numberOfPerson } ?: Sportsman("five", "")
            csvParser.forEach {
                person.times.add(it.get(1))
            }
            person.finishTime = person.times.last()
            person.finishTimeInSeconds = timeToSeconds(person.finishTime)
        }

        finish.sportsmen = finish.sportsmen.sortedBy { it.finishTimeInSeconds }.toMutableList()

        return finish
    }

    fun resultInGroupAsListOfList(): List<List<String>>{
        val people = makeFinishResults().sportsmen
        val ans = mutableListOf<List<String>>()
        var place = 0
        people.forEach {
            place +=1
            ans.add(listOf(place.toString(), it.name, it.surname, it.team, it.finishTime))
        }
        return ans
    }

    fun  makeFinishResultsInFile() {
        val finish = makeFinishResults()
        val file =
            File("./finish_results/${finish.name}_result.csv")
        val writer = file.bufferedWriter()
        val csvPrinter = CSVPrinter(
            writer, CSVFormat.DEFAULT
                .withHeader(finish.name, "", "", "", "", "", "")
        )
        var number = 1
        for (person in finish.sportsmen) {
            csvPrinter.printRecord(
                listOf(
                    number,
                    person.number,
                    person.surname,
                    person.name,
                    person.rank,
                    person.team,
                    person.finishTime
                )
            )
            number += 1
        }
        csvPrinter.flush()
        csvPrinter.close()
    }

    fun getTeamResults() {
        val finish = makeFinishResults()
        val winnerTime = timeToSeconds(finish.sportsmen[0].finishTime)
        val result = finish.sportsmen.groupBy { it.team }
        for (key in result.keys) {
            val star:MutableList<String> = scanFile1("./team_results/${key}_result").toMutableList()
            if (star.size==0){
                star.add("0")
            }
            val list: MutableList<String> = star[0].split(",").toList().toMutableList()
            for(person in result[key]!!){
                if (list[list.size-1]==""){
                    list[list.size-1]="0"
                }
                list[list.size-1] = (list[list.size-1].toInt()+maxOf(0, (100 * (2F - timeToSeconds(person.finishTime) / winnerTime.toFloat())).toInt())).toString()
            }
            val file =
                File("./team_results/${key}_result")
            val writer = file.bufferedWriter()
            val csvPrinter = CSVPrinter(
                writer, CSVFormat.DEFAULT
                    .withHeader(list[list.size-1])
            )
            var number = 1
            for (element in 1 until star.size){
                val str=star[element].split(",").toList()
                csvPrinter.printRecord(str)
            }

            for (person in result[key]!!) {
                csvPrinter.printRecord(
                    listOf(
                        person.groupName,
                        person.surname,
                        person.name,
                        person.rank,
                        person.birthday,
                        maxOf(0, (100 * (2F - timeToSeconds(person.finishTime) / winnerTime.toFloat())).toInt())
                    )
                )
                number += 1
            }
            csvPrinter.flush()
            csvPrinter.close()
        }
    }
}

fun makeSportsmanFromStartProtocol(line: CSVRecord): Sportsman {
    val name = line.get(2)
    val surname = line.get(3)
    val newSportsman = Sportsman(name, surname)
    newSportsman.number = line.get(0).toInt()
    newSportsman.birthday = line.get(4).toInt()
    newSportsman.rank = line.get(5)
    newSportsman.team = line.get(6)
    newSportsman.groupName = line.get(1)
    return newSportsman
}

//?????????????????????????????? ?????????????? ??????????????
fun timeToSeconds(time: String): Int {
    val timeList = time.split(":")
    return timeList[0].toInt() * 3600 + timeList[1].toInt() * 60 + timeList[2].toInt()
}

fun timeToString(number: Int): String {
    return if (number % 60 < 10) "${12 + number / 60}:0${number % 60}:00"
    else "${12 + number / 60}:${number % 60}:00"
}

fun makeSportsmanFromTeamList(line: CSVRecord, teamname: String): Sportsman {
    val name = line.get(1)
    val surname = line.get(2)
    val newSportsman = Sportsman(name, surname)
    newSportsman.team = teamname
    newSportsman.birthday = line.get(3).toInt()
    newSportsman.rank = line.get(4).ifEmpty { "-" }
    newSportsman.groupName = line.get(0)
    return newSportsman
}

fun makeListSprotsmen(nameFile: String): List<Sportsman>{
    val file = File("./applications/${nameFile}")
    val reader = file.bufferedReader()
    val csvParser = CSVParser(
        reader, CSVFormat.DEFAULT
            .withIgnoreHeaderCase()
            .withFirstRecordAsHeader()
            .withTrim()
    )
    val ans = mutableListOf<Sportsman>()
    csvParser.forEach {
        ans.add(makeSportsmanFromTeamList(it, nameFile.substring(0, nameFile.length-4)))
    }
    return ans
}

fun sportsmenInOneTeamAsListOfList(name: String): List<List<String>>{
    val person = makeListSprotsmen("$name.csv")
    val ans = mutableListOf<List<String>>()
    person.forEach {
        ans.add(listOf(it.groupName, it.name, it.surname, it.birthday.toString(), it.rank))
    }
    return ans
}

fun allSportsmen(): List<Sportsman>{
    val files = File("./applications").listFiles()?.toList() ?: listOf()
    val ans = mutableListOf<Sportsman>()
    files.forEach {
        ans += makeListSprotsmen(it.name)
    }
    return ans
}

fun allSportsmenAsListOfList(): List<List<String>>{
    val people = allSportsmen()
    val ans = mutableListOf<List<String>>()
    people.forEach {
        ans.add(listOf(it.groupName, it.name, it.surname, it.team, it.rank, it.birthday.toString()))
    }
    return ans
}

fun makeSportsmanFromTeamResult(line: CSVRecord): Sportsman{
    val name = line.get(1)
    val surname = line.get(2)
    val newSportsman = Sportsman(name, surname)
    newSportsman.birthday = line.get(4).toInt()
    newSportsman.rank = line.get(3).ifEmpty { "-" }
    newSportsman.groupName = line.get(0)
    newSportsman.result = line.get(5).toInt()
    return newSportsman
}

fun sportsmenInTeamResultsFileAsListOfList(name: String): List<List<String>>{
    val file = File("./team_results/${name}_result")
    val reader = file.bufferedReader()
    val csvParser = CSVParser(
        reader, CSVFormat.DEFAULT
            .withIgnoreHeaderCase()
            .withFirstRecordAsHeader()
            .withTrim()
    )
    val ans = mutableListOf<List<String>>()
    csvParser.forEach {
        val person = makeSportsmanFromTeamResult(it)
        ans.add(listOf(person.groupName, person.surname, person.name, person.birthday.toString(), person.rank, person.result.toString()))
    }
    return ans
}

fun teamResult(fileName: String) = File("./team_results/${fileName}_result").readLines()[0]

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

fun scanFile1(fileName: String): List<String> {
    val fileList: MutableList<String> = mutableListOf()
    for (line in File(fileName).readLines()) {
        fileList.add(line)
    }
    return fileList
}







