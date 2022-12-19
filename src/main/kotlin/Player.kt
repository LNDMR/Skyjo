open class Player(var name: String, var currentCards: PlayingField = PlayingField(mutableListOf()),
                  var score: Int = 0, var terminate: Boolean = false) {


    open fun chooseCard(): Int{
        var choice: String
        println("Please select a card to turn over: [e.g. 1A, 3B, ...] ")
        choice = readln()
        while(!currentCards.references.containsKey(choice)){
            println("Your input is not valid. Please try again.")
            println()
            println("Please select a card to turn over: [e.g. 1A, 3B, ...] ")
            choice = readln()
        }
        // TODO: understand the null situation in more detail
        return currentCards.references[choice]!!
    }


    open fun flipCard(i: Int){
        if(!currentCards.cards[i].flipped){
            currentCards.cards[i].turnOver()
        } else {
            var tryagain: Int
            do{
                println("This card is already facing up. Please choose another one: ")
                tryagain = chooseCard()
            } while(currentCards.cards[tryagain].flipped)
            currentCards.cards[tryagain].turnOver()
        }
    }


    open fun drawCard(discardPile: MutableList<Card>, pile: MutableList<Card>): Card {
        var newCard: Card
        println()
        println()
        var choice: Int = 0
        do {
            try {
                println("Do you want to draw a card from the draw pile or discard pile? [1 = draw, 2 = discard]: ")
                choice = readln().toInt()
            } catch (ex: Exception) {
                println("Your input is not valid. Please try again.")
            }
        } while(choice != 1 && choice != 2)
        if(choice == 1){
            newCard = pile.removeFirst()
        } else {
            newCard = discardPile.removeLast()
        }
        newCard.turnOver()
        return newCard
    }


    open fun keepOrDismiss(newCard: Card, discardPile: MutableList<Card>) {
        println()
        var choice: Int = 0
        do {
            try {
                println("Do you want to keep the card or dismiss it? [1 = keep, 2 = dismiss]: ")
                choice = readln().toInt()
            } catch (ex: Exception) {
                println("Your input is not valid. Please try again.")
            }
        } while(choice != 1 && choice != 2)
        if(choice == 1){
            println("$name has decided to swap the new card with a card on the playing field.")
            dismissCard(currentCards.swapCard(currentCards.cards[chooseCard()], newCard),discardPile)
        } else {
            dismissCard(newCard, discardPile)
            println("$name has decided to flip a hidden card.")
            flipCard(chooseCard())
        }
    }


    fun dismissCard(card: Card, discardPile: MutableList<Card>) {
        discardPile.add(card)
        println("The card ${card.front} has been dropped on the discard pile.")
    }


    open fun wantToCloseRound(discardPile: MutableList<Card>){
        println()
        endOfTurnCheck(discardPile)
        if (currentCards.allCardsRevealed){
            println("Your last hidden card has been revealed in this turn. You just ended this round.")
            terminate = true
        } else {
            println("Do you want to close this round? [yes, no]")
            var close: String = readln()
            while (close != "yes" && close != "no") {
                println()
                println("Your input is not valid. Please try again.")
                println()
                println("Do you want to close this round? [yes, no]")
                close = readln()
            }
            terminate = close == "yes"
        }
    }


    fun endOfTurnCheck(discardPile: MutableList<Card>){
        currentCards.checkAllColumns(discardPile)
        currentCards.allOpen()
    }

}
