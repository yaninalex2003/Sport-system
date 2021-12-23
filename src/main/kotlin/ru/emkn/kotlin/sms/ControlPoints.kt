package ru.emkn.kotlin.sms

import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import com.github.doyaaaaaken.kotlincsv.dsl.csvWriter
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVParser
import org.apache.commons.csv.CSVPrinter
import org.apache.commons.csv.CSVRecord
import org.jetbrains.skia.paragraph.Direction
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
        val finish = getGroup()
        for (file in files){
            val reader = file.bufferedReader()
            val numberOfPerson = reader.readLine().split(",")[0].toInt()
            val csvParser = CSVParser(
                reader, CSVFormat.DEFAULT
                    .withIgnoreHeaderCase()
                    .withTrim()
            )
            val person = finish.sportsmen.find { it.number == numberOfPerson }!!
            csvParser.forEach {
                person.times.add(it.get(1))
            }
            person.finishTime = person.times.last()
            person.finishTimeInSeconds = timeToSeconds(person.finishTime)
        }

        finish.sportsmen = finish.sportsmen.sortedBy { it.finishTimeInSeconds }.toMutableList()
        val winnerTime = timeToSeconds(finish.sportsmen.sortedBy { timeToSeconds(it.finishTime) }[0].finishTime)
        val result = finish.sportsmen.groupBy { it.team }
        for (i in result.keys) {
            val file =
                File("./team_results/${i}_result")
            val writer = file.bufferedWriter()
            val csvPrinter = CSVPrinter(
                writer, CSVFormat.DEFAULT
                    .withHeader(i, "", "", "", "", "", "")
            )
            var number = 1
            for (person in result[i]!!) {
                csvPrinter.printRecord(
                    listOf(
                        number,
                        person.number,
                        person.surname,
                        person.name,
                        person.rank,
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

//Вспомогательные функции времени
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
    newSportsman.rank = line.get(4)
    newSportsman.groupName = line.get(0)
    return newSportsman
}

fun makeListSprotsmen(nameFile: String): List<Sportsman>{
    val file = File("./application/${nameFile}.csv")
    val reader = file.bufferedReader()
    val numberOfPerson = reader.readLine().split(",")[0].toInt()
    val csvParser = CSVParser(
        reader, CSVFormat.DEFAULT
            .withTrim()
    )
    val ans = mutableListOf<Sportsman>()
    csvParser.forEach {
        ans.add(makeSportsmanFromTeamList(it, nameFile))
    }
    return ans
}
