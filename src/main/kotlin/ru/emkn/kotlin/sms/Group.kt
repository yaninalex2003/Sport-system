package ru.emkn.kotlin.sms
import java.io.File

class Group(var name : String) {

    var controlPoints = listOf<File>()

    var sportsmen = mutableListOf<Sportsman>()

    var size: Int = 0
}