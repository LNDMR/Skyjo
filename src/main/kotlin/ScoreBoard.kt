class ScoreBoard(var gameName: String,  var player1name: String, var player2name: String) {


    var data: MutableList<Int> = mutableListOf()


    // TODO: double check
    fun extendBoard(round: Int, player1_score: Int, player2_score: Int){
        if(data.isEmpty()){
            data.addAll(listOf(round, player1_score, player2_score))
        } else {
            data.addAll(listOf(round, data[data.size-2] + player1_score, data[data.size-1] + player2_score))
        }
    }


    // TODO: fix the appearance please
    fun display(){
        val yellow = "\u001b[33m"
        val reset = "\u001b[0m"
        println()
        println("><><><><><><><><><><><><><><><><><><><><><><><><><><><")
        println("     $gameName score board")
        println("><><><><><><><><><><><><><><><><><><><><><><><><><><><")
        println()
        println("     round".padEnd(5, ' ')  + " |" +
                "     $player1name - score".padEnd(5, ' ') + "   |" +
                "     $player2name - score".padEnd(5, ' '))
        println("------------------------------------------------------")
        if(!data.isEmpty()) {
            for (i in 0..data.size - 1 step 3) {
                print("     ".padEnd(5, ' ') + yellow + data[i].toString().padEnd(7, ' ') + reset + " |")
                print("     ".padEnd(10, ' ') + yellow + data[i + 1].toString().padEnd(5, ' ') + reset + " |")
                println("     ".padEnd(10, ' ') + yellow + data[i + 2] + reset)
            }
        }
    }


    fun victoryCeremony(){
        val green = "\u001b[32m"
        val red = "\u001b[31m"
        val reset = "\u001b[0m"
        println()
        println()
        println("This game is finished. And the winner is: ")
        println()
        if(data[data.size-2] < data[data.size-1]){
            println(green + player1name.uppercase() + reset + " won with ${data[data.size-2]} total points")
            println(red + player2name.uppercase() + reset + " lost with ${data[data.size-1]} total points")
        }
    }
}

