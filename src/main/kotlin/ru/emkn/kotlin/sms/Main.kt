package ru.emkn.kotlin.sms

import java.io.File
import java.lang.StringBuilder

fun main(args: Array<String>) {
    var groups = getGroups()
}

fun getGroups(): MutableMap<String, MutableList<Sportsman>> {
    val groups: MutableMap<String, MutableList<Sportsman>> = mutableMapOf()
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
                    val person = makeSportsman(line, team)
                    groups[person.group]?.add(person)
                }
            }
        }
    }
    return groups
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
