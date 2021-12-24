package ru.emkn.kotlin.sms

class Sportsman(var name: String, var surname: String) {

    lateinit var rank: String

    lateinit var team: String

    var birthday: Int = 0

    lateinit var groupName: String

    var number: Int = 0

    var times = mutableListOf<String>()

    lateinit var finishTime: String

    var finishTimeInSeconds: Int = 0

    var result: Int = 0
}