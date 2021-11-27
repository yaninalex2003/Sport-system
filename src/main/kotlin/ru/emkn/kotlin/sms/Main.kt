package ru.emkn.kotlin.sms

import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVPrinter
import java.io.File
import java.lang.StringBuilder
import java.nio.file.Paths

fun main(args: Array<String>) {
    val groups = getGroups()
    getStartProtocols(groups)
}

fun getGroups(): Map<String, List<Sportsman>> {
    val person: MutableList<Sportsman> = mutableListOf()
    val n = readLine()!!.toInt()
    for (i in 1..n) {
        val fileName = readLine()!!
        if (File(fileName).isFile) {
            val data = File(fileName)
            var strNumber = 0
            var team = ""
            for (line in data.readLines()) {
                if (strNumber == 0) {
                    team = teamName(line)
                    strNumber += 1
                } else if (strNumber == 1) strNumber += 1
                else {
                    person.add(makeSportsman(line, team))
                }
            }
        }
    }
    return person.groupBy { it.group }
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


fun makeSportsman(line: String, team: String): Sportsman {
    val splLine = line.split(",")
    val name = splLine[1]
    val surname = splLine[2]
    val newSportsman = Sportsman(name, surname)
    newSportsman.group = splLine[0]
    newSportsman.birthday = splLine[3].toInt()
    newSportsman.team = team
    newSportsman.rank = splLine[4]
    return newSportsman
}

fun getStartProtocols(groups: Map<String, List<Sportsman>>){
    groups.forEach{ it.value.shuffled()}
    for (oneGroup in groups.keys){
        val people = groups[oneGroup]
        val file = File("C:\\Users\\79068\\IdeaProjects\\oop-2021-sport-management-system-yanix\\groups_for _start\\$oneGroup")
        val writer = file.bufferedWriter()
        val csvPrinter = CSVPrinter(writer, CSVFormat.DEFAULT
            .withHeader(oneGroup, "" , "", "", "", "", ""))
        if (people != null) {
            var number = 0
            for (person in people){
                csvPrinter.printRecord(listOf(number+1,person.surname,person.name,person.rank,time(number)))
                number += 1
            }
        }
        csvPrinter.flush()
        csvPrinter.close()
    }
}
fun time(number: Int): String{
    return if (number%60<10) "${12+number/60}:0${number%60}:00"
    else "${12+number/60}:${number%60}:00"
}
