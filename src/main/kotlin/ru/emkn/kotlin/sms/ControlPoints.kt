package ru.emkn.kotlin.sms

import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import com.github.doyaaaaaken.kotlincsv.dsl.csvWriter
import org.jetbrains.skia.paragraph.Direction
import java.io.File

class ControlPoints(val groupname: String) {

    val direct = File(groupname).listFiles()


}