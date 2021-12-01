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
        val dir = "./sample-data/applications/"
        val applications = File(dir).listFiles()
        val groups: GroupsMap = mutableMapOf()
        applications.forEach { it ->
            csvReader().readAllWithHeader(it).forEach {
                val groupname = it[it.keys.first()]!!
                groups[groupname]?.add(it) ?: run { groups[groupname] = mutableListOf(it) }
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
                map.putAll(it.toMutableMap())
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
            val row = mutableListOf<List<String>>()
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