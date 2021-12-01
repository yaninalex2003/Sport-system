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

fun main() {
    Application().create()
}

class Application {
    private val groups: GroupsMap
        get() = generateGroupsMap()

    private fun generateGroupsMap(): GroupsMap {
        /*
         * Из папки applications для каждой заявки в формате csv
         * загружается информация об участниках, разбивается на группы
         * а затем распределяется рандомным образом внутри группы
         * добавляется колонка "Команда" для дальнейшего использования
         */
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
    private fun generateGroups(): GroupsMap {
        /*
        * Здесь нумеруется участник и выдается ему время на основании номера участия
        */
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
        return list
    }
    private fun generateGroupsFiles(){
        /*
         * Создает файлы по группам вместе с заголовками в формате csv
         */
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