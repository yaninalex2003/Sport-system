package ru.emkn.kotlin.sms

import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import com.github.doyaaaaaken.kotlincsv.dsl.csvWriter
import java.io.File
import kotlin.random.Random


typealias GroupName = String
typealias Participant = Map<String, String>
typealias ParticipantsList = MutableList<Participant>
typealias GroupsMap = MutableMap<GroupName, ParticipantsList>


class Application(
    val appDir: String = "./applications",
    val groupsDir: String = "./groups",
    val seed: Random = Random
) {
    private val groups: GroupsMap
        get() = generateGroupsMap()

    private fun generateGroupsMap(): GroupsMap {
        /*
         * Из папки applications для каждой заявки в формате csv
         * загружается информация об участниках, разбивается на группы
         * а затем распределяется рандомным образом внутри группы
         * добавляется колонка "Команда" для дальнейшего использования
         */
        val applications = File(appDir).listFiles()
        val groups: GroupsMap = mutableMapOf()
        applications.forEach { file ->
            csvReader().readAllWithHeader(file).forEach { app ->
                val groupname = app[app.keys.first()]!!
                val participant = app.toMutableMap()
                participant["Команда"] = file.nameWithoutExtension
                groups[groupname]?.add(participant) ?: run { groups[groupname] = mutableListOf(participant) }
            }
        }
        groups.forEach { _, v -> v.shuffle(seed) }
        return groups
    }
    private fun generateGroups(): GroupsMap {
        /*
        * Здесь нумеруется участник и выдается ему время на основании номера участия
        */
        val list: GroupsMap = mutableMapOf()
        groups.forEach { key, value ->
            var counter = 1
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
        File(groupsDir).mkdir()
        this.generateGroups().forEach { key, value ->
            val row = mutableListOf(value[0].keys.toList())
            value.forEach {
                row.add(it.values.toList())
            }
            csvWriter().writeAll(row, File("$groupsDir/$key.csv"))
        }
    }
    fun create() {
        generateGroupsFiles()
    }
}