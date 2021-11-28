package ru.emkn.kotlin.sms

import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVParser
import org.apache.commons.csv.CSVPrinter
import org.apache.commons.csv.CSVRecord
import java.io.File
import java.lang.StringBuilder

fun main(args: Array<String>) {
    val files = readData()
    val people = getGroup(files)
    val groups = people.groupBy { it.group }
    getStartProtocols(groups)

    val groupForFinish = makeGroupClassForFinishResults(groups)
    getFinishResults(groupForFinish)
}

fun readData(): List<File> {
    val ans = mutableListOf<File>()
    println("Введите количество спортивных коллективов")
    val amountOfFiles = readLine()!!.toInt()
    println("Введите ссылки на файлы, хранящие данные о коллективах")
    repeat(amountOfFiles) {
        val fileName = readLine()!!
        ans.add(File(fileName))
    }
    return ans
}

fun getGroup(files: List<File>): List<Sportsman> {
    val people: MutableList<Sportsman> = mutableListOf()
    for (file in files) {
        val reader = file.bufferedReader()
        val team = teamName(reader.readLine())
        val csvParser = CSVParser(
            reader, CSVFormat.DEFAULT
                .withIgnoreHeaderCase()
                .withTrim()
        )
        csvParser.forEach {
            people.add(makeSportsman(it, team))
        }
    }
    return people
}


fun teamName(line: String): String {
    val splLine = line.split(",")
    splLine.filter { it.isNotEmpty() }
    val ans = StringBuilder()
    for (i in 1 until splLine.size) {
        ans.append(splLine[i])
        ans.append(",")
    }
    ans.append(splLine.last())
    return ans.toString()
}

fun makeSportsman(line: CSVRecord, team: String): Sportsman {
    val name = line.get(1)
    val surname = line.get(2)
    val newSportsman = Sportsman(name, surname)
    newSportsman.group = line.get(0)
    newSportsman.birthday = line.get(3).toInt()
    newSportsman.team = team
    newSportsman.rank = line.get(4)
    return newSportsman
}

fun getStartProtocols(groups: Map<String, List<Sportsman>>) {
    groups.forEach { it.value.shuffled() }
    for (oneGroup in groups.keys) {
        val people = groups[oneGroup]!!
        val file =
            File("C:\\Users\\79068\\IdeaProjects\\oop-2021-sport-management-system-yanix\\groups_for _start\\$oneGroup")
        val writer = file.bufferedWriter()
        val csvPrinter = CSVPrinter(
            writer, CSVFormat.DEFAULT
                .withHeader(oneGroup, "", "", "", "", "", "")
        )
        var number = 0
        for (person in people) {
            person.number = number + 1
            csvPrinter.printRecord(listOf(person.number, person.surname, person.name, person.rank, timeToString(number)))
            number += 1
        }
        csvPrinter.flush()
        csvPrinter.close()
    }
}

fun makeGroupClassForFinishResults(groups: Map<String, List<Sportsman>>): Group {
    println("Введите название файла, хранящего стартовый протокол группы")
    val startFileName = readLine()!!
    val ans = Group(File(startFileName))
    val reader = File(startFileName).bufferedReader()
    ans.name = reader.readLine().split(",")[0]
    var a = groups[ans.name]?.groupBy { it.number }
    ans.sportsmen = groups[ans.name]!!

    println("Для каждого участника введите название файла содержащего данные о прохождении контрольных пунктов")
    for (i in 1..ans.size) {
        val fileName = readLine()!!
        ans.controlPoints.add(File(fileName))
    }
    return ans
}

fun getFinishResults(finish: Group) {
    for (file in finish.controlPoints) {
        val reader = file.bufferedReader()
        val csvParser = CSVParser(
            reader, CSVFormat.DEFAULT
                .withIgnoreHeaderCase()
                .withTrim()
        )
        val numberOfPerson = csvParser.headerNames[0].toInt()
        val person = finish.sportsmen.find { it.number == numberOfPerson }!!
        csvParser.forEach {
            person.times.add(it.get(1))
        }
    }
    finish.sportsmen.sortedBy { it.finishTimeInSeconds }.reversed()
    val file =
        File("C:\\Users\\79068\\IdeaProjects\\oop-2021-sport-management-system-yanix\\finish_results\\group_${finish.name}")
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
                person.finishTime
            )
        )
        number += 1
    }
    csvPrinter.flush()
    csvPrinter.close()
}

fun timeToSeconds(time: String): Int {
    val timeList = time.split(":")
    return timeList[0].toInt() * 3600 + timeList[1].toInt() * 60 + timeList[2].toInt()
}

fun timeToString(number: Int): String {
    return if (number % 60 < 10) "${12 + number / 60}:0${number % 60}:00"
    else "${12 + number / 60}:${number % 60}:00"
}