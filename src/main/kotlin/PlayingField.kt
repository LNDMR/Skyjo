class PlayingField(var cards: MutableList<Card>) {

    var column1Idx: MutableList<Int> = mutableListOf(0,4,8)
    var column2Idx: MutableList<Int> = mutableListOf(1,5,9)
    var column3Idx: MutableList<Int> = mutableListOf(2,6,10)
    var column4Idx: MutableList<Int> = mutableListOf(3,7,11)
    var toBeDeleted: MutableList<Int> = mutableListOf()
    var allCardsRevealed: Boolean = false
    var references: MutableMap<String, Int> = mutableMapOf("1A" to 0, "1B" to 1, "1C" to 2, "1D" to 3, "2A" to 4, "2B" to 5,
        "2C" to 6, "2D" to 7, "3A" to 8, "3B" to 9, "3C" to 10, "3D" to 11)


    // in case all cards in a column are facing upward that column is checked for 3 equal values
    // if the condition is being met - the regarding indices are saved temporarily in a separate list 'toBeDeleted'
    fun checkColumn(col: MutableList<Int>){
        if((cards[col[0]].flipped == cards[col[1]].flipped) && (cards[col[0]].flipped== cards[col[2]].flipped)){
            if((cards[col[0]].front == cards[col[1]].front) && (cards[col[0]].front== cards[col[2]].front)){
                toBeDeleted.addAll(col)
            }
        }
    }


    // AFTER all remaining columns of the playing field have been checked for equal values a potential equal column
    // is going to be deleted and the cards are dumped on the discard pile
    fun cleanUp(discardPile: MutableList<Card>){
        for(ind in toBeDeleted.reversed() ) {
            discardPile.add(cards.removeAt(ind))
        }
        toBeDeleted.clear()
        swap_indices()
    }


    // everytime a column gets eliminated, the lists that refer to the columns indices need to be adjusted
    fun swap_indices(){
        when(cards.size){
            9 -> {
                column1Idx = mutableListOf(0,3,6)
                column2Idx = mutableListOf(1,4,7)
                column3Idx = mutableListOf(2,5,8)
                column4Idx = mutableListOf()
            }
            6 -> {
                column1Idx = mutableListOf(0,2,4)
                column2Idx = mutableListOf(1,3,5)
                column3Idx = mutableListOf()
                column4Idx = mutableListOf()
            }
            3 -> {
                column1Idx = mutableListOf(0,1,2)
                column2Idx = mutableListOf()
                column3Idx = mutableListOf()
                column4Idx = mutableListOf()
            }
        }
    }


    // depending on the size of the current playing field the columns will be checked one by one
    fun checkAllColumns(discardPile: MutableList<Card>) {
        when (cards.size) {
            12 -> {
                checkColumn(column1Idx)
                checkColumn(column2Idx)
                checkColumn(column3Idx)
                checkColumn(column4Idx)
                cleanUp(discardPile)
            }
            9 -> {
                checkColumn(column1Idx)
                checkColumn(column2Idx)
                checkColumn(column3Idx)
                cleanUp(discardPile)
            }
            6 -> {
                checkColumn(column1Idx)
                checkColumn(column2Idx)
                cleanUp(discardPile)
            }
            3 -> {
                checkColumn(column1Idx)
                cleanUp(discardPile)
            }
            else -> println("checkAllColumns() & checkColumn() => ERROR in cards.size")
        }
    }


    // calculates the current score of the regarding playing field considering only cards that face upward
    fun calculateScore(): Int{
        println()
        var sum: Int = 0
        for(card in cards){
            if(card.flipped){
                sum += card.front
            }
        }
        return sum
    }


    // checks if all cards in the playing field are facing upward
    // if the condition is met - 'allCardsReveal' will be set to true which indicates the end of the current round
    fun allOpen(){
        println()
        var count: Int = 0
        for (card in cards) {
            if (card.flipped)
                count++
        }
        if(count == cards.size)
            allCardsRevealed = true
    }


    // swaps the previously drawn card with any chosen card (facing upward or downward) on the regarding playing field
    fun swapCard(oldCard: Card, newCard: Card): Card{
        cards.add(cards.indexOf(oldCard), newCard)
        cards.remove(oldCard)
        return oldCard
    }


    // if a player terminates a round - all remaining cards facing downward on the playing field need to be flipped
    // their values need to be considered while calculation the total score
    fun flipRest(){
        for (card in cards){
            if(card.flipped != false)
                card.turnOver()
        }
    }


    // output adjusted to number of columns
    fun display(){
        println()
        var ind: Int = 0
        val blue = "\u001b[34m"
        val reset = "\u001b[0m"
        when(cards.size){
            12 -> {
                println((" ".padEnd(11,' '))+"A"+(" ".padEnd(4,' ')) + " |"+
                        (" ".padEnd(5,' '))+"B"+(" ".padEnd(4,' ')) + " |"+
                        (" ".padEnd(5,' '))+"C"+(" ".padEnd(4,' ')) + " |"+
                        (" ".padEnd(5,' '))+"D"+(" ".padEnd(4,' ')) + " ")
                println("     ".padEnd(54, '-'))
                for (i in 1..3) {
                    print(" $i   |")
                    for (j in 0..3) {
                        print((" ".padEnd(5,' '))+ blue + cards[ind].surface.toString().padEnd(5, ' ')+ reset + " |")
                        ind++
                    }
                    println()
                }
            }
            9 -> {
                println((" ".padEnd(11,' '))+"A"+(" ".padEnd(4,' ')) + " |"+
                        (" ".padEnd(5,' '))+"B"+(" ".padEnd(4,' ')) + " |"+
                        (" ".padEnd(5,' '))+"C"+(" ".padEnd(4,' ')) + " ")
                println("     ".padEnd(42, '-'))
                for (i in 1..3) {
                    print(" $i   |")
                    for (j in 0..2) {
                        print(" " + blue + cards[ind].surface.toString().padEnd(10, ' ') + reset + " |")
                        ind++
                    }
                    println()
                }
            }
            6 -> {
                println((" ".padEnd(11,' '))+"A"+(" ".padEnd(4,' ')) + " |"+
                        (" ".padEnd(5,' '))+"B"+(" ".padEnd(4,' ')) + " ")
                println("     ".padEnd(30, '-'))
                for (i in 1..3) {
                    print(" $i   |")
                    for (j in 0..1) {
                        print(" " + blue + cards[ind].surface.toString().padEnd(10, ' ') + reset + " |")
                        ind++
                    }
                    println()
                }
            }
            3 -> {
                println((" ".padEnd(11,' '))+"A"+(" ".padEnd(4,' ')) + " ")
                println("     ".padEnd(18, '-'))
                for (i in 1..3) {
                    print(" $i   |")
                    println(" " + cards[ind].surface.toString().padEnd(10, ' '))
                    ind++
                }
            }
            else -> println("PlayingField.display() => ERROR somewhere")
        }
    }
}


