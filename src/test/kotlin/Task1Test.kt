import ru.emkn.kotlin.sms.Application
import java.io.File
import java.nio.file.Path
import kotlin.io.path.createTempDirectory
import kotlin.test.*

internal class Task1Test {

    fun createTmp(): Path {
        val velikieLuki = """Группа,Фамилия,Имя,Г.р.,Разр.
М12,СЕРГЕЕНКОВ,АРТЕМ,2010,
М14,ГЕЙ,КОНСТАНТИН,2007,
М14,ЛОБАНОВ,СТЕПАН,2007,
М14,БЫСТРОВ,ЕГОР,2007,
М16,ВИНОГРАДОВ,АРТЕМ,2005,
М16,ЯКОВЛЕВ,ИВАН,2005,
М16,ПИСКУНОВ,ВЛАДИМИР,2005,
М18,ПИВОВАРОВ,ВЛАДИСЛАВ,2004,
М18,БЕЛОВ,АНДРЕЙ,2004,
М18,ОРЛОВ,ВАДИМ,2003,1р
М21,МЕЛЬНИКОВ,ИВАН,1986,1р
М21,БУРЫЛИН,АНТОН,1979,1р
М40,ОРЛОВ,ЮРИЙ,1972,
М40,УЛЬЯНОВ,ПАВЕЛ,1976,3р
Ж12,АНТОНОВА,ВАСИЛИСА,2010,
Ж12,ЛИНКЕВИЧ,АЛИСА,2010,
Ж12,ПОДСТЕЖОНОК,ПОЛИНА,2009,
Ж12,БУБНОВА,КСЕНИЯ,2009,
Ж12,САФАРЯН,НЕЛЛИ,2009,
Ж14,ТОРГАШОВА,АННА,2008,
Ж14,ТОРГАШОВА,ЯНА,2008,
Ж16,НЕЛИДОВА,ВЕРОНИКА,2006,
Ж16,ЯКОВЛЕВА,МАРИЯ,2006,
Ж21,ПАРАМОНОВА,ЛЮДМИЛА,1989,
Ж40,ОРЛОВА,НАТАЛЬЯ,1975,2р
Ж40,МИХАЙЛОВА,ТАТЬЯНА,1971,3р
Ж40,СТЕПАНОВА,НАТАЛЬЯ,1965,
Ж10,НАРОЛЬСКАЯ,АЛЕНА,2011,
М10,ЗЕРНОВ,ГЛЕБ,2012,
Ж10,ЛОВЦОВА,АЛЕКСАНДРА,2014,
"""
        val appTmp = createTempDirectory()
        File("$appTmp/1.csv").writeText(velikieLuki)
        val groupsTmp = createTempDirectory()
        Application(appTmp.toString(), groupsTmp.toString(), kotlin.random.Random(1000)).create()
        return groupsTmp
    }

    @Test
    fun testGenerateGroupsFiles() {
        val tmp = createTmp()
        assertEquals("""Номер,Группа,Фамилия,Имя,Г.р.,Разр.,Команда,Время
0,Ж12,САФАРЯН,НЕЛЛИ,2009,,1,12:00:00
1,Ж12,БУБНОВА,КСЕНИЯ,2009,,1,12:01:00
2,Ж12,ПОДСТЕЖОНОК,ПОЛИНА,2009,,1,12:02:00
3,Ж12,АНТОНОВА,ВАСИЛИСА,2010,,1,12:03:00
4,Ж12,ЛИНКЕВИЧ,АЛИСА,2010,,1,12:04:00
""", File("$tmp/Ж12.csv").readText().replace("\r\n", "\n"))
        val actual = File("$tmp").list()!!.size // Number of groups in which they compete
        assertEquals(13, actual)
        assertEquals(false, File("$tmp/Ж8.csv").exists())
    }


}