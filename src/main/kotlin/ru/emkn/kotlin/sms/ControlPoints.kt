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
    val files: List<File> = File("C:\\Users\\79068\\IdeaProjects\\oop-2021-sport-management-system-yanix\\control_points\\${groupname}").listFiles().toList()

    fun getGroup(): Group{
        val groupFile = File("C:\\Users\\79068\\IdeaProjects\\oop-2021-sport-management-system-yanix\\groups\\${groupname}.csv")

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

    fun makeFinishResults(){
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
        val file =
            File(".\\finish_results\\${finish.name}_result.csv")
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