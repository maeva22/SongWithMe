package fr.enssat.singwithme.MdNs

data class ParoleParse(
    val txt: String,
    val timerStart: Int,
    val timerEnd: Int,
    val end : Boolean
)

fun initParoleParle(txt: String, timerStart: Int, timerEnd: Int, end : Boolean) : ParoleParse{
    return ParoleParse(txt = txt, timerStart = timerStart, timerEnd =  timerEnd, end = end)
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

    val tab: MutableList<ParoleParse> = mutableListOf(
        ParoleParse(txt = "start", timerStart = 0, timerEnd = 0, end = true)
    )
    for(pack in result){
        val check =  pack.groups[2]!!.value.indexOf("\n")!=-1 || pack.groups[4] !=null
        tab.add(
            ParoleParse(txt = pack.groups[2]!!.value.replace('\n',' ') ,
                timerStart =timeCodeToMs(pack.groups[1]!!.value) ,
                timerEnd =timeCodeToMs(pack.groups[3]!!.value), end =check )
        )
    }

    for( line in tab ){
        print( line.timerStart.toString() + " / " + line.timerEnd.toString() + " : " + line .txt )
        if( line.end) print('\n')
    }
    return tab

}


fun main() {
    var textsong = parseMd("https://gcpa-enssat-24-25.s3.eu-west-3.amazonaws.com/DontLookBack/DontLookBack.md")
    var text = tabToString(textsong)
    transformToData(text)

}