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
        val group1 = Group("badminton")
        val sportsman1 = Sportsman("Ivan", "Petrov")
        sportsman1.rank = "loh"
        sportsman1.team = "loh"
        sportsman1.birthday = 1000
        sportsman1.groupName = "badminton"
        sportsman1.number = 1
        sportsman1.rank = "loh"
        sportsman1.rank = "loh"
        sportsman1.finishTime = "13:00:00"

        val sportsman2 = Sportsman("Petr", "Ivanov")
        sportsman2.rank = "loh"
        sportsman2.team = "loh"
        sportsman2.birthday = 2000
        sportsman2.groupName = "badminton"
        sportsman2.number = 1
        sportsman2.rank = "loh"
        sportsman2.rank = "loh"
        sportsman2.finishTime = "13:00:00"
        group1.sportsmen = listOf(sportsman1, sportsman2).toMutableList()
        getFinishResults(group1)
        getTeamResults(group1)
        var answ1 = scanFile1("./team_results\\helpFile1")
        var ans1 = scanFile1("./team_results\\loh_result")
        for (i in 0 until maxOf(answ1.size, ans1.size)) {
            assertEquals(answ1[i], ans1[i])
        }
    }

    fun test2() {
        val group2 = Group("tennis")
        val sportsman1 = Sportsman("Igor", "Efremov")
        sportsman1.rank = "kms"
        sportsman1.team = "loch"
        sportsman1.birthday = 1000
        sportsman1.groupName = "tennis"
        sportsman1.number = 100
        sportsman1.finishTime = "13:00:00"

        val sportsman2 = Sportsman("Lesha", "Yanin")
        sportsman2.rank = "kms"
        sportsman2.team = "loch"
        sportsman2.birthday = 2000
        sportsman2.groupName = "tennis"
        sportsman2.number = 20
        sportsman2.finishTime = "14:00:00"
        group2.sportsmen = listOf(sportsman1, sportsman2).toMutableList()
        getFinishResults(group2)
        getTeamResults(group2)

        var answ1 = scanFile1("./team_results\\helpFile2")
        var ans1 = scanFile1("./team_results\\loch_result")
        for (i in 0 until maxOf(answ1.size, ans1.size)) {
            assertEquals(answ1[i], ans1[i])
        }
    }
}