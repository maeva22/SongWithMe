package fr.enssat.singwithme.MdNs.Back

data class ParoleParse(
    var txt: MutableList<String> = mutableListOf<String>(),
    var timerStart: MutableList<Int> = mutableListOf<Int>(),
    var timerEnd: MutableList<Int> = mutableListOf<Int>(),
    var totalLetter: Int = 0,
    var totalText: String = "",
    var totalTimer: Int=0
)

fun initParoleParle() : ParoleParse {
    return ParoleParse()
}

fun tabToString(textsong: String) : String {
    var tps = ""
    for (value in textsong) {
        tps += value + "\n"
    }
    return tps
}

fun timeCodeToMs(timeCode : String) : Int{
    return (timeCode.split(':')[0]).toInt()*60000 + (timeCode.split(':')[1]).toInt()*1000
}

fun transformToData(textsong:String): MutableList<ParoleParse> {
    val regexRule  ="""(?=(?>\{ *([^\n {}]+) *\}([^\n{}]+\n?)(?>\{ *([^\n {}]+) *\}(\n)?)))"""
    val regex = Regex(regexRule,RegexOption.MULTILINE)

    val result: Sequence<MatchResult>  =  regex.findAll(textsong)

    val tab: MutableList<ParoleParse> = mutableListOf(ParoleParse())
    tab.add(ParoleParse())
    for(pack in result){
        val check =  pack.groups[2]!!.value.indexOf("\n")!=-1 || pack.groups[4] !=null


        tab.last().txt.add( pack.groups[2]!!.value.replace('\n',' '))
        tab.last().totalText+=tab.last().txt.last()
        tab.last().timerStart.add( timeCodeToMs(pack.groups[1]!!.value))
        tab.last().timerEnd.add( timeCodeToMs(pack.groups[3]!!.value))

        if(check){
            val totaltimer = tab.last().timerEnd.last() - tab.last().timerStart.first()
            tab.last().totalTimer = totaltimer
            tab.last().totalLetter = tab.last().totalText.length
            tab.add(ParoleParse())
        }

    }
    tab.removeFirst()
    tab.removeLast()
    return tab

}


fun main() {
    var textsong = parseMd("https://gcpa-enssat-24-25.s3.eu-west-3.amazonaws.com/DontLookBack/DontLookBack.md")
    var text = tabToString(textsong)
    transformToData(text)

}