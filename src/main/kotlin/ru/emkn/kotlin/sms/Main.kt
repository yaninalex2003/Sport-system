package ru.emkn.kotlin.sms

import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVParser
import org.apache.commons.csv.CSVPrinter
import org.apache.commons.csv.CSVRecord
import java.io.File

fun main() {
    val groupForFinish = makeGroupClassForFinishResults()
    getFinishResults(groupForFinish)
    getTeamResults(groupForFinish)
}


//Создает класс группа, по стартовому протоколу группы
fun makeGroupClassForFinishResults(): Group {
    println("Введите название файла, хранящего стартовый протокол группы")
    val startFileName = readLine()!!
    val reader = File(startFileName).bufferedReader()
    val ans = Group(File(startFileName).name)
    val csvParser = CSVParser(
        reader, CSVFormat.DEFAULT
            .withFirstRecordAsHeader()
            .withIgnoreHeaderCase()
            .withTrim()
    )
    csvParser.forEach {
        ans.sportsmen.add(makeSportsmanFromStartProtocol(it))
        ans.size += 1
    }

    println("Для каждого участника введите название файла содержащего данные о прохождении контрольных пунктов")
    for (i in 1..ans.size) {
        val fileName = readLine()!!
        ans.controlPoints.add(File(fileName))
    }
    return ans
}

//Создает спортсмена из строчки в стартовом протоколе
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

//Создает файл с финишным результатами группы
fun getFinishResults(finish: Group) {
    for (file in finish.controlPoints) {
        val reader = file.bufferedReader()
        val numberOfPerson = file.bufferedReader().readLine().split(",")[0].toInt()
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
    val file =
        File("./finish_results\\group_${finish.name}")
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

//Вроде должна создавать файлы с результатами команд
fun getTeamResults(finish: Group) {
    val winnerTime = timeToSeconds(finish.sportsmen.sortedBy { timeToSeconds(it.finishTime) }[0].finishTime)
    val result = finish.sportsmen.groupBy { it.team }
    for (i in result.keys) {
        val file =
            File("./team_results\\${i}_result")
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

//Вспомогательные функции времени
fun timeToSeconds(time: String): Int {
    val timeList = time.split(":")
    return timeList[0].toInt() * 3600 + timeList[1].toInt() * 60 + timeList[2].toInt()
}

fun timeToString(number: Int): String {
    return if (number % 60 < 10) "${12 + number / 60}:0${number % 60}:00"
    else "${12 + number / 60}:${number % 60}:00"
}