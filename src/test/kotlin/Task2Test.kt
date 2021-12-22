import ru.emkn.kotlin.sms.*
import java.io.File
import kotlin.test.*

//Считывание файла
fun scanFile1(fileName: String): Array<String> {
    var fileArray: Array<String> = arrayOf()
    for (line in File(fileName).readLines()) {
        fileArray += line
    }
    return fileArray
}



internal class Test1 {
    @Test
    fun test1() {

        ControlPoints("Ж14").makeFinishResultsInFile()
        ControlPoints("Ж14").getTeamResults()
        val answ1 = scanFile1("./team_results/helpFile1")
        val ans1 = scanFile1("./team_results/ПАТРИОТ_result")
        for (i in 0 until maxOf(answ1.size, ans1.size)) {
            assertEquals(answ1[i], ans1[i])
        }
        val answ2 = scanFile1("./finish_results/helpFile3.csv")
        val ans2 = scanFile1("./finish_results/Ж14_result.csv")
        for (i in 0 until maxOf(answ2.size, ans2.size)) {
            assertEquals(answ2[i], ans2[i])
        }
    }

    @Test
    fun test2() {

        ControlPoints("Ж14").makeFinishResultsInFile()
        ControlPoints("Ж14").getTeamResults()

        val answ1 = scanFile1("./team_results/helpFile2")
        val ans1 = scanFile1("./team_results/ВЕЛИКИЕ ЛУКИ_result")
        for (i in 0 until maxOf(answ1.size, ans1.size)) {
            assertEquals(answ1[i], ans1[i])
        }
        val answ2 = scanFile1("./finish_results/helpFile3.csv")
        val ans2 = scanFile1("./finish_results/Ж14_result.csv")
        for (i in 0 until maxOf(answ2.size, ans2.size)) {
            assertEquals(answ2[i], ans2[i])
        }
    }
}