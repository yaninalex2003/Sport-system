package ru.emkn.kotlin.sms

class Sportsman(var name: String, var surname: String) {

    lateinit var rank: String

    lateinit var team: String

    var birthday: Int = 0

    lateinit var group: String

    var number: Int = 0

    var times = mutableListOf<String>()

    var finishTime = times.last()

    var finishTimeInSeconds = timeToSeconds(finishTime)
}