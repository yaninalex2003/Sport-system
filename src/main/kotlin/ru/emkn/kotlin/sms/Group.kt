package ru.emkn.kotlin.sms
import java.io.File

class Group(var startFile : File) {

    lateinit var name: String

    var controlPoints = mutableListOf<File>()

    var sportsmen = listOf<Sportsman>()

    var size = sportsmen.size
}