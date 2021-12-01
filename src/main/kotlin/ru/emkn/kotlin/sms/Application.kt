package ru.emkn.kotlin.sms

import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import com.github.doyaaaaaken.kotlincsv.dsl.csvWriter
import ru.emkn.kotlin.sms.timeToString
import java.io.File
import kotlin.system.exitProcess


typealias GroupName = String
typealias Participant = Map<String, String>
typealias ParticipantsList = MutableList<Participant>
typealias GroupsMap = MutableMap<GroupName, ParticipantsList>


class Application {
    val groups: GroupsMap
        get() = generateGroupsMap()

    fun generateGroupsMap(): GroupsMap {
        val dir = "./applications/"
        val applications = File(dir).listFiles()
        val groups: GroupsMap = mutableMapOf()
        applications.forEach { file ->
            csvReader().readAllWithHeader(file).forEach { app ->
                val groupname = app[app.keys.first()]!!
                val participant = app.toMutableMap()
                participant["Команда"] = file.nameWithoutExtension
                groups[groupname]?.add(participant) ?: run { groups[groupname] = mutableListOf(participant) }
            }
        }
        groups.forEach { _, v -> v.shuffle() }
        return groups
    }
    fun generateGroups(): GroupsMap {
        val list: GroupsMap = mutableMapOf()
        groups.forEach { key, value ->
            var counter = 0
            val temp: ParticipantsList = mutableListOf()
            value.forEach {
                val map = mutableMapOf("Номер" to "$counter")
                map.putAll(it)
                map["Время"] = timeToString(counter++)
                temp.add(map)
            }
            list[key] = temp
        }
//        print(list)
        return list
    }
    fun generateGroupsFiles(){
        val dir = "./groups"
        File(dir).mkdir()
        this.generateGroups().forEach { key, value ->
            val row = mutableListOf(value[0].keys.toList())
            value.forEach {
                row.add(it.values.toList())
            }
            csvWriter().writeAll(row, File("$dir/$key.csv"))
        }
    }
    fun create() {
        generateGroupsFiles()
    }
}